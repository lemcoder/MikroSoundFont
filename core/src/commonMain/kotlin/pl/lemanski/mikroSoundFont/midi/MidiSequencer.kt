package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.SoundFont
import pl.lemanski.mikroSoundFont.getLogger

class MidiSequencer(
    private val soundFont: SoundFont,
    private val sampleRate: Int,
) {
    private val sampleBlockSize: Int = 512
    private val logger = getLogger()
    private var currentTime = 0L
    private val messages = mutableListOf<MidiMessage>()

    fun loadMidiEvents(midiMessages: List<MidiMessage>) {
        messages.clear()
        messages.addAll(midiMessages)
    }

    fun generate(): FloatArray {
        var currentTime = 0.0
        var audioBuffer = FloatArray(0)

        for (msg in messages) {
            msg.process()
            val targetTime = msg.time.toDouble()

            while (targetTime >= currentTime) {
                currentTime += sampleBlockSize * (1000.0 / sampleRate)
                audioBuffer += soundFont.renderFloat(sampleBlockSize, false)
            }
        }

        return audioBuffer
    }

    private fun MidiMessage.process() = when (this) {
        is MidiVoiceMessage.NoteOff       -> soundFont.channels[channel].noteOff(key)
        is MidiVoiceMessage.NoteOn        -> soundFont.channels[channel].noteOn(key, velocity / 127.0f)
        is MidiVoiceMessage.PitchBend     -> soundFont.channels[channel].setPitchWheel(pitchBend)
        is MidiVoiceMessage.ProgramChange -> soundFont.channels[channel].setPresetNumber(program, channel == 9)
        is MidiVoiceMessage.ControlChange -> soundFont.channels[channel].setMidiControl(control, controlValue)
        else                              -> { }
    }
}