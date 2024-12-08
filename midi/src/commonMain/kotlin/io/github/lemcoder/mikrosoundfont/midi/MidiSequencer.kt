package io.github.lemcoder.mikrosoundfont.midi

import io.github.lemcoder.mikrosoundfont.SoundFont

class MidiSequencer(
    private val soundFont: SoundFont,
    private val sampleRate: Int,
    private val channels: Int
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
                audioBuffer += soundFont.renderFloat(sampleBlockSize, channels, false)
            }
        }

        return audioBuffer
    }

    private fun MidiMessage.process() = when (this) {
        is MidiVoiceMessage.NoteOff       -> soundFont.noteOff(channel, key)
        is MidiVoiceMessage.NoteOn        -> soundFont.noteOn(channel, key, velocity / 127.0f)
        is MidiVoiceMessage.PitchBend     -> soundFont.channels[channel].setPitchWheel(pitchBend)
        is MidiVoiceMessage.ProgramChange -> soundFont.channels[channel].setPresetNumber(program, channel == 9)
        is MidiVoiceMessage.ControlChange -> soundFont.channels[channel].setMidiControl(control, controlValue)
        else                              -> {}
    }
}