package pl.lemanski.mikroSoundFont.midi

data class MidiEvent(
    val deltaTime: Long,
    val eventType: EventType,
    val data: List<Int>
)

enum class EventType {
    NOTE_ON,
    NOTE_OFF,
    PROGRAM_CHANGE,
    CONTROL_CHANGE
}