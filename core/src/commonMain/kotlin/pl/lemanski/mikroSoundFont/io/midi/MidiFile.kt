package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMetaMessage
import pl.lemanski.mikroSoundFont.midi.MidiTrack
import pl.lemanski.mikroSoundFont.midi.UnsupportedMidiMessage

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
        var tempoTicks = 0     // Tick count at the last tempo change
        var tempoMsec = 0      // Milliseconds at the last tempo change

        val allMessages = mutableListOf<MidiMessage>()

        for (track in tracks) {
            var trackTicks = 0

            for (msg in track.messages) {
                if (msg is UnsupportedMidiMessage) continue
                trackTicks += msg.time

                val millis = tempoMsec + ((trackTicks - tempoTicks) * ticks2time).toInt()
                msg.time = millis

                if (msg is MidiMetaMessage.SetTempo) {
                    val tempo = msg.tempo
                    ticks2time = tempo / (1000.0 * division)
                    tempoTicks = trackTicks
                    tempoMsec = millis
                }

                allMessages.add(msg)
            }
        }

        return allMessages.sortedBy { it.time }
    }
}