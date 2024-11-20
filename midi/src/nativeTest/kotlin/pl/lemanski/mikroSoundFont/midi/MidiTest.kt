package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.MikroSoundFont
import pl.lemanski.mikroSoundFont.io.loadFile
import pl.lemanski.mikroSoundFont.io.midi.MidiFileParser
import pl.lemanski.mikroSoundFont.io.saveFile
import pl.lemanski.mikroSoundFont.io.toByteArrayLittleEndian
import pl.lemanski.mikroSoundFont.io.wav.WavFileHeader
import pl.lemanski.mikroSoundFont.io.wav.toByteArray
import kotlin.test.Test

class MidiTest {
    private val dir = "C:\\Users\\Mikolaj\\Desktop\\midi"

    @Test
    fun testMidi() {
        val midiFileBuffer = loadFile("$dir\\Gmajor.mid")
        val midiFile = MidiFileParser(midiFileBuffer).parse()
        val messages = midiFile.getMessagesOverTime()

        val soundFont = MikroSoundFont.load("$dir\\font.sf2")

        val sequencer = MidiSequencer(soundFont, 44_100)
        sequencer.loadMidiEvents(messages)
        val wavBytes = sequencer.generate()
        println(wavBytes.size)

        val wavHeader =  WavFileHeader.write(44_100u, wavBytes.size.toUInt(), 2u)
        val file = wavHeader.toByteArray() + wavBytes.toByteArrayLittleEndian()

        saveFile(file, "$dir\\output.wav")
    }
}