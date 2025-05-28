package compiler.clr.backend

import compiler.clr.frontend.ClrActualizingBuiltinSymbolProvider
import org.jetbrains.kotlin.backend.common.actualizer.IrExtraActualDeclarationExtractor
import org.jetbrains.kotlin.config.AnalysisFlags
import org.jetbrains.kotlin.fir.backend.Fir2IrClassifierStorage
import org.jetbrains.kotlin.fir.backend.Fir2IrComponents
import org.jetbrains.kotlin.fir.backend.Fir2IrDeclarationStorage
import org.jetbrains.kotlin.fir.languageVersionSettings
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.getRegularClassSymbolByClassId
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirCachingCompositeSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithName
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.isAnnotation
import org.jetbrains.kotlin.ir.util.isTopLevel
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.javac.resolve.classId
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.utils.addToStdlib.runIf

class FirClrBuiltinProviderActualDeclarationExtractor private constructor(
	private val provider: FirSymbolProvider,
	private val classifierStorage: Fir2IrClassifierStorage,
	private val declarationStorage: Fir2IrDeclarationStorage,
) : IrExtraActualDeclarationExtractor() {
	companion object {
		val ActualizeByClrBuiltinProviderFqName: FqName = classId("kotlin.internal", "ActualizeByClrBuiltinProvider").asSingleFqName()

		fun initializeIfNeeded(platformComponents: Fir2IrComponents): IrExtraActualDeclarationExtractor? {
			val session = platformComponents.session
			return runIf(session.languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation)) {
				val dependencyProviders = (session.symbolProvider as FirCachingCompositeSymbolProvider).providers
				val firClrActualizingBuiltinSymbolProvider =
					dependencyProviders.filterIsInstance<ClrActualizingBuiltinSymbolProvider>().single()
				FirClrBuiltinProviderActualDeclarationExtractor(
					firClrActualizingBuiltinSymbolProvider.builtinsSymbolProvider,
					platformComponents.classifierStorage,
					platformComponents.declarationStorage
				)
			}
		}
	}

	override fun extract(expectIrClass: IrClass): IrClassSymbol? {
		if (!expectIrClass.hasActualizeByClrBuiltinProviderFqNameAnnotation()) return null

		val regularClassSymbol = classifierStorage.session.getRegularClassSymbolByClassId(expectIrClass.classIdOrFail) ?: return null
		return classifierStorage.getIrClassSymbol(regularClassSymbol)
	}

	private fun IrClass.hasActualizeByClrBuiltinProviderFqNameAnnotation(): Boolean {
		if (annotations.any { it.isAnnotation(ActualizeByClrBuiltinProviderFqName) }) return true
		return parentClassOrNull?.hasActualizeByClrBuiltinProviderFqNameAnnotation() == true
	}

	override fun extract(expectTopLevelCallables: List<IrDeclarationWithName>, expectCallableId: CallableId): List<IrSymbol> {
		require(expectTopLevelCallables.all { it.isTopLevel })

		if (expectTopLevelCallables.none { expectCallable ->
				expectCallable.annotations.any { it.isAnnotation(ActualizeByClrBuiltinProviderFqName) }
			}
		) {
			return emptyList()
		}

		return provider.getTopLevelCallableSymbols(expectCallableId.packageName, expectCallableId.callableName).mapNotNull {
			when (it) {
				is FirPropertySymbol -> declarationStorage.getIrPropertySymbol(it)
				is FirFunctionSymbol<*> -> declarationStorage.getIrFunctionSymbol(it)
				else -> null
			}
		}
	}
}