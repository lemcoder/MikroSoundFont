package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.SoundFont

class MidiSequencer(
    private val soundFont: SoundFont
) {
    private var currentTime = 0L
    private val events = mutableListOf<MidiEvent>()

    fun loadMidiEvents(midiEvents: List<MidiEvent>) {
        events.clear()
        events.addAll(midiEvents)
    }

    fun play() {
        events.forEach { event ->
            currentTime += event.deltaTime
            handleEvent(event)
        }
    }

    private fun handleEvent(event: MidiEvent) {
        when (event.eventType) {
            EventType.NOTE_ON        -> soundFont.noteOn(
                event.data[0],
                event.data[1],
                event.data[2] / 127.0f
            )
            EventType.NOTE_OFF       -> soundFont.noteOff(event.data[0], event.data[1])
            EventType.PROGRAM_CHANGE -> soundFont.setBankPreset(
                0,
                event.data[0],
                event.data[1]
            )
            EventType.CONTROL_CHANGE -> handleControlChange(
                event.data[0],
                event.data[1],
                event.data[2]
            )
        }
    }

    private fun handleControlChange(controller: Int, value: Int, channel: Int) {
        // Handle pitch bends, volume controls, etc.
    }
}