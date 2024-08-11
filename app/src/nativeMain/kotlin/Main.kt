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
        time = 10000,
        channel = 5,
        key = 32,
        next = null
    )

    val g = MidiMessageNoteOn(
        time = 2500,
        channel = 5,
        key = 43,
        velocity = 80,
        next = gOff
    )

    val eOff = MidiMessageNoteOff(
        time = 2000,
        channel = 7,
        key = 40,
        next = g
    )

    val e = MidiMessageNoteOn(
        time = 1_000,
        channel = 7,
        key = 40,
        velocity = 80,
        next = eOff
    )

    val cOff = MidiMessageNoteOff(
        time = 100,
        channel = 1,
        key = 36,
        next = e
    )

    val c = MidiMessageNoteOn(
        time = 0,
        channel = 1,
        key = 36,
        velocity = 80,
        next = cOff
    )

    var bytes = ByteArray(0)
    val generator = getGenerator()

    val soundFontPath = Path("D:\\src\\MidiWavConverter\\Example\\florestan-subset.sf2")

    println("1")
    generator.setSoundFont(soundFontPath.toString())
    println("2")
    val midiBytes = generator.generate(c)
    println("3")
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