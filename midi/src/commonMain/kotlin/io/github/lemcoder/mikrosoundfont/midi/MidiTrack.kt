package io.github.lemcoder.mikrosoundfont.midi

data class MidiTrack(
    val header: Header,
    val messages: List<MidiMessage>
) {
    data class Header(
        val dataSize: Int,
    )
}