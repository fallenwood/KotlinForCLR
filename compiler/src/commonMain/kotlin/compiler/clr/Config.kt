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
	val SDK_HOME = CompilerConfigurationKey.create<File>("sdk home")
	val NO_SDK = CompilerConfigurationKey.create<Boolean>("no sdk")
}