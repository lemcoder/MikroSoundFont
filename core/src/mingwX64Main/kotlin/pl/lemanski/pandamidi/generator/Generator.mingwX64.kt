package pl.lemanski.pandamidi.generator

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toByte
import tinySoundFont.TSFOutputMode
import tinySoundFont.tsf_channel_midi_control
import tinySoundFont.tsf_channel_note_off
import tinySoundFont.tsf_channel_note_on
import tinySoundFont.tsf_channel_set_bank_preset
import tinySoundFont.tsf_channel_set_pitchwheel
import tinySoundFont.tsf_channel_set_presetnumber
import tinySoundFont.tsf_load_filename
import tinySoundFont.tsf_render_float
import tinySoundFont.tsf_set_output

internal object MinGWGenerator : Generator {
    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_COUNT = 2
    private const val SAMPLE_BLOCK_SIZE = 410

    @OptIn(ExperimentalForeignApi::class)
    private var soundFont: CPointer<*>? = null

    @OptIn(ExperimentalForeignApi::class)
    override fun setSoundFont(path: String) {
        soundFont = tsf_load_filename(path)
        tsf_channel_set_bank_preset(soundFont?.reinterpret(), 9, 128, 0)
        tsf_set_output(soundFont?.reinterpret(), TSFOutputMode.TSF_STEREO_INTERLEAVED, SAMPLE_RATE, 0.0f)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun generate(midiMessage: MidiMessage): FloatArray {
        if (soundFont == null) {
            throw IllegalStateException("SoundFont not set")
        }

        val tmpBuffer = FloatArray(SAMPLE_BLOCK_SIZE * CHANNEL_COUNT)

        var currentTime = 0.0
        var audioBuffer = FloatArray(0)
        var message: MidiMessage? = midiMessage

        while (message != null) {
            message.process()
            message = message.next

            val targetTime = message?.time?.toDouble() ?: currentTime

            while (targetTime >= currentTime) {
                currentTime += SAMPLE_BLOCK_SIZE * (1000.0 / SAMPLE_RATE)
                tsf_render_float(soundFont?.reinterpret(), tmpBuffer.refTo(0), SAMPLE_BLOCK_SIZE, 0)
                audioBuffer += tmpBuffer
            }
        }

        return audioBuffer
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun MidiMessage.process() {
        if (soundFont == null) {
            // TODO add logging
            throw IllegalStateException("SoundFont not set")
        }

        when (this) {
            is MidiMessageControlChange -> tsf_channel_midi_control(
                soundFont?.reinterpret(),
                channel.toInt(),
                control.toInt(),
                controlValue.toInt()
            )
            is MidiMessageNoteOff       -> tsf_channel_note_off(
                soundFont?.reinterpret(),
                channel.toInt(),
                key.toInt()
            )
            is MidiMessageNoteOn        -> tsf_channel_note_on(
                soundFont?.reinterpret(),
                channel.toInt(),
                key.toInt(),
                velocity.toFloat() / 127.0f
            )
            is MidiMessagePitchBend     -> tsf_channel_set_pitchwheel(
                soundFont?.reinterpret(),
                channel.toInt(),
                pitchBend.toInt()
            )
            is MidiMessageProgramChange -> tsf_channel_set_presetnumber(
                soundFont?.reinterpret(),
                channel.toInt(),
                program.toInt(),
                (channel == 9u).toByte().toInt()
            )
        }
    }
}

actual fun getGenerator(): Generator = MinGWGenerator