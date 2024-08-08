import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.sizeOf
import kotlinx.io.Buffer
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import pl.lemanski.pandamidi.generator.MidiMessageNoteOff
import pl.lemanski.pandamidi.generator.MidiMessageNoteOn
import pl.lemanski.pandamidi.generator.getGenerator
import pl.lemanski.pandamidi.io.toByteArrayLittleEndian
import pl.lemanski.pandamidi.io.wav.WavFileHeader
import pl.lemanski.pandamidi.io.wav.toByteArray
import platform.posix.sleep

@OptIn(ExperimentalForeignApi::class)
fun main() {
    val gOff = MidiMessageNoteOff(
        time = 10000u,
        channel = 5u,
        key = 32u,
        next = null
    )

    val g = MidiMessageNoteOn(
        time = 2500u,
        channel = 5u,
        key = 43u,
        velocity = 80u,
        next = gOff
    )

    val eOff = MidiMessageNoteOff(
        time = 2000u,
        channel = 7u,
        key = 40u,
        next = g
    )


    val e = MidiMessageNoteOn(
        time = 1_000u,
        channel = 7u,
        key = 40u,
        velocity = 80u,
        next = eOff
    )

    val cOff = MidiMessageNoteOff(
        time = 100u,
        channel = 1u,
        key = 36u,
        next = e
    )

    val c = MidiMessageNoteOn(
        time = 0u,
        channel = 1u,
        key = 36u,
        velocity = 80u,
        next = cOff
    )

    var bytes = ByteArray(0)
    val generator = getGenerator()

    val soundFontPath = Path("D:\\src\\MidiWavConverter\\Example\\florestan-subset.sf2")

    generator.setSoundFont(soundFontPath.toString())
    val midiBytes = generator.generate(c)
    val numSamples = midiBytes.size.toUInt() / sizeOf<FloatVar>().toUInt()
    val wavFileHeader = WavFileHeader.write(44100u, numSamples, 2u)

    bytes += wavFileHeader.toByteArray()
    bytes += midiBytes.toByteArrayLittleEndian()

    println(bytes.size)

    val file = Buffer()

    file.write(bytes, 0, bytes.size)

    SystemFileSystem.sink(Path("./output.wav")).write(file, file.size)

    sleep(5u)
}