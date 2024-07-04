package pl.lemanski.pandamidi.sequencer

import pl.lemanski.pandamidi.core.TimeSignature
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence
import javax.sound.midi.Track

class MidiSequencer {
    private var timeSignature: TimeSignature = TimeSignature(4, 4)
    private val ticksPerQuarterNote = 32
    private val sequence: Sequence = Sequence(Sequence.PPQ, ticksPerQuarterNote)
    private val track: Track = sequence.createTrack()
    private val sequencer = MidiSystem.getSequencer()

    fun addEvent(event: MidiEvent) {
        track.add(event)
    }

    fun addMidiNote(midiNote: MidiNote, measureNumber: Int, beat: Int = 0) {
        assert(measureNumber >= 0)
        assert(beat >= 0)

        val initialTick = (ticksPerQuarterNote * beat).toLong() // FIXME

        val midiEventNoteOn = MidiEvent(midiNote.messages.first, initialTick)
        val midiEventNoteOff = MidiEvent(midiNote.messages.second, initialTick + (midiNote.timeDivision.value.toLong() * ticksPerQuarterNote) + 1)

        addEvent(midiEventNoteOn)
        addEvent(midiEventNoteOff)
    }

    fun setTempo(bpm: Int) {
        sequencer.tempoInBPM = bpm.toFloat()
    }

    fun setTimeSignature(timeSignature: TimeSignature) {
        this.timeSignature = timeSignature
    }

    fun play() {
        sequencer.open()
        sequencer.setSequence(sequence)
        sequencer.start()

        while (sequencer.isRunning) {
            Thread.sleep(1000)
        }

        sequencer.close()
    }
}