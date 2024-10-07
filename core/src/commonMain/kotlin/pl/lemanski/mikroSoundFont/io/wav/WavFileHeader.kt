package pl.lemanski.mikroSoundFont.io.wav

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

expect fun WavFileHeader.toByteArray(): ByteArray