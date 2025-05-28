package compiler.clr.backend.ir

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.fir.backend.Fir2IrComponents
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.impl.IrFactoryImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrClassSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

/**
 * 为CLR后端生成合成的IR类。
 * 
 * 这个类用于在需要特定于CLR平台的内置类型时创建合成IR类表示。
 */
class ClrIrClassGenerator(
    private val components: Fir2IrComponents
) {
    private val syntheticIrClassCache = mutableMapOf<ClassId, IrClass>()
    private val irFactory: IrFactory = IrFactoryImpl

    /**
     * 提供CLR特定的合成IrClass。
     * 这不是为了覆盖基类内置类型如 anyClass，而是为纯粹的合成类型，
     * 这些类型是CLR后端可能需要但不是标准Kotlin内置类型的。
     */
    fun getOrProvideClrSyntheticIrClass(
        classId: ClassId,
        origin: IrDeclarationOrigin = ClrDeclarationOrigin.SYNTHETIC_CLR_BUILTIN,
        modality: Modality = Modality.FINAL,
        kind: ClassKind = ClassKind.CLASS,
        superTypesProvider: () -> List<IrType> = { emptyList() }
    ): IrClass {
        return syntheticIrClassCache.getOrPut(classId) {
            val symbol = IrClassSymbolImpl()
            val syntheticClass = irFactory.createClass(
                startOffset = -1,
                endOffset = -1,
                origin = origin,
                name = classId.shortClassName,
                visibility = DescriptorVisibilities.PUBLIC,
                symbol = symbol,
                modality = modality,
                kind = kind
            ).apply {
                this.superTypes = superTypesProvider()
            }
            symbol.bind(syntheticClass)
            syntheticClass
        }
    }
} 