package pl.lemanski.mikroSoundFont.io.wav

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Stack

actual fun WavFileHeader.toByteArray(): ByteArray {
    val byteArray = ByteArray(44) // The size is based on the structure
    val buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)
    buffer.put(chunkID.toByteArray(Charsets.UTF_8), 0, 4)

    buffer.putInt(4, chunkSize.toInt())

    buffer.position(8)
    buffer.put(format.toByteArray(Charsets.UTF_8), 0, 4)

    buffer.position(12)
    buffer.put(subchunk1ID.toByteArray(Charsets.UTF_8), 0, 4)

    buffer.putInt(16, subchunk1Size.toInt())

    buffer.putShort(20, audioFormat.toShort())

    buffer.putShort(22, numChannels.toShort())

    buffer.putInt(24, sampleRate.toInt())

    buffer.putInt(28, byteRate.toInt())

    buffer.putShort(32, blockAlign.toShort())

    buffer.putShort(34, bitsPerSample.toShort())

    buffer.position(36)
    buffer.put(subchunk2ID.toByteArray(Charsets.UTF_8), 0, 4)

    buffer.putInt(40, subchunk2Size.toInt())

    return byteArray
}

fun isValid(s: String): Boolean {
    if (s.length % 2 != 0) {
        return false
    }

    val stack = mutableListOf<Char>()
    s.forEach {
        when (it) {
            '(', '{', '[' -> stack.add(it)
            ')' -> if (stack.lastOrNull() == '(') stack.removeLast() else return false
            '}' -> if (stack.lastOrNull() == '{') stack.removeLast() else return false
            ']' -> if (stack.lastOrNull() == '[') stack.removeLast() else return false
        }
    }

    return stack.size == 0
}