package pl.lemanski.pandamidi.sequencer

import java.lang.foreign.Arena
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout

actual fun strLen(s: String): Long {
    val linker = Linker.nativeLinker()
    val stdlib = linker.defaultLookup()
    val strLenAddress: MemorySegment = stdlib.find("strlen").orElseThrow()

    val descriptor = FunctionDescriptor.of(
        ValueLayout.JAVA_LONG,
        ValueLayout.ADDRESS
    )
    val strlen = linker.downcallHandle(
        strLenAddress,
        descriptor
    )

    Arena.ofConfined().use { offHeap ->
        val funcArg: MemorySegment = offHeap.allocateFrom(s)

        return strlen.invoke(funcArg) as Long
    }
}