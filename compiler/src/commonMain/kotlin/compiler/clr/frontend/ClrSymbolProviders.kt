/*
   Copyright 2025 Nyayurin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package compiler.clr.frontend

import compiler.clr.*
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.EffectiveVisibility
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.analysis.checkers.hasAnnotationOrInsideAnnotatedClass
import org.jetbrains.kotlin.fir.builder.buildPackageDirective
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.builder.*
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.declarations.impl.FirResolvedDeclarationStatusImpl
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.jetbrains.kotlin.fir.expressions.impl.FirEmptyAnnotationArgumentMapping
import org.jetbrains.kotlin.fir.resolve.dfa.cfg.FirStub
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProviderInternals
import org.jetbrains.kotlin.fir.scopes.FirKotlinScopeProvider
import org.jetbrains.kotlin.fir.scopes.kotlinScopeProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.types.ConeTypeProjection
import org.jetbrains.kotlin.fir.types.FirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.fir.types.impl.FirImplicitNullableAnyTypeRef
import org.jetbrains.kotlin.fir.types.toLookupTag
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.*

class ClrSymbolNamesProvider : FirSymbolNamesProvider() {
	private val packageNames = mutableSetOf<FqName>()
	private val classNames = mutableMapOf<FqName, MutableSet<Name>>()
	private val callableNames = mutableMapOf<FqName, MutableSet<Name>>()

	override val hasSpecificClassifierPackageNamesComputation: Boolean = true
	override val hasSpecificCallablePackageNamesComputation: Boolean = true

	override fun getPackageNames(): Set<String> = packageNames.map { it.asString() }.toSet()

	override fun getTopLevelClassifierNamesInPackage(packageFqName: FqName): Set<Name> =
		classNames[packageFqName] ?: emptySet()

	override fun getTopLevelCallableNamesInPackage(packageFqName: FqName): Set<Name> =
		callableNames[packageFqName] ?: emptySet()

	fun registerPackageName(packageFqName: FqName) {
		packageNames.add(packageFqName)
	}

	fun registerClassName(packageFqName: FqName, name: Name) {
		classNames.getOrPut(
			key = packageFqName,
			defaultValue = { mutableSetOf() }
		) += name
	}

	fun registerCallableName(packageFqName: FqName, name: Name) {
		callableNames.getOrPut(
			key = packageFqName,
			defaultValue = { mutableSetOf() }
		) += name
	}
}

@OptIn(FirImplementationDetail::class, SymbolInternals::class)
class ClrSymbolProvider(
	session: FirSession,
	assemblies: Map<String, NodeAssembly>,
	rewriteModuleData: FirModuleData?,
) : FirSymbolProvider(session) {
	private val clrSymbolNamesProvider = ClrSymbolNamesProvider()
	private val classPackages = mutableMapOf<String, MutableList<ClassId>>()
	private val classSymbols = mutableMapOf<ClassId, FirClassLikeSymbol<*>>()
	private val functionPackages = mutableMapOf<String, MutableList<CallableId>>()
	private val functionSymbols = mutableMapOf<CallableId, MutableList<FirNamedFunctionSymbol>>()

	private val firModuleData: FirModuleData = rewriteModuleData
		?: session.nullableModuleData
		?: error("Module data is not registered in $session")

	private val firFileSymbols = assemblies.mapValues { (_, node) -> node.types.map { buildFile(it) } }.apply {
		/*File("fir").mkdir()
		this.forEach { (assembly, fileSymbols) ->
			File("fir/$assembly").printWriter().use { writer ->
				writer.println(fileSymbols.map { it.fir.render() })
			}
		}*/
	}

	private fun buildFile(node: NodeType) = FirFileSymbol().also { fileSymbol ->
		buildFile {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			packageDirective = buildPackageDirective {
				packageFqName = FqName(node.namespace ?: "")
			}

			declarations += when {
				node.attributes.any { it == "kotlin.clr.KotlinObject" } -> buildObject(node).fir
				node.attributes.any { it == "kotlin.clr.KotlinFileClass" } -> buildFileClass(node).fir
				else -> buildClass(node).fir
			}
			name = node.name
			symbol = fileSymbol
		}
		clrSymbolNamesProvider.registerPackageName(fileSymbol.fir.packageFqName)
	}

	private fun buildObject(node: NodeType) = FirRegularClassSymbol(
		classId = classId(node.namespace ?: "", node.name)
	).also { classSymbol ->
		buildRegularClass {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			typeParameters
			status = FirResolvedDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			classKind = ClassKind.OBJECT

			declarations += node.methods.map { buildTopLevelFunction(classSymbol.classId, it).fir }

			name = classSymbol.name
			scopeProvider = session.kotlinScopeProvider
			symbol = classSymbol
		}
		clrSymbolNamesProvider.registerClassName(classSymbol.packageFqName(), classSymbol.name)
		classPackages.getOrPut(
			key = node.namespace ?: "",
			defaultValue = { mutableListOf() }
		) += classSymbol.classId
		classSymbols[classSymbol.classId] = classSymbol
	}

	private fun buildFileClass(node: NodeType) = FirRegularClassSymbol(
		classId = classId(node.namespace ?: "", node.name)
	).also { classSymbol ->
		buildRegularClass {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			typeParameters
			status = FirResolvedDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			classKind = ClassKind.CLASS

			declarations += node.methods.map { buildTopLevelFunction(classSymbol.classId, it).fir }

			name = classSymbol.name
			scopeProvider = session.kotlinScopeProvider
			symbol = classSymbol
			companionObjectSymbol = buildCompanionClass(node, classSymbol.classId)
		}
		clrSymbolNamesProvider.registerClassName(classSymbol.packageFqName(), classSymbol.name)
		classPackages.getOrPut(
			key = node.namespace ?: "",
			defaultValue = { mutableListOf() }
		) += classSymbol.classId
		classSymbols[classSymbol.classId] = classSymbol
	}

	private fun buildClass(node: NodeType) = FirRegularClassSymbol(
		classId = classId(node.namespace ?: "", node.name)
	).also { classSymbol ->
		buildRegularClass {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			typeParameters
			status = FirResolvedDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			classKind = ClassKind.CLASS

			declarations += node.constructors
				.filter { !it.isStatic }
				.map { buildConstructor(classSymbol.classId, it).fir }
			declarations += node.methods
				.filter { !it.isStatic }
				.map { buildFunction(classSymbol.classId, it).fir }

			name = classSymbol.name
			scopeProvider = session.kotlinScopeProvider
			symbol = classSymbol
			companionObjectSymbol = buildCompanionClass(node, classSymbol.classId)
		}
		clrSymbolNamesProvider.registerClassName(classSymbol.packageFqName(), classSymbol.name)
		classPackages.getOrPut(
			key = node.namespace ?: "",
			defaultValue = { mutableListOf() }
		) += classSymbol.classId
		classSymbols[classSymbol.classId] = classSymbol
	}

	private fun buildCompanionClass(node: NodeType, parent: ClassId) = FirRegularClassSymbol(
		classId = parent.createNestedClassId(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
	).also { classSymbol ->
		buildRegularClass {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			typeParameters
			status = FirDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL
			).apply {
				isCompanion = true
			}.resolved(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			classKind = ClassKind.OBJECT

			declarations += node.constructors
				.filter { it.isStatic }
				.map { buildConstructor(classSymbol.classId, it).fir }
			declarations += node.methods
				.filter { it.isStatic }
				.map { buildFunction(classSymbol.classId, it).fir }

			name = classSymbol.name
			scopeProvider = session.kotlinScopeProvider
			symbol = classSymbol
		}
		classPackages.getOrPut(
			key = node.namespace ?: "",
			defaultValue = { mutableListOf() }
		) += classSymbol.classId
		classSymbols[classSymbol.classId] = classSymbol
	}

	private fun buildConstructor(classId: ClassId, node: NodeConstructor) = FirConstructorSymbol(
		callableId = classId.callableIdForConstructor()
	).also { constructorSymbol ->
		buildConstructor {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			status = FirResolvedDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			returnTypeRef = resolveFirTypeRefForClr(classId.asString(), true)
			if (!node.isStatic) {
				dispatchReceiverType = ConeClassLikeTypeImpl(
					classId.toLookupTag(),
					emptyArray<ConeTypeProjection>(),
					false
				)
			}
			symbol = constructorSymbol
		}
	}

	private fun buildTopLevelFunction(classId: ClassId, node: NodeMethod) = FirNamedFunctionSymbol(
		callableId = CallableId(classId.packageFqName, Name.identifier(node.name))
	).also { functionSymbol ->
		buildSimpleFunction {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			status = FirDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL
			).apply {
				isStatic = true
			}.resolved(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			returnTypeRef = when (node.returnType != null) {
				true -> resolveFirTypeRefForClr(node.returnType, true)
				else -> FirImplicitNullableAnyTypeRef(null)
			}

			dispatchReceiverType = ConeClassLikeTypeImpl(
				lookupTag = classId.toLookupTag(),
				typeArguments = emptyArray(),
				isMarkedNullable = false
			)

			val isExtension = node.attributes
				.filterNotNull()
				.any { it == "kotlin.clr.KotlinExtension" }
			valueParameters += node.parameters
				.drop(
					when (isExtension) {
						true -> 1
						false -> 0
					}
				)
				.map { buildValueParameter(it, functionSymbol).fir }
			if (isExtension) {
				receiverParameter = FirReceiverParameterSymbol().also { parameterSymbol ->
					buildReceiverParameter {
						moduleData = firModuleData
						origin = FirDeclarationOrigin.Library
						symbol = parameterSymbol
						typeRef = buildResolvedTypeRef {
							coneType = this@buildSimpleFunction.valueParameters.first().returnTypeRef.coneType
						}
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
			}
			name = Name.identifier(node.name)
			symbol = functionSymbol
		}
		clrSymbolNamesProvider.registerCallableName(functionSymbol.packageFqName(), functionSymbol.name)
		functionPackages.getOrPut(
			key = functionSymbol.packageFqName().asString(),
			defaultValue = { mutableListOf() }
		) += functionSymbol.callableId
		functionSymbols.getOrPut(
			key = functionSymbol.callableId,
			defaultValue = { mutableListOf() }
		) += functionSymbol
	}

	private fun buildFunction(classId: ClassId, node: NodeMethod) = FirNamedFunctionSymbol(
		callableId = CallableId(classId, Name.identifier(node.name))
	).also { functionSymbol ->
		buildSimpleFunction {
			moduleData = firModuleData
			origin = FirDeclarationOrigin.Library
			status = FirResolvedDeclarationStatusImpl(
				Visibilities.Public,
				Modality.FINAL,
				EffectiveVisibility.Public
			)
			returnTypeRef = when (node.returnType != null) {
				true -> resolveFirTypeRefForClr(node.returnType, true)
				else -> FirImplicitNullableAnyTypeRef(null)
			}

			valueParameters += node.parameters.map { buildValueParameter(it, functionSymbol).fir }
			name = Name.identifier(node.name)
			symbol = functionSymbol
			if (node.isStatic) {
				annotations += buildAnnotation {
					annotationTypeRef = buildResolvedTypeRef {
						coneType = ConeClassLikeTypeImpl(
							lookupTag = classId("kotlin.clr", "ClrStatic").toLookupTag(),
							typeArguments = emptyArray(),
							isMarkedNullable = false
						)
					}
					argumentMapping = FirEmptyAnnotationArgumentMapping
				}
			}
		}
	}

	private fun buildValueParameter(node: NodeParameter, containingSymbol: FirFunctionSymbol<*>) =
		FirValueParameterSymbol(
			name = Name.identifier(node.name!!)
		).also { parameterSymbol ->
			buildValueParameter {
				moduleData = firModuleData
				origin = FirDeclarationOrigin.Library
				returnTypeRef = when (node.type != null) {
					true -> resolveFirTypeRefForClr(node.type, true)
					else -> FirImplicitNullableAnyTypeRef(null)
				}
				name = parameterSymbol.callableId.callableName
				symbol = parameterSymbol
				if (node.hasDefaultValue) {
					defaultValue = FirStub
				}
				containingDeclarationSymbol = containingSymbol
			}
		}

	fun hasClassSymbol(classId: ClassId): Boolean = classId in classSymbols

	override fun getClassLikeSymbolByClassId(classId: ClassId): FirClassLikeSymbol<*>? {
		return classSymbols[classId]
	}

	private fun resolveFirTypeRefForClr(
		clrTypeName: String,
		isReturnPosition: Boolean = false,
	): FirResolvedTypeRef {
		val (classId, isPrimitiveOrKnownValueType) = when (clrTypeName) {
			"System.Void" -> StandardClassIds.Unit to true
			"System.String" -> StandardClassIds.String to false
			"System.Boolean" -> StandardClassIds.Boolean to true
			"System.Char" -> StandardClassIds.Char to true
			"System.Byte" -> StandardClassIds.Byte to true
			"System.SByte" -> StandardClassIds.Byte to true
			"System.Int16" -> StandardClassIds.Short to true
			"System.UInt16" -> StandardClassIds.UShort to true
			"System.Int32" -> StandardClassIds.Int to true
			"System.UInt32" -> StandardClassIds.UInt to true
			"System.Int64" -> StandardClassIds.Long to true
			"System.UInt64" -> StandardClassIds.ULong to true
			"System.Single" -> StandardClassIds.Float to true
			"System.Double" -> StandardClassIds.Double to true
			"System.Object" -> StandardClassIds.Any to false
			else -> {
				val fqNameParts = clrTypeName.split('.', '+')
				if (fqNameParts.isNotEmpty()) {
					val name = Name.identifier(fqNameParts.last())
					val pkgSegments = fqNameParts.dropLast(1)
					val pkgName = when (pkgSegments.isEmpty() && typeCacheContainsTopLevel(fqNameParts.last())) {
						true -> findNamespaceForType(fqNameParts.last()) ?: ""
						else -> pkgSegments.joinToString(".")
					}
					ClassId(FqName(pkgName), name) to false
				} else {
					StandardClassIds.Any to false
				}
			}
		}

		val isNullable = when {
			isReturnPosition && classId == StandardClassIds.Unit -> false
			isPrimitiveOrKnownValueType -> false
			else -> true
		}

		return buildResolvedTypeRef {
			coneType = ConeClassLikeTypeImpl(
				classId.toLookupTag(),
				emptyArray<ConeTypeProjection>(),
				isNullable
			)
		}
	}

	private fun typeCacheContainsTopLevel(typeName: String): Boolean {
		return classPackages.values.any { classIds ->
			classIds.any { it.shortClassName.asString() == typeName && it.packageFqName.asString() == "" }
		}
	}

	private fun findNamespaceForType(typeName: String): String? {
		classPackages.forEach { (namespace, types) ->
			val found = types.any {
				it.shortClassName.asString() == typeName
						|| it.shortClassName.asString().substringAfterLast('.') == typeName
			}
			if (found) return namespace
		}
		return null
	}

	@FirSymbolProviderInternals
	override fun getTopLevelCallableSymbolsTo(
		destination: MutableList<FirCallableSymbol<*>>,
		packageFqName: FqName,
		name: Name,
	) {
		destination += functionSymbols
			.filter { it.key.packageName == packageFqName && it.key.callableName == name }
			.flatMap { it.value }
	}

	@FirSymbolProviderInternals
	override fun getTopLevelFunctionSymbolsTo(
		destination: MutableList<FirNamedFunctionSymbol>,
		packageFqName: FqName,
		name: Name,
	) {
		destination += functionSymbols
			.filter { it.key.packageName == packageFqName && it.key.callableName == name }
			.flatMap { it.value }
	}

	@FirSymbolProviderInternals
	override fun getTopLevelPropertySymbolsTo(
		destination: MutableList<FirPropertySymbol>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	override fun hasPackage(fqName: FqName): Boolean {
		val packageName = fqName.asString()
		return classPackages.containsKey(packageName) || functionPackages.containsKey(packageName)
	}

	override val symbolNamesProvider: FirSymbolNamesProvider = clrSymbolNamesProvider
}

class ClrAssemblyBasedSymbolProvider(
	session: FirSession,
	moduleData: FirModuleData,
	kotlinScopeProvider: FirKotlinScopeProvider,
	assemblies: Map<String, NodeAssembly>,
) : FirSymbolProvider(session) {
	private val symbolProvider = ClrSymbolProvider(session, assemblies, moduleData)
	override fun getClassLikeSymbolByClassId(classId: ClassId): FirClassLikeSymbol<*>? =
		symbolProvider.getClassLikeSymbolByClassId(classId)

	@FirSymbolProviderInternals
	override fun getTopLevelCallableSymbolsTo(
		destination: MutableList<FirCallableSymbol<*>>,
		packageFqName: FqName,
		name: Name,
	) = symbolProvider.getTopLevelCallableSymbolsTo(destination, packageFqName, name)

	@FirSymbolProviderInternals
	override fun getTopLevelFunctionSymbolsTo(
		destination: MutableList<FirNamedFunctionSymbol>,
		packageFqName: FqName,
		name: Name,
	) = symbolProvider.getTopLevelFunctionSymbolsTo(destination, packageFqName, name)

	@FirSymbolProviderInternals
	override fun getTopLevelPropertySymbolsTo(
		destination: MutableList<FirPropertySymbol>,
		packageFqName: FqName,
		name: Name,
	) = symbolProvider.getTopLevelPropertySymbolsTo(destination, packageFqName, name)

	override fun hasPackage(fqName: FqName): Boolean =
		fqName == FqName("System") || fqName == FqName("System.Console") || symbolProvider.hasPackage(fqName)

	override val symbolNamesProvider: FirSymbolNamesProvider
		get() = symbolProvider.symbolNamesProvider
}

/**
 * CLR内置类型符号提供器
 */
@OptIn(SymbolInternals::class)
class ClrBuiltinsSymbolProvider(
	session: FirSession,
	moduleData: FirModuleData,
	kotlinScopeProvider: FirKotlinScopeProvider,
	assemblies: Map<String, NodeAssembly>,
) : FirSymbolProvider(session) {
	private val stdlibSymbolProvider = ClrSymbolProvider(session, assemblies, moduleData)

	private val annotationSymbol = StandardClassIds.Annotation.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val anySymbol = StandardClassIds.Any.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
		declarations += FirConstructorSymbol(
			callableId = classId.callableIdForConstructor()
		).also { constructorSymbol ->
			buildPrimaryConstructor {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirResolvedDeclarationStatusImpl(
					visibility = Visibilities.Public,
					modality = Modality.FINAL,
					effectiveVisibility = EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						lookupTag = classId.toLookupTag(),
						typeArguments = emptyArray(),
						isMarkedNullable = false
					)
				}
				symbol = constructorSymbol
			}.apply {
				containingClassForStaticMemberAttr = classId.toLookupTag()
			}
		}.fir
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("equals"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirResolvedDeclarationStatusImpl(
					visibility = Visibilities.Public,
					modality = Modality.OPEN,
					effectiveVisibility = EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						lookupTag = StandardClassIds.Boolean.toLookupTag(),
						typeArguments = emptyArray(),
						isMarkedNullable = false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					lookupTag = classId.toLookupTag(),
					typeArguments = emptyArray(),
					isMarkedNullable = false
				)
				valueParameters += FirValueParameterSymbol(
					name = Name.identifier("other")
				).also { valueParameterSymbol ->
					buildValueParameter {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						returnTypeRef = buildResolvedTypeRef {
							coneType = ConeClassLikeTypeImpl(
								lookupTag = StandardClassIds.Any.toLookupTag(),
								typeArguments = emptyArray(),
								isMarkedNullable = true
							)
						}
						name = valueParameterSymbol.name
						symbol = valueParameterSymbol
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
				name = functionSymbol.name
				symbol = functionSymbol
			}.apply {
				containingClassForStaticMemberAttr = classId.toLookupTag()
			}
		}.fir
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("hashCode"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirResolvedDeclarationStatusImpl(
					visibility = Visibilities.Public,
					modality = Modality.OPEN,
					effectiveVisibility = EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						lookupTag = StandardClassIds.Int.toLookupTag(),
						typeArguments = emptyArray(),
						isMarkedNullable = false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					lookupTag = classId.toLookupTag(),
					typeArguments = emptyArray(),
					isMarkedNullable = false
				)
				name = functionSymbol.name
				symbol = functionSymbol
			}.apply {
				containingClassForStaticMemberAttr = classId.toLookupTag()
			}
		}.fir
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("toString"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirResolvedDeclarationStatusImpl(
					visibility = Visibilities.Public,
					modality = Modality.OPEN,
					effectiveVisibility = EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						lookupTag = StandardClassIds.String.toLookupTag(),
						typeArguments = emptyArray(),
						isMarkedNullable = false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					lookupTag = classId.toLookupTag(),
					typeArguments = emptyArray(),
					isMarkedNullable = false
				)
				name = functionSymbol.name
				symbol = functionSymbol
			}.apply {
				containingClassForStaticMemberAttr = classId.toLookupTag()
			}
		}.fir
	}
	private val arraySymbol = StandardClassIds.Array.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val byteArraySymbol = classId("kotlin", "ByteArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val charArraySymbol = classId("kotlin", "CharArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val shortArraySymbol = classId("kotlin", "ShortArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val intArraySymbol = classId("kotlin", "IntArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val longArraySymbol = classId("kotlin", "LongArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val floatArraySymbol = classId("kotlin", "FloatArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val doubleArraySymbol = classId("kotlin", "DoubleArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val booleanArraySymbol = classId("kotlin", "BooleanArray").buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val booleanSymbol = StandardClassIds.Boolean.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val charSymbol = StandardClassIds.Char.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val charSequenceSymbol = StandardClassIds.CharSequence.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val comparableSymbol = StandardClassIds.Comparable.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val enumSymbol = StandardClassIds.Enum.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.ABSTRACT,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val nothingSymbol = StandardClassIds.Nothing.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
		declarations += FirConstructorSymbol(
			callableId = classId.callableIdForConstructor()
		).also { constructorSymbol ->
			buildPrimaryConstructor {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirResolvedDeclarationStatusImpl(
					visibility = Visibilities.Private,
					modality = Modality.FINAL,
					effectiveVisibility = EffectiveVisibility.PrivateInClass
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						lookupTag = classId.toLookupTag(),
						typeArguments = emptyArray(),
						isMarkedNullable = false
					)
				}
				symbol = constructorSymbol
			}.apply {
				containingClassForStaticMemberAttr = classId.toLookupTag()
			}
		}.fir
	}
	private val numberSymbol = StandardClassIds.Number.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.ABSTRACT,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val byteSymbol = StandardClassIds.Byte.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val shortSymbol = StandardClassIds.Short.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val intSymbol = StandardClassIds.Int.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("plus"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirDeclarationStatusImpl(
					Visibilities.Public,
					Modality.FINAL
				).apply {
					isOperator = true
				}.resolved(
					Visibilities.Public,
					Modality.FINAL,
					EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						classId.toLookupTag(),
						emptyArray(),
						false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					classId.toLookupTag(),
					emptyArray(),
					false
				)
				valueParameters += FirValueParameterSymbol(Name.identifier("other")).also { valueParameterSymbol ->
					buildValueParameter {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						returnTypeRef = buildResolvedTypeRef {
							coneType = ConeClassLikeTypeImpl(
								classId.toLookupTag(),
								emptyArray(),
								false
							)
						}
						name = valueParameterSymbol.name
						symbol = valueParameterSymbol
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
				name = functionSymbol.callableId.callableName
				symbol = functionSymbol
			}
		}.fir
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("compareTo"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirDeclarationStatusImpl(
					Visibilities.Public,
					Modality.FINAL
				).apply {
					isOperator = true
				}.resolved(
					Visibilities.Public,
					Modality.FINAL,
					EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						classId.toLookupTag(),
						emptyArray(),
						false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					classId.toLookupTag(),
					emptyArray(),
					false
				)
				valueParameters += FirValueParameterSymbol(Name.identifier("other")).also { valueParameterSymbol ->
					buildValueParameter {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						returnTypeRef = buildResolvedTypeRef {
							coneType = ConeClassLikeTypeImpl(
								classId.toLookupTag(),
								emptyArray(),
								false
							)
						}
						name = valueParameterSymbol.name
						symbol = valueParameterSymbol
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
				name = functionSymbol.callableId.callableName
				symbol = functionSymbol
			}
		}.fir
	}
	private val longSymbol = StandardClassIds.Long.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val floatSymbol = StandardClassIds.Float.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val doubleSymbol = StandardClassIds.Double.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("plus"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirDeclarationStatusImpl(
					Visibilities.Public,
					Modality.FINAL
				).apply {
					isOperator = true
				}.resolved(
					Visibilities.Public,
					Modality.FINAL,
					EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						classId.toLookupTag(),
						emptyArray(),
						false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					classId.toLookupTag(),
					emptyArray(),
					false
				)
				valueParameters += FirValueParameterSymbol(Name.identifier("other")).also { valueParameterSymbol ->
					buildValueParameter {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						returnTypeRef = buildResolvedTypeRef {
							coneType = ConeClassLikeTypeImpl(
								classId.toLookupTag(),
								emptyArray(),
								false
							)
						}
						name = valueParameterSymbol.name
						symbol = valueParameterSymbol
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
				name = functionSymbol.callableId.callableName
				symbol = functionSymbol
			}
		}.fir
		declarations += FirNamedFunctionSymbol(
			callableId = CallableId(classId, Name.identifier("times"))
		).also { functionSymbol ->
			buildSimpleFunction {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				status = FirDeclarationStatusImpl(
					Visibilities.Public,
					Modality.FINAL
				).apply {
					isOperator = true
				}.resolved(
					Visibilities.Public,
					Modality.FINAL,
					EffectiveVisibility.Public
				)
				returnTypeRef = buildResolvedTypeRef {
					coneType = ConeClassLikeTypeImpl(
						classId.toLookupTag(),
						emptyArray(),
						false
					)
				}
				dispatchReceiverType = ConeClassLikeTypeImpl(
					classId.toLookupTag(),
					emptyArray(),
					false
				)
				valueParameters += FirValueParameterSymbol(Name.identifier("other")).also { valueParameterSymbol ->
					buildValueParameter {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						returnTypeRef = buildResolvedTypeRef {
							coneType = ConeClassLikeTypeImpl(
								StandardClassIds.Int.toLookupTag(),
								emptyArray(),
								false
							)
						}
						name = valueParameterSymbol.name
						symbol = valueParameterSymbol
						containingDeclarationSymbol = functionSymbol
					}
				}.fir
				name = functionSymbol.callableId.callableName
				symbol = functionSymbol
			}
		}.fir
	}
	private val stringSymbol = StandardClassIds.String.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.FINAL,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}
	private val throwableSymbol = StandardClassIds.Throwable.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.CLASS
	}

	private val iterableSymbol = StandardClassIds.Iterable.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableIterableSymbol = StandardClassIds.MutableIterable.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val collectionSymbol = StandardClassIds.Collection.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableCollectionSymbol = StandardClassIds.MutableCollection.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val listSymbol = StandardClassIds.List.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableListSymbol = StandardClassIds.MutableList.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val setSymbol = StandardClassIds.Set.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableSetSymbol = StandardClassIds.MutableSet.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mapSymbol = StandardClassIds.Map.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableMapSymbol = StandardClassIds.MutableMap.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val iteratorSymbol = StandardClassIds.Iterator.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableIteratorSymbol = StandardClassIds.MutableIterator.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val listIteratorSymbol = StandardClassIds.ListIterator.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}
	private val mutableListIteratorSymbol = StandardClassIds.MutableListIterator.buildSymbol(moduleData) { classId ->
		status = FirResolvedDeclarationStatusImpl(
			Visibilities.Public,
			Modality.OPEN,
			EffectiveVisibility.Public
		)
		classKind = ClassKind.INTERFACE
	}

	@OptIn(FirImplementationDetail::class)
	private val builtinsClassSymbols = listOf(
		annotationSymbol,
		anySymbol,
		arraySymbol,
		byteArraySymbol,
		charArraySymbol,
		shortArraySymbol,
		intArraySymbol,
		longArraySymbol,
		floatArraySymbol,
		doubleArraySymbol,
		booleanArraySymbol,
		booleanSymbol,
		charSymbol,
		charSequenceSymbol,
		comparableSymbol,
		enumSymbol,
		nothingSymbol,
		numberSymbol,
		byteSymbol,
		shortSymbol,
		intSymbol,
		longSymbol,
		floatSymbol,
		doubleSymbol,
		stringSymbol,
		throwableSymbol,

		iterableSymbol,
		mutableIterableSymbol,
		collectionSymbol,
		mutableCollectionSymbol,
		listSymbol,
		mutableListSymbol,
		setSymbol,
		mutableSetSymbol,
		mapSymbol,
		mutableMapSymbol,
		iteratorSymbol,
		mutableIteratorSymbol,
		listIteratorSymbol,
		mutableListIteratorSymbol
	).associate { it.unpack() }

	private fun ClassId.buildSymbol(
		moduleData: FirModuleData,
		build: FirRegularClassBuilder.(ClassId) -> Unit,
	): FirRegularClassSymbol {
		val returnSymbol: FirRegularClassSymbol
		FirFileSymbol().let { fileSymbol ->
			val classId = this
			buildFile {
				this.moduleData = moduleData
				origin = FirDeclarationOrigin.BuiltIns
				packageDirective = buildPackageDirective {
					packageFqName = classId.packageFqName
				}
				declarations += FirRegularClassSymbol(classId).also { classSymbol ->
					returnSymbol = classSymbol
					buildRegularClass {
						this.moduleData = moduleData
						origin = FirDeclarationOrigin.BuiltIns
						name = classId.shortClassName
						scopeProvider = session.kotlinScopeProvider
						build(this@buildSymbol)
						symbol = classSymbol
					}
				}.fir
				name = classId.shortClassName.asString()
				symbol = fileSymbol
			}
		}
		return returnSymbol
	}

	private fun FirRegularClassSymbol.unpack(): Pair<ClassId, FirRegularClassSymbol> = classId to this

	override fun getClassLikeSymbolByClassId(classId: ClassId) =
		tryGetClassLikeSymbol(classId)
			?: stdlibSymbolProvider.getClassLikeSymbolByClassId(classId)

	override fun hasPackage(fqName: FqName) =
		builtinsClassSymbols.any { it.key.packageFqName == fqName }
				|| stdlibSymbolProvider.hasPackage(fqName)

	override val symbolNamesProvider = object : FirSymbolNamesProvider() {
		private val packageFqNamesSet: Set<FqName> by lazy {
			builtinsClassSymbols.keys.map { it.packageFqName }.toSet()
		}

		private val classifierNamesByPackage: Map<FqName, Set<Name>> by lazy {
			builtinsClassSymbols.keys.groupBy { it.packageFqName }
				.mapValues { entry -> entry.value.map { classId -> classId.shortClassName }.toSet() }
		}

		override val hasSpecificClassifierPackageNamesComputation: Boolean = true
		override val hasSpecificCallablePackageNamesComputation: Boolean = true

		override fun getPackageNames(): Set<String>? = buildSet {
			addAll(packageFqNamesSet.map { it.asString() })
			addAll(stdlibSymbolProvider.symbolNamesProvider.getPackageNames() ?: emptySet())
		}.ifEmpty { null }

		override fun getTopLevelClassifierNamesInPackage(packageFqName: FqName): Set<Name>? {
			val explicitNames = classifierNamesByPackage[packageFqName]
			if (explicitNames != null) {
				return explicitNames
			}

			return stdlibSymbolProvider.symbolNamesProvider.getTopLevelClassifierNamesInPackage(packageFqName)
				?: emptySet()
		}

		override fun getTopLevelCallableNamesInPackage(packageFqName: FqName): Set<Name>? {
			return if (packageFqName in classifierNamesByPackage) emptySet() else emptySet()
		}
	}

	@OptIn(FirImplementationDetail::class)
	private fun tryGetClassLikeSymbol(classId: ClassId): FirClassLikeSymbol<*>? =
		builtinsClassSymbols[classId]

	@FirSymbolProviderInternals
	override fun getTopLevelCallableSymbolsTo(
		destination: MutableList<FirCallableSymbol<*>>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	@FirSymbolProviderInternals
	override fun getTopLevelFunctionSymbolsTo(
		destination: MutableList<FirNamedFunctionSymbol>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	@FirSymbolProviderInternals
	override fun getTopLevelPropertySymbolsTo(
		destination: MutableList<FirPropertySymbol>,
		packageFqName: FqName,
		name: Name,
	) {
	}
}

/**
 * CLR类路径内置符号提供器
 */
class ClrClasspathBuiltinSymbolProvider(
	session: FirSession,
	moduleData: FirModuleData,
	kotlinScopeProvider: FirKotlinScopeProvider,
	assemblies: Map<String, NodeAssembly>,
) : FirSymbolProvider(session) {
	private val delegate = ClrBuiltinsSymbolProvider(
		session,
		moduleData,
		kotlinScopeProvider,
		assemblies
	)

	override fun getClassLikeSymbolByClassId(classId: ClassId) =
		delegate.getClassLikeSymbolByClassId(classId)

	override fun hasPackage(fqName: FqName) =
		delegate.hasPackage(fqName)

	override val symbolNamesProvider get() = delegate.symbolNamesProvider

	@FirSymbolProviderInternals
	override fun getTopLevelCallableSymbolsTo(
		destination: MutableList<FirCallableSymbol<*>>,
		packageFqName: FqName,
		name: Name,
	) = delegate.getTopLevelCallableSymbolsTo(destination, packageFqName, name)

	@FirSymbolProviderInternals
	override fun getTopLevelFunctionSymbolsTo(
		destination: MutableList<FirNamedFunctionSymbol>,
		packageFqName: FqName,
		name: Name,
	) = delegate.getTopLevelFunctionSymbolsTo(destination, packageFqName, name)

	@FirSymbolProviderInternals
	override fun getTopLevelPropertySymbolsTo(
		destination: MutableList<FirPropertySymbol>,
		packageFqName: FqName,
		name: Name,
	) = delegate.getTopLevelPropertySymbolsTo(destination, packageFqName, name)
}

/**
 * CLR实际化内置符号提供器
 */
class ClrActualizingBuiltinSymbolProvider(
	val builtinsSymbolProvider: ClrBuiltinsSymbolProvider,
	private val refinedSourceSymbolProviders: List<FirSymbolProvider>,
) : FirSymbolProvider(builtinsSymbolProvider.session) {
	override fun getClassLikeSymbolByClassId(classId: ClassId): FirRegularClassSymbol? {
		for (symbolProvider in refinedSourceSymbolProviders) {
			val classSymbol = symbolProvider.getClassLikeSymbolByClassId(classId) ?: continue
			if (!classSymbol.hasAnnotationOrInsideAnnotatedClass(
					classId("kotlin.internal", "ActualizeByClrBuiltinProvider"),
					symbolProvider.session
				)
			) {
				continue
			}

			// If there are multiple declarations with the same name, they will be reported as redeclarations by a checker
			return builtinsSymbolProvider.getClassLikeSymbolByClassId(classId) as FirRegularClassSymbol
		}

		return null
	}

	override val symbolNamesProvider: FirSymbolNamesProvider = builtinsSymbolProvider.symbolNamesProvider

	@FirSymbolProviderInternals
	override fun getTopLevelCallableSymbolsTo(
		destination: MutableList<FirCallableSymbol<*>>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	@FirSymbolProviderInternals
	override fun getTopLevelFunctionSymbolsTo(
		destination: MutableList<FirNamedFunctionSymbol>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	@FirSymbolProviderInternals
	override fun getTopLevelPropertySymbolsTo(
		destination: MutableList<FirPropertySymbol>,
		packageFqName: FqName,
		name: Name,
	) {
	}

	override fun hasPackage(fqName: FqName): Boolean = builtinsSymbolProvider.hasPackage(fqName)
}