package pl.lemanski.pandamidi.sequencer

interface Sequence {
    fun addEvent(event: MidiEvent)
    fun next(): MidiEvent
    fun hasNext(): Boolean
}

class MidiSequence : Sequence {
    private val events = mutableListOf<MidiEvent>()

    override fun addEvent(event: MidiEvent) {
        events.add(event)
    }

    override fun next(): MidiEvent {
        return events.removeFirst()
    }

    override fun hasNext(): Boolean {
        return events.isNotEmpty()
    }
}