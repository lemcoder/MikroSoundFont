package pl.lemanski.pandamidi.io

import kotlin.experimental.and

data class WavFileHeader(
    val riff: ByteArray,
    val fLength: Int,
    val wave: ByteArray,
    val format: ByteArray,
    val chunkSize: Int,
    val formatTag: Short,
    val numberOfChannels: Short,
    val sampleRate: Int,
    val bytesPerSecond: Int,
    val bytesPerSample: Short,
    val bitsPerSample: Short,
    val data: ByteArray,
    val dLength: Int,
) {
    fun toByteArray(): ByteArray {
        return riff +
                fLength.toLittleEndianBytes() +
                wave +
                format +
                chunkSize.toLittleEndianBytes() +
                formatTag.toLittleEndianBytes() +
                numberOfChannels.toLittleEndianBytes() +
                sampleRate.toLittleEndianBytes() +
                bytesPerSecond.toLittleEndianBytes() +
                bytesPerSample.toLittleEndianBytes() +
                bitsPerSample.toLittleEndianBytes() +
                data +
                dLength.toLittleEndianBytes()
    }

    private fun Int.toLittleEndianBytes(): ByteArray {
        return byteArrayOf(
            (this and 0xFF).toByte(),
            ((this shr 8) and 0xFF).toByte(),
            ((this shr 16) and 0xFF).toByte(),
            ((this shr 24) and 0xFF).toByte()
        )
    }

    private fun Short.toLittleEndianBytes(): ByteArray {
        return byteArrayOf(
            (this and 0xFF).toByte(),
            ((this.toInt() shr 8) and 0xFF).toByte()
        )
    }
}
