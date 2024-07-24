import pl.lemanski.pandamidi.io.WavFile
import pl.lemanski.pandamidi.io.WavFileHeader
import pl.lemanski.pandamidi.io.save
import kotlin.math.PI
import kotlin.math.cos

fun main() {
    val MIDDLE_C = 256.0
    val sampleRate = 8000
    val durationSeconds = 10
    val bufferSize = sampleRate * durationSeconds
    val buffer = ShortArray(bufferSize)
    val bytesPerSample = 16

    val headerLength = 44 // ???

    for (i in 0 until bufferSize) {
        buffer[i] = (cos(2.0 * PI * MIDDLE_C * i / sampleRate) * 1000).toInt().toShort()
    }

    val wavHeader = WavFileHeader(
        riff = "RIFF".encodeToByteArray(),
        fLength = bufferSize * bytesPerSample + headerLength,
        wave = "WAVE".encodeToByteArray(),
        format = "fmt ".encodeToByteArray(),
        chunkSize = 16,
        formatTag = 1,
        numberOfChannels = 1,
        sampleRate = sampleRate,
        bytesPerSecond = sampleRate * 16 / 8 * 1,
        bytesPerSample = (16 / 8 * 1).toShort(),
        bitsPerSample = 16,
        data = "data".encodeToByteArray(),
        dLength = bufferSize * bytesPerSample,
    )

    val file = WavFile(
        header = wavHeader,
        data = buffer.map { it.toByte() }.toByteArray()
    )

    file.save("test.wav")
}