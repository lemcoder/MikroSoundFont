package pl.lemanski.mikroSoundFont.io.wav

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ByteVarOf
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.UShortVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.plus
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun WavFileHeader.toByteArray(): ByteArray {
    val byteArray = ByteArray(44)

    memScoped {
        val buffer = byteArray.pin().addressOf(0).reinterpret<ByteVar>()

        memcpy(buffer, chunkID.cstr.ptr, 4u)
        buffer.setUIntValueAt(4, chunkSize)
        memcpy(buffer.plus(8), format.cstr.ptr, 4u)
        memcpy(buffer.plus(12), subchunk1ID.cstr.ptr, 4u)
        buffer.setUIntValueAt(16, subchunk1Size)
        buffer.setUShortValueAt(20, audioFormat)
        buffer.setUShortValueAt(22, numChannels)
        buffer.setUIntValueAt(24, sampleRate)
        buffer.setUIntValueAt(28, byteRate)
        buffer.setUShortValueAt(32, blockAlign)
        buffer.setUShortValueAt(34, bitsPerSample)
        memcpy(buffer.plus(36), subchunk2ID.cstr.ptr, 4u)
        buffer.setUIntValueAt(40, subchunk2Size)
    }

    return byteArray
}

@OptIn(ExperimentalForeignApi::class)
private fun <T : ByteVarOf<*>> CPointer<T>.setUIntValueAt(index: Int, value: UInt): Int {
    this.plus(index)?.reinterpret<UIntVar>()?.pointed?.let { it.value = value } ?: return -1
    return 0
}

@OptIn(ExperimentalForeignApi::class)
private fun <T : ByteVarOf<*>> CPointer<T>.setUShortValueAt(index: Int, value: UShort): Int {
    this.plus(index)?.reinterpret<UShortVar>()?.pointed?.let { it.value = value } ?: return -1
    return 0
}