package pl.lemanski.mikroSoundFont.io

import kotlin.experimental.and

/**
 * Convert byte array to float array with little endian order
 */
fun ByteArray.toFloatArray(): FloatArray {
    val floatArray = FloatArray(size / Float.SIZE_BYTES)
    for (i in floatArray.indices) {
        val intBits = ((this[i * Float.SIZE_BYTES + 3].toInt() and 0xFF) shl 24) or
                ((this[i * Float.SIZE_BYTES + 2].toInt() and 0xFF) shl 16) or
                ((this[i * Float.SIZE_BYTES + 1].toInt() and 0xFF) shl 8) or
                (this[i * Float.SIZE_BYTES].toInt() and 0xFF)
        floatArray[i] = Float.fromBits(intBits)
    }
    return floatArray
}

/**
 * Convert float array with little endian order to byte array
 */
fun FloatArray.toByteArrayLittleEndian(): ByteArray {
    val byteArray = ByteArray(size * Float.SIZE_BYTES)
    for (i in indices) {
        val intBits = this[i].toRawBits()
        byteArray[i * Float.SIZE_BYTES] = (intBits and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 1] = ((intBits shr 8) and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 2] = ((intBits shr 16) and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 3] = ((intBits shr 24) and 0xFF).toByte()
    }
    return byteArray
}

fun FloatArray.toByteArrayBigEndian(): ByteArray {
    val byteArray = ByteArray(size * Float.SIZE_BYTES)
    for (i in indices) {
        val intBits = this[i].toRawBits()
        byteArray[i * Float.SIZE_BYTES] = ((intBits shr 24) and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 1] = ((intBits shr 16) and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 2] = ((intBits shr 8) and 0xFF).toByte()
        byteArray[i * Float.SIZE_BYTES + 3] = (intBits and 0xFF).toByte()
    }
    return byteArray
}


fun ByteArray.readShortAt(index: Int): Short {
    val shortBuffer = this.copyOfRange(index, index + 2).map { it.toInt() and 0xFF }
    return (shortBuffer[1] or (shortBuffer[0] shl 8)).toShort()
}

fun ByteArray.readIntAt(index: Int): Int {
    val intBuffer = this.copyOfRange(index, index + 4).map { it.toInt() and 0xFF }
    return intBuffer[3] or (intBuffer[2] shl 8) or (intBuffer[1] shl 16) or (intBuffer[0] shl 24)
}

fun ByteArray.readByteAt(index: Int): Byte {
    return this[index] and 0xFF.toByte()
}