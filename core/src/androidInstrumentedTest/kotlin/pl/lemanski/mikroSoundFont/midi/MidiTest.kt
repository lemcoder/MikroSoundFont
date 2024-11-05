package pl.lemanski.mikroSoundFont.midi

import android.Manifest
import android.os.Environment
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import pl.lemanski.mikroSoundFont.MikroSoundFont
import pl.lemanski.mikroSoundFont.io.midi.MidiFileParser
import pl.lemanski.mikroSoundFont.io.saveFile
import pl.lemanski.mikroSoundFont.io.toByteArrayLittleEndian
import pl.lemanski.mikroSoundFont.io.wav.WavFileHeader
import pl.lemanski.mikroSoundFont.io.wav.toByteArray
import java.io.File


class MidiTest {

    @JvmField
    @Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    @Test
    fun testMidi() {
        val context = InstrumentationRegistry.getInstrumentation().context
        Log.e("test", "1")
        val path = "${Environment.getExternalStorageDirectory().absolutePath}/Download/"

        val midi = context.resources.assets.open("gmajor.mid")
        val sf = context.resources.assets.open("font.sf2")

        val midiBuffer = midi.readBytes()
        val sfBuffer = sf.readBytes()

        val midiFile = MidiFileParser(midiBuffer).parse()
        val messages = midiFile.getMessagesOverTime()

        val soundFont = MikroSoundFont.load(sfBuffer)

        val sequencer = MidiSequencer(soundFont, 44_100)
        sequencer.loadMidiEvents(messages)
        val wavBytes = sequencer.generate()

        val wavHeader = WavFileHeader.write(44_100u, wavBytes.size.toUInt(), 2u)
        val file = wavHeader.toByteArray() + wavBytes.toByteArrayLittleEndian()

        File("$path/output.wav").delete()
        saveFile(file, "$path/output.wav")
    }
}