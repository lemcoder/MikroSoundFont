package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.SoundFont
import pl.lemanski.mikroSoundFont.getLogger

class MidiSequencer(
    private val soundFont: SoundFont,
    private val sampleRate: Int,
    private val channelCount: Int,
    private val sampleBlockSize: Int
) {
    private val logger = getLogger()
    private var currentTime = 0L
    private val events = mutableListOf<MidiMessage>()

    fun loadMidiEvents(midiEvents: List<MidiMessage>) {
        events.clear()
        events.addAll(midiEvents)
    }

    fun generate(midiMessage: MidiMessage): FloatArray {
        var currentTime = 0.0
        var audioBuffer = FloatArray(0)
        val i = events.iterator()
        var message: MidiMessage? = if (i.hasNext()) i.next() else return floatArrayOf()

        while (i.hasNext()) {
            message?.process()
            message = i.next()

            val targetTime = message.time.toDouble()

            while (targetTime >= currentTime) {
                currentTime += sampleBlockSize * (1000.0 / sampleRate)
                audioBuffer += soundFont.renderFloat(sampleBlockSize, false)
            }
        }

        return audioBuffer
    }

    private fun MidiMessage.process() {
        when (this) {
            is MidiMessageNoteOff -> soundFont.noteOff(channel, key)
            is MidiMessageNoteOn  -> soundFont.noteOn(channel, key, velocity / 127.0f)
            else                  -> logger.log("Unknown message type ${type.name}")
        }
    }
}