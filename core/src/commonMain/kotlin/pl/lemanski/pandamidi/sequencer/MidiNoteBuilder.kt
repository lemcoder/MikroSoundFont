package pl.lemanski.pandamidi.sequencer

import pl.lemanski.pandamidi.core.Note
import javax.sound.midi.ShortMessage

class MidiNoteBuilder {
    operator fun invoke(
        note: Note,
        timeDivision: TimeDivision,
        velocity: Velocity = Velocity.MAX,
        channel: Int = 1
    ): MidiNote {
        val noteOn = ShortMessage().apply { setMessage(MidiEventType.NOTE_ON.code, channel, note.value, velocity.value) }
        val noteOff = ShortMessage().apply { setMessage(MidiEventType.NOTE_OFF.code, channel, note.value, velocity.value) }

        return MidiNote(
            messages = noteOn to noteOff,
            timeDivision = timeDivision
        )
    }
}