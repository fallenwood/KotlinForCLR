package compiler.clr.frontend

import compiler.clr.NodeAssembly
import org.jetbrains.kotlin.fir.FirSessionComponent
import org.jetbrains.kotlin.fir.NoMutableState

@NoMutableState
class FirClrAssemblyProvider internal constructor(val assemblies: Map<String, NodeAssembly>) : FirSessionComponent

@NoMutableState 
class FirClrTargetProvider internal constructor() : FirSessionComponent 