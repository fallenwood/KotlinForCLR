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

namespace kotlin.ranges;

public class CharProgression {
	internal CharProgression(
		char first,
		char endInclusive,
		int step
	) {
		this.first = first;
		this.step = step;
	}
	
	public char first { get; }
	public int step { get; }
}

public class IntProgression {
	internal IntProgression(
		int first,
		int endInclusive,
		int step
	) {
		this.first = first;
		this.step = step;
	}
	
	public int first { get; }
	public int step { get; }
}

public class LongProgression {
	internal LongProgression(
		long first,
		long endInclusive,
		long step
	) {
		this.first = first;
		this.step = step;
	}
	
	public long first { get; }
	public long step { get; }
}