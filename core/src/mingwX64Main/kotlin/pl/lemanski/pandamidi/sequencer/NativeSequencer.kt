package pl.lemanski.pandamidi.sequencer

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ShortVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.sizeOf
import org.tsf.TSFOutputMode
import org.tsf.tsf_load_filename
import org.tsf.tsf_note_off
import org.tsf.tsf_note_on
import org.tsf.tsf_render_short
import org.tsf.tsf_set_output
import platform.posix.printf

internal class NativeSequencer : Sequencer {
    private var soundFontPath: String = "C:\\Users\\lenovo\\Downloads\\florestan-subset.sf2"
    private lateinit var sequence: Sequence
    private var sampleRate = 8000

    @OptIn(ExperimentalForeignApi::class)
    private var outputMode: TSFOutputMode = TSFOutputMode.TSF_STEREO_INTERLEAVED


    @OptIn(ExperimentalForeignApi::class)
    private val soundFont
        get() = tsf_load_filename(soundFontPath)

    @OptIn(ExperimentalForeignApi::class)
    override fun setSequence(sequence: Sequence) {
        this.sequence = sequence
        tsf_set_output(soundFont, outputMode, sampleRate, 0f);

        while (sequence.hasNext()) {
            val event = sequence.next()
            when (event.type) {
                MidiEvent.Type.NOTE_ON  -> tsf_note_on(soundFont, 0, event.note.value, 100f)
                MidiEvent.Type.NOTE_OFF -> tsf_note_off(soundFont, 0, event.note.value)
            }
        }
    }

    override fun setSoundFont(path: String) {
        soundFontPath = path
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun nextChunk(): ShortArray {
        val soundFont = tsf_load_filename(soundFontPath)
        tsf_set_output(soundFont, TSFOutputMode.TSF_MONO, 44100, 0f) //sample rate
        tsf_note_on(soundFont, 0, 60, 1.0f) //preset 0, middle C
        val halfSecond = nativeHeap.allocArray<ShortVar>(22050)
        tsf_render_short(soundFont, halfSecond, 22050, 0)
        val values = halfSecond.readBytes(22050 * sizeOf<ShortVar>().toInt())

        return byteArrayToShortArray(values)
    }

    fun byteArrayToShortArray(byteArray: ByteArray): ShortArray {
        require(byteArray.size % 2 == 0) { "Byte array length must be even to convert to Short array." }

        val shortArray = ShortArray(byteArray.size / 2)
        for (i in shortArray.indices) {
            val low = byteArray[i * 2].toInt() and 0xFF
            val high = byteArray[i * 2 + 1].toInt() and 0xFF
            shortArray[i] = ((high shl 8) or low).toShort()
        }
        return shortArray
    }
}

actual fun getSequencer(): Sequencer = NativeSequencer()