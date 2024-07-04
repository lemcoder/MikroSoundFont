package pl.lemanski.pandamidi.sequencer

import javax.sound.midi.MidiMessage

data class MidiNote(
    val messages: Pair<MidiMessage, MidiMessage>,
    val timeDivision: TimeDivision
)