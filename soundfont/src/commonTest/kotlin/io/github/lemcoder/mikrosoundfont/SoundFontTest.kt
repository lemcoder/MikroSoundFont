package io.github.lemcoder.mikrosoundfont

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Runs every test against the SF2 fixture and its SF3 (Ogg Vorbis) conversion.
 *
 * `florestan-subset.sf2` is TinySoundFont's example font (examples/ in its repository);
 * `florestan-subset.sf3` is made from it with `scripts/make-test-sf3.py`.
 */
abstract class SoundFontTest(private val fixture: String) {
    private val sampleRate = 44100

    private fun load(): SoundFont = MikroSoundFont.load(readTestResource(fixture)).apply {
        setOutput(SoundFont.OutputMode.STEREO_INTERLEAVED, sampleRate, 0f)
    }

    private fun FloatArray.peak(): Float = maxOf { abs(it) }

    @Test
    fun readsPresets() = load().use { soundFont ->
        assertEquals(17, soundFont.getPresetsCount())
        assertEquals("Piano", soundFont.getPresetName(0))
        assertEquals(soundFont.getPresetsCount() + 1, soundFont.channels.size)
    }

    @Test
    fun rendersANote() = load().use { soundFont ->
        assertEquals(0f, soundFont.renderFloat(1024, 2, false).peak(), "silence before any note")
        soundFont.noteOn(presetIndex = 0, key = 60, velocity = 1f)
        assertEquals(1, soundFont.activeVoiceCount())
        val audio = soundFont.renderFloat(sampleRate / 10, 2, false)
        assertEquals(sampleRate / 10 * 2, audio.size)
        assertTrue(audio.peak() > 0.05f, "peak ${audio.peak()}")
    }

    @Test
    fun stereoOutputHonoursPan() = load().use { soundFont ->
        val channel = soundFont.channels[0]
        channel.setPresetIndex(0)
        channel.setPan(0f)
        channel.noteOn(key = 60, velocity = 1f)
        val audio = soundFont.renderFloat(sampleRate / 10, 2, false)
        val left = audio.filterIndexed { i, _ -> i % 2 == 0 }.maxOf { abs(it) }
        val right = audio.filterIndexed { i, _ -> i % 2 == 1 }.maxOf { abs(it) }
        assertTrue(left > 0.05f && right < left / 10, "left $left, right $right")
    }

    @Test
    fun sustainHoldsReleasedNotes() = load().use { soundFont ->
        val channel = soundFont.channels[0]
        channel.setPresetIndex(0)
        channel.setSustain(true)
        channel.noteOn(key = 60, velocity = 1f)
        channel.noteOff(key = 60)
        soundFont.renderFloat(sampleRate * 2, 2, false)
        assertEquals(1, soundFont.activeVoiceCount(), "held by the pedal")

        channel.setSustain(false)
        soundFont.renderFloat(sampleRate * 2, 2, false)
        assertEquals(0, soundFont.activeVoiceCount(), "released with the pedal")
    }

    @Test
    fun closedSoundFontRejectsCalls() {
        val soundFont = load()
        val channel = soundFont.channels[0]
        soundFont.close()
        soundFont.close() // closing twice is harmless
        assertFailsWith<IllegalStateException> { soundFont.getPresetsCount() }
        assertFailsWith<IllegalStateException> { soundFont.renderFloat(16, 2, false) }
        assertFailsWith<IllegalStateException> { channel.noteOn(key = 60, velocity = 1f) }
    }
}

class Sf2Test : SoundFontTest("florestan-subset.sf2")

class Sf3Test : SoundFontTest("florestan-subset.sf3")

class LoadTest {
    @Test
    fun rejectsNonSoundFontData() {
        assertFailsWith<IllegalStateException> { MikroSoundFont.load(ByteArray(64) { it.toByte() }) }
    }
}
