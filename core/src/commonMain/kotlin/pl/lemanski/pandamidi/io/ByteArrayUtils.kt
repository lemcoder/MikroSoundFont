package pl.lemanski.pandamidi.io

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
