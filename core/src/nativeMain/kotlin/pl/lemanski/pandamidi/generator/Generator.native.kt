package pl.lemanski.pandamidi.generator

import pl.lemanski.mikroSoundFont.MikroSoundFont
import pl.lemanski.mikroSoundFont.SoundFont

internal object MinGWGenerator : Generator {
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_COUNT = 2
    private const val SAMPLE_BLOCK_SIZE = 410
    private lateinit var soundFont: SoundFont

    override fun setSoundFont(path: String) {
        soundFont = MikroSoundFont.load(path)
        soundFont.setBankPreset(9, 128, 0)
        soundFont.setOutput(SoundFont.OutputMode.TSF_STEREO_INTERLEAVED, SAMPLE_RATE, 0.0f)
    }

    override fun generate(midiMessage: MidiMessage): FloatArray {
        var currentTime = 0.0
        var audioBuffer = FloatArray(0)
        var message: MidiMessage? = midiMessage

        while (message != null) {
            message.process()
            message = message.next

            val targetTime = message?.time?.toDouble() ?: currentTime

            while (targetTime >= currentTime) {
                currentTime += SAMPLE_BLOCK_SIZE * (1000.0 / SAMPLE_RATE)
                audioBuffer += soundFont.renderFloat(SAMPLE_BLOCK_SIZE, false)
            }
        }

        return audioBuffer
    }

    private fun MidiMessage.process() {
        when (this) {
            is MidiMessageNoteOff -> soundFont.noteOff(channel, key)
            is MidiMessageNoteOn -> soundFont.noteOn(channel, key, velocity / 127.0f)

//            is MidiMessageControlChange -> tsf_channel_midi_control(
//                soundFont?.reinterpret(),
//                channel.toInt(),
//                control.toInt(),
//                controlValue.toInt()
//            )
//            is MidiMessagePitchBend     -> tsf_channel_set_pitchwheel(
//                soundFont?.reinterpret(),
//                channel.toInt(),
//                pitchBend.toInt()
//            )
//            is MidiMessageProgramChange -> tsf_channel_set_presetnumber(
//                soundFont?.reinterpret(),
//                channel.toInt(),
//                program.toInt(),
//                (channel == 9u).toByte().toInt()
//            )
            else -> throw Exception("Unsupported command!")
        }
    }
}

actual fun getGenerator(): Generator = MinGWGenerator