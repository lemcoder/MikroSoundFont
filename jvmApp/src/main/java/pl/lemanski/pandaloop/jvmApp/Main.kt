package pl.lemanski.pandaloop.jvmApp

import pl.lemanski.pandamidi.core.ChordBuilder
import pl.lemanski.pandamidi.core.Note
import pl.lemanski.pandamidi.core.dominantSeventh
import pl.lemanski.pandamidi.core.majorSeventh
import pl.lemanski.pandamidi.core.minorSeventh
import pl.lemanski.pandamidi.sequencer.MidiEventType
import pl.lemanski.pandamidi.sequencer.MidiNoteBuilder
import pl.lemanski.pandamidi.sequencer.MidiSequencer
import pl.lemanski.pandamidi.sequencer.TimeDivision
import javax.sound.midi.MidiEvent

fun main() {
    val midiSequencer = MidiSequencer()
    val midiNoteBuilder = MidiNoteBuilder()

    midiSequencer.setTempo(60)

    val dMinorSeventh = ChordBuilder(Note(62)).minorSeventh()

    dMinorSeventh.forEach {
        midiSequencer.addMidiNote(midiNoteBuilder(it, TimeDivision.QUARTER), 0, 0)
    }

    val gDominant = ChordBuilder(Note(67)).dominantSeventh()

    gDominant.forEach {
        midiSequencer.addMidiNote(midiNoteBuilder(it, TimeDivision.QUARTER), 0, 1)
    }

    val cMajorSeventh = ChordBuilder(Note(60)).majorSeventh()

    cMajorSeventh.forEach {
        midiSequencer.addMidiNote(midiNoteBuilder(it, TimeDivision.QUARTER), 0, 2)
    }

    midiSequencer.play()
}