package compiler.clr.backend.ir

import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOriginImpl

/**
 * CLR平台特定的声明来源。
 * 
 * 这些原点标识在IR生成过程中由CLR后端创建的声明。
 */
object ClrDeclarationOrigin {
    /**
     * 表示CLR后端合成的内置类型声明。
     */
    val SYNTHETIC_CLR_BUILTIN = IrDeclarationOriginImpl("SYNTHETIC_CLR_BUILTIN")
    
    /**
     * 表示从.NET元数据生成的声明。
     */
    val CLR_METADATA = IrDeclarationOriginImpl("CLR_METADATA")
    
    /**
     * 表示为CLR互操作生成的桥接方法或属性。
     */
    val CLR_BRIDGE = IrDeclarationOriginImpl("CLR_BRIDGE")
} 