package pl.lemanski.mikroSoundFont.io.midi

data class MidiFileHeader(
    val format: Int,
    val numTracks: Int,
    val division: Int
)