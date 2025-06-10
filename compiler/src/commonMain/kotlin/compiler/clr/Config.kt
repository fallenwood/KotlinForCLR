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

import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

object CLRConfigurationKeys {
	val OUTPUT_DIRECTORY = CompilerConfigurationKey.create<File>("output directory")
	val DOTNET_HOME = CompilerConfigurationKey.create<File>("dotnet home")
	val DOTNET_VERSION = CompilerConfigurationKey.create<String>("dotnet version")
	val ASSEMBLY_RESOLVER = CompilerConfigurationKey.create<File>("assembly resolver")
	val NO_DOTNET = CompilerConfigurationKey.create<Boolean>("no dotnet")
}