package io.github.lemcoder.mikrosoundfont.io.wav

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.writeIntLe
import kotlinx.io.writeShortLe

data class WavFileHeader(
    val chunkID: String = "RIFF",
    val chunkSize: UInt,
    val format: String = "WAVE",
    val subchunk1ID: String = "fmt ",
    val subchunk1Size: UInt = 16u,
    val audioFormat: UShort = 3u, // IEEE float
    val numChannels: UShort,
    val sampleRate: UInt,
    val byteRate: UInt,
    val blockAlign: UShort,
    val bitsPerSample: UShort = 32u,
    val subchunk2ID: String = "data",
    val subchunk2Size: UInt
) {
    companion object {
        fun write(sampleRate: UInt, numSamples: UInt, numChannels: UShort): WavFileHeader {
            return WavFileHeader(
                chunkSize = 36u + numSamples * numChannels * Float.SIZE_BYTES.toUInt(),
                numChannels = numChannels,
                sampleRate = sampleRate,
                byteRate = sampleRate * numChannels * Float.SIZE_BYTES.toUInt(),
                blockAlign = (numChannels * Float.SIZE_BYTES.toUInt()).toUShort(),
                subchunk2Size = numSamples * numChannels * Float.SIZE_BYTES.toUInt()
            )
        }
    }
}

fun WavFileHeader.toByteArray(): ByteArray {
    return Buffer()
        .apply {
            write(chunkID.encodeToByteArray(), 0, 4)
            writeIntLe(chunkSize.toInt())
            write(format.encodeToByteArray(), 0, 4)
            write(subchunk1ID.encodeToByteArray(), 0, 4)
            writeIntLe(subchunk1Size.toInt())
            writeShortLe(audioFormat.toShort())
            writeShortLe(numChannels.toShort())
            writeIntLe(sampleRate.toInt())
            writeIntLe(byteRate.toInt())
            writeShortLe(blockAlign.toShort())
            writeShortLe(bitsPerSample.toShort())
            write(subchunk2ID.encodeToByteArray(), 0, 4)
            writeIntLe(subchunk2Size.toInt())
        }.readByteArray()
}