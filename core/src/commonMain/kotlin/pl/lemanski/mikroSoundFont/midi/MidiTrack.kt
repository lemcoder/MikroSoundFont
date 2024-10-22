package pl.lemanski.mikroSoundFont.midi

data class MidiTrack(
    val id: Int,
    val end: Int,
    val ticks: Int
)