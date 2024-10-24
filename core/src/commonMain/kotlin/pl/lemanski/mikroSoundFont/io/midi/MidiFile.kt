package pl.lemanski.mikroSoundFont.io.midi

import pl.lemanski.mikroSoundFont.midi.MidiMessage
import pl.lemanski.mikroSoundFont.midi.MidiMessageType
import pl.lemanski.mikroSoundFont.midi.MidiTrack

class MidiFile(
    private val buffer: ByteArray
) {
    val header: MidiFileHeader
    val tracks: Array<MidiTrack>

    init {
        header = MidiFileHeader.fromBytes(buffer)
        var data = buffer.copyOfRange(MidiFileHeader.SIZE_IN_BYTES, buffer.size)
        tracks = Array(header.trackCount) {
            val track = MidiTrack.fromBytes(data)
            data = data.copyOfRange(track.rawData.size + 8, data.size)
            track
        }
    }

    /**
     * Messages are ordered by the absolute time of occurrence
     * meaning that delta times has been converted to absolute times.
     *
     * @return messages parsed from all tracks.
     */
    fun messages(): List<MidiMessage> {
        val actualMessages = mutableListOf<MidiMessage>()
        var totalTicks = 0
        var tempoMsec = 0
        var tempoTicks = 0
        var ticksToTime = 500_000.0 / (1000.0 * header.division)

        for (track in tracks) {
            track.messages.sortedBy { it.time }
            for (msg in track.messages) {
                totalTicks += msg.time
                val actualTime = tempoMsec + ((totalTicks - tempoTicks) * ticksToTime).toInt()
                val actual = msg.copy(time = actualTime)

                if (actual.type is MidiMessageType.SetTempo) {
                    val tempo = actual.type.tempo
                    ticksToTime = (tempo[0].toInt() shl 16 or (tempo[1].toInt() shl 8) or tempo[2].toInt()) / (1000.0 * header.division)
                    tempoMsec = actualTime
                    tempoTicks = totalTicks
                }

                actualMessages.add(actual)
            }
        }

        return actualMessages
    }
}
