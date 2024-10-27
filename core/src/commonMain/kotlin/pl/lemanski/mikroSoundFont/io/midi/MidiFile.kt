package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.midi.MidiTrack

data class MidiFile(
    val header: Header,
    val tracks: List<MidiTrack>
) {
    data class Header(
        val trackCount: Int,
        val division: Int,
    )
}