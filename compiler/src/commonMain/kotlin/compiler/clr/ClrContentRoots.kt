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

package compiler.clr

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.ContentRoot
import org.jetbrains.kotlin.com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

interface ClrContentRootBase : ContentRoot

interface ClrDllRootBase : ClrContentRootBase {
	val isSdkRoot: Boolean
}

interface ClrContentRoot : ClrContentRootBase {
	val file: File
}

data class ClrDllRoot(
	override val file: File,
	override val isSdkRoot: Boolean = false,
) : ClrContentRoot, ClrDllRootBase

@Suppress("unused") // Might be useful for external tools which invoke kotlinc with their own file system, not based on java.io.File.
data class VirtualClrDllRoot(val file: VirtualFile, override val isSdkRoot: Boolean) : ClrDllRootBase {
	constructor(file: VirtualFile) : this(file, false)
}

data class CSharpSourceRoot(override val file: File, val packagePrefix: String?) : ClrContentRoot

val CompilerConfiguration.clrDllRoots: List<File>
	get() = getList(CLIConfigurationKeys.CONTENT_ROOTS).filterIsInstance<ClrDllRoot>().map(ClrContentRoot::file)