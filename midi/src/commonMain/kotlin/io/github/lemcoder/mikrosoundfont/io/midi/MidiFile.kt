package io.github.lemcoder.mikrosoundfont.io.midi

import io.github.lemcoder.mikrosoundfont.midi.MidiMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiMetaMessage
import io.github.lemcoder.mikrosoundfont.midi.MidiTrack
import io.github.lemcoder.mikrosoundfont.midi.UnsupportedMidiMessage

data class MidiFile(
    val header: Header,
    val tracks: List<MidiTrack>
) {
    data class Header(
        val trackCount: Int,
        val division: Int,
    )

    fun getMessagesOverTime(): List<MidiMessage> {
        val division = header.division
        var ticks2time = 500000 / (1000.0 * division)

        val allMessages = mutableListOf<MidiMessage>()

        for (track in tracks) {
            var trackTicks = 0

            for (msg in track.messages) {
                if (msg is UnsupportedMidiMessage) continue
                trackTicks += msg.time

                val millis = trackTicks * ticks2time
                msg.time = millis.toInt()

                if (msg is MidiMetaMessage.SetTempo) {
                    val tempo = msg.tempo
                    ticks2time = tempo / (1000.0 * division)
                }

                allMessages.add(msg)
            }
        }

        return allMessages.sortedBy { it.time }
    }
}