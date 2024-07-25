import pl.lemanski.pandamidi.core.Note
import pl.lemanski.pandamidi.io.WavFile
import pl.lemanski.pandamidi.io.WavFileHeader
import pl.lemanski.pandamidi.io.save
import pl.lemanski.pandamidi.sequencer.MidiEvent
import pl.lemanski.pandamidi.sequencer.MidiSequence
import pl.lemanski.pandamidi.sequencer.getSequencer

fun main() {
    val sampleRate = 44100
    val bytesPerSample = 2 // Each sample is 2 bytes (16 bits)
    val headerLength = 44

    val c = MidiEvent(MidiEvent.Type.NOTE_ON, Note(60), 0, 0)
    val e = MidiEvent(MidiEvent.Type.NOTE_ON, Note(64), 0, 0)

    val sequence = MidiSequence()
    sequence.addEvent(c)
    sequence.addEvent(e)

    val sequencer = getSequencer()
    sequencer.setSequence(sequence)

    val buffer = sequencer.nextChunk()
    val bufferSize = buffer.size

    val wavHeader = WavFileHeader(
        riff = "RIFF".encodeToByteArray(),
        fLength = bufferSize * bytesPerSample + headerLength - 8, // fLength is the file size minus 8 bytes
        wave = "WAVE".encodeToByteArray(),
        format = "fmt ".encodeToByteArray(),
        chunkSize = 16,
        formatTag = 1,
        numberOfChannels = 1,
        sampleRate = sampleRate,
        bytesPerSecond = sampleRate * bytesPerSample,
        bytesPerSample = bytesPerSample.toShort(),
        bitsPerSample = 16,
        data = "data".encodeToByteArray(),
        dLength = bufferSize * bytesPerSample
    )

    val byteBuffer = ByteArray(bufferSize * bytesPerSample)
    for (i in buffer.indices) {
        byteBuffer[i * 2] = (buffer[i].toInt() and 0xFF).toByte()
        byteBuffer[i * 2 + 1] = ((buffer[i].toInt() shr 8) and 0xFF).toByte()
    }

    val file = WavFile(
        header = wavHeader,
        data = byteBuffer
    )

    file.save("test.wav")
}