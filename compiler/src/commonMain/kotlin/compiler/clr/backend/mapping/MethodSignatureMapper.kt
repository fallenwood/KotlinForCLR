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

package compiler.clr.backend.mapping

import compiler.clr.backend.ClrBackendContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.isVararg

/**
 * CLR平台的方法签名映射器
 * 
 * 负责将Kotlin IR函数签名映射到CLR平台方法签名
 */
class MethodSignatureMapper(
    private val context: ClrBackendContext,
    private val typeMapper: IrTypeMapper
) {

    /**
     * 映射函数名
     */
    fun mapFunctionName(function: IrFunction): String {
        return function.name.asString()
    }

    /**
     * 映射函数签名
     */
    fun mapSignature(function: IrFunction): String {
        val returnType = typeMapper.mapReturnType(function.returnType)
        val params = function.valueParameters.joinToString(", ") { mapParameter(it) }
        val name = mapFunctionName(function)
        
        return "$returnType $name($params)"
    }

    /**
     * 映射参数
     */
    private fun mapParameter(parameter: IrValueParameter): String {
        val type = typeMapper.mapType(parameter.type)
        val name = parameter.name.asString()
        
        val typeModifier = if (parameter.isVararg) "params " else ""
        val nullModifier = if (parameter.type.isNullable()) "?" else ""
        
        return "$typeModifier$type$nullModifier $name"
    }
}