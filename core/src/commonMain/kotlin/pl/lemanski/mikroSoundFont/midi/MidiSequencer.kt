package pl.lemanski.mikroSoundFont.midi

import pl.lemanski.mikroSoundFont.SoundFont

class MidiSequencer(
    private val soundFont: SoundFont,
    private val sampleRate: Int,
) {
    private val sampleBlockSize: Int = 512
    private val messages = mutableListOf<MidiMessage>()

    fun loadMidiEvents(midiMessages: List<MidiMessage>) {
        messages.clear()
        messages.addAll(midiMessages)
    }

    fun generate(): FloatArray {
        var currentTime = 0
        var audioBuffer = FloatArray(0)

        for (i in 0..<messages.size) {
            messages[i].process()
            val targetTime = messages.getOrNull(i + 1)?.time ?: currentTime

            while (targetTime > currentTime) {
                currentTime += (sampleBlockSize * (1000.0 / sampleRate)).toInt()
                audioBuffer += soundFont.renderFloat(sampleBlockSize, false)
            }
        }

        return audioBuffer
    }

    private fun MidiMessage.process() = when (this) {
        is MidiVoiceMessage.NoteOff       -> soundFont.channels[channel].noteOff(key)
        is MidiVoiceMessage.NoteOn        -> soundFont.channels[channel].noteOn(key, if (velocity == 0) 0f else 1f)
        is MidiVoiceMessage.PitchBend     -> soundFont.channels[channel].setPitchWheel(pitchBend)
        is MidiVoiceMessage.ProgramChange -> soundFont.channels[channel].setPresetNumber(program, channel == 9)
        is MidiVoiceMessage.ControlChange -> soundFont.channels[channel].setMidiControl(control, controlValue)
        else                              -> {}
    }
}