package pl.lemanski.pandamidi.sequencer

import pl.lemanski.pandamidi.core.Note

data class MidiEvent(
    val type: Type,
    val note: Note,
    val channel: Int,
    val tick: Int
) {
    enum class Type {
        NOTE_ON,
        NOTE_OFF
    }
}