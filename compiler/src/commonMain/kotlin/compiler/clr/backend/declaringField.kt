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

package compiler.clr.backend

import org.jetbrains.kotlin.backend.jvm.JvmLoweredDeclarationOrigin
import org.jetbrains.kotlin.backend.jvm.ir.isJvmInterface
import org.jetbrains.kotlin.builtins.CompanionObjectMapping
import org.jetbrains.kotlin.builtins.isMappedIntrinsicCompanionObjectClassId
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.irAttribute
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.load.java.JavaDescriptorVisibilities
import org.jetbrains.kotlin.load.java.JvmAbi
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.utils.addToStdlib.getOrSetIfNull

private var IrEnumEntry.declaringField: IrField? by irAttribute(followAttributeOwner = false)
private var IrProperty.staticBackingFields: IrField? by irAttribute(followAttributeOwner = false)
private var IrSimpleFunction.staticCompanionDeclarations: Pair<IrSimpleFunction, IrSimpleFunction>? by irAttribute(followAttributeOwner = false)

private var IrSimpleFunction.defaultImplsMethod: IrSimpleFunction? by irAttribute(followAttributeOwner = false)
private var IrClass.defaultImplsClass: IrClass? by irAttribute(followAttributeOwner = false)
private var IrSimpleFunction.defaultImplsRedirection: IrSimpleFunction? by irAttribute(followAttributeOwner = false)
private var IrSimpleFunction.originalFunctionForDefaultImpl: IrSimpleFunction? by irAttribute(followAttributeOwner = false)

private var IrClass.repeatedAnnotationSyntheticContainer: IrClass? by irAttribute(followAttributeOwner = false)

private var IrClass.fieldForObjectInstance: IrField? by irAttribute(followAttributeOwner = false)
private var IrClass.interfaceCompanionFieldForObjectInstance: IrField? by irAttribute(followAttributeOwner = false)

/*
    This class keeps track of singleton fields for instances of object classes.
 */
class CachedFieldsForObjectInstances(
	private val irFactory: IrFactory,
	private val languageVersionSettings: LanguageVersionSettings,
) {
	fun getFieldForObjectInstance(singleton: IrClass): IrField =
		singleton::fieldForObjectInstance.getOrSetIfNull {
			val originalVisibility = singleton.visibility
			val isNotMappedCompanion = singleton.isCompanion && !singleton.isMappedIntrinsicCompanionObject()
			val useProperVisibilityForCompanion =
				languageVersionSettings.supportsFeature(LanguageFeature.ProperVisibilityForCompanionObjectInstanceField)
						&& singleton.isCompanion
						&& !singleton.parentAsClass.isInterface
			irFactory.buildField {
				name = if (isNotMappedCompanion) singleton.name else Name.identifier(JvmAbi.INSTANCE_FIELD)
				type = singleton.defaultType
				origin = IrDeclarationOrigin.FIELD_FOR_OBJECT_INSTANCE
				isFinal = true
				isStatic = true
				visibility = when {
					!useProperVisibilityForCompanion -> DescriptorVisibilities.PUBLIC
					originalVisibility == DescriptorVisibilities.PROTECTED -> JavaDescriptorVisibilities.PROTECTED_STATIC_VISIBILITY
					else -> originalVisibility
				}

			}.apply {
				parent = if (isNotMappedCompanion) singleton.parent else singleton
			}
		}

	private fun IrClass.isMappedIntrinsicCompanionObject() =
		isCompanion && classId?.let { CompanionObjectMapping.isMappedIntrinsicCompanionObjectClassId(it) } == true

	fun getPrivateFieldForObjectInstance(singleton: IrClass): IrField =
		if (singleton.isCompanion && singleton.parentAsClass.isJvmInterface)
			singleton::interfaceCompanionFieldForObjectInstance.getOrSetIfNull {
				irFactory.buildField {
					name = Name.identifier("$\$INSTANCE")
					type = singleton.defaultType
					origin = JvmLoweredDeclarationOrigin.INTERFACE_COMPANION_PRIVATE_INSTANCE
					isFinal = true
					isStatic = true
					visibility = JavaDescriptorVisibilities.PACKAGE_VISIBILITY
				}.apply {
					parent = singleton
				}
			}
		else
			getFieldForObjectInstance(singleton)

}