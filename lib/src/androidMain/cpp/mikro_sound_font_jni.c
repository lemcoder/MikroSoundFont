#include <jni.h>

#define TSF_IMPLEMENTATION

#include "tsf.h"

// Declare global tsf (TinySoundFont) object
static struct tsf *g_tsf = NULL;

// Function to reset the SoundFont (stop all notes, reset parameters)
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_reset(JNIEnv *env, jobject obj) {
    if (g_tsf) {
        tsf_reset(g_tsf);
    }
}

// Get preset index
JNIEXPORT jint JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_getPresetIndex(JNIEnv *env, jobject obj, jint bank, jint presetNumber) {
    if (g_tsf) {
        return tsf_get_presetindex(g_tsf, bank, presetNumber);
    }
    return -1;
}

// Get number of presets
JNIEXPORT jint JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_getPresetsCount(JNIEnv *env, jobject obj) {
    if (g_tsf) {
        return tsf_get_presetcount(g_tsf);
    }
    return 0;
}

// Get the name of a preset by index
JNIEXPORT jstring JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_getPresetName(JNIEnv *env, jobject obj, jint presetIndex) {
    if (g_tsf) {
        const char *name = tsf_get_presetname(g_tsf, presetIndex);
        return (*env)->NewStringUTF(env, name);
    }
    return (*env)->NewStringUTF(env, "");
}

// Set output parameters
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_setOutput(JNIEnv *env, jobject obj, jobject outputMode, jint sampleRate, jfloat globalGainDb) {
    if (g_tsf) {
        jclass outputModeClass = (*env)->GetObjectClass(env, outputMode);

        jmethodID ordinalMethod = (*env)->GetMethodID(env, outputModeClass, "ordinal", "()I");
        jint ordinal = (*env)->CallIntMethod(env, outputMode, ordinalMethod);
        enum TSFOutputMode outMode;
        switch (ordinal) {
            case 0:
                outMode = TSF_STEREO_INTERLEAVED;
            case 1:
                outMode = TSF_STEREO_UNWEAVED;
            default:
                outMode = TSF_MONO;  // Default or error handling
        }

        tsf_set_output(g_tsf, outMode, sampleRate, globalGainDb);
    }
}

// Set volume
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_setVolume(JNIEnv *env, jobject obj, jfloat globalGain) {
    if (g_tsf) {
        tsf_set_volume(g_tsf, globalGain);
    }
}

// Set max voices
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_setMaxVoices(JNIEnv *env, jobject obj, jint maxVoices) {
    if (g_tsf) {
        tsf_set_max_voices(g_tsf, maxVoices);
    }
}

// Start playing a note
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_noteOn(JNIEnv *env, jobject obj, jint presetIndex, jint key, jfloat velocity) {
    if (g_tsf) {
        tsf_note_on(g_tsf, presetIndex, key, velocity);
    }
}

// Stop playing a note
JNIEXPORT void JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_noteOff(JNIEnv *env, jobject obj, jint presetIndex, jint key) {
    if (g_tsf) {
        tsf_note_off(g_tsf, presetIndex, key);
    }
}

JNIEXPORT jfloatArray JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_renderFloat(JNIEnv *env, jobject obj, jint samples, jboolean isMixing) {
    if (g_tsf) {
        int item_count = samples * 2;
        float *buffer = malloc(item_count * sizeof(float) * 2);
        tsf_render_float(g_tsf, buffer, samples, 0);

        jfloatArray output = (*env)->NewFloatArray(env, item_count);
        (*env)->SetFloatArrayRegion(env, output, 0, item_count, buffer);
        free(buffer);

        return output;
    }
    return NULL;
}

JNIEXPORT jboolean JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_loadMemory(JNIEnv *env, jobject thiz, jbyteArray memory, jint size) {
    jbyte *memoryBuffer = (*env)->GetByteArrayElements(env, memory, NULL);

    if (g_tsf) {
        tsf_close(g_tsf);
    }

    g_tsf = tsf_load_memory(memoryBuffer, size);

    (*env)->ReleaseByteArrayElements(env, memory, memoryBuffer, 0);

    return g_tsf != NULL ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_loadFilename(JNIEnv *env, jobject thiz, jstring path) {
    const char *filePath = (*env)->GetStringUTFChars(env, path, NULL);

    if (g_tsf) {
        tsf_close(g_tsf);  // Close the existing SoundFont if loaded
    }

    g_tsf = tsf_load_filename(filePath);

    (*env)->ReleaseStringUTFChars(env, path, filePath);

    return g_tsf != NULL ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_bankNoteOn(JNIEnv *env, jobject thiz, jint bank, jint preset_number, jint key, jfloat velocity) {
    if (g_tsf) {
        tsf_bank_note_on(g_tsf, bank, preset_number, key, velocity);
    }
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_bankNoteOff(JNIEnv *env, jobject thiz, jint bank, jint preset_number, jint key) {
    if (g_tsf) {
        tsf_bank_note_off(g_tsf, bank, preset_number, key);
    }
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_noteOffAll(JNIEnv *env, jobject thiz) {
    if (g_tsf) {
        tsf_note_off_all(g_tsf);
    }
}

JNIEXPORT jint JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_activeVoiceCount(JNIEnv *env, jobject thiz) {
    if (g_tsf) {
        return tsf_active_voice_count(g_tsf);
    }
    return 0;  // Return 0 if no active SoundFont
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_setBankPreset(JNIEnv *env, jobject thiz, jint channel, jint bank, jint preset_number) {
    if (g_tsf) {
        tsf_channel_set_bank_preset(g_tsf, channel, bank, preset_number);
    }
}

JNIEXPORT jstring JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_bankGetPresetName(JNIEnv *env, jobject thiz, jint bank, jint preset_number) {
    if (g_tsf) {
        const char *presetName = tsf_bank_get_presetname(g_tsf, bank, preset_number);
        if (presetName) {
            return (*env)->NewStringUTF(env, presetName);
        }
    }
    return (*env)->NewStringUTF(env, "");  // Return empty string if not found
}

// ----------------
// Channel methods
// ----------------

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setPresetIndex(JNIEnv *env, jobject thiz, jint channel, jint preset_index) {
    tsf_channel_set_presetindex(g_tsf, channel, preset_index);
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setPresetNumber(JNIEnv *env, jobject thiz, jint channel, jint preset_number, jboolean is_midi_drums) {
    tsf_channel_set_presetnumber(g_tsf, channel, preset_number, is_midi_drums);
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setBank(JNIEnv *env, jobject thiz, jint channel, jint bank) {
    tsf_channel_set_bank(g_tsf, channel, bank);
}

JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setBankPreset(JNIEnv *env, jobject thiz, jint channel, jint bank, jint preset_number) {
    tsf_channel_set_bank_preset(g_tsf, channel, bank, preset_number);
}

// Set the pan (stereo position) for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setPan(JNIEnv *env, jobject thiz, jint channel, jfloat pan) {
    tsf_channel_set_pan(g_tsf, channel, pan);
}

// Set the volume (gain) for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setVolume(JNIEnv *env, jobject thiz, jint channel, jfloat volume) {
    tsf_channel_set_volume(g_tsf, channel, volume);
}

// Set the pitch wheel position for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setPitchWheel(JNIEnv *env, jobject thiz, jint channel, jint pitch_wheel) {
    tsf_channel_set_pitchwheel(g_tsf, channel, pitch_wheel);
}

// Get the pitch wheel position for the channel
JNIEXPORT jint JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPitchWheel(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_pitchwheel(g_tsf, channel);
}

// Get the pitch range for the channel
JNIEXPORT jfloat JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPitchRange(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_pitchrange(g_tsf, channel);
}

// Get the tuning for the channel
JNIEXPORT jfloat JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getTuning(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_tuning(g_tsf, channel);
}

// Set a MIDI control for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setMidiControl(JNIEnv *env, jobject thiz, jint channel, jint control, jint control_value) {
    tsf_channel_midi_control(g_tsf, channel, control, control_value);
}

// Set the pitch range for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setPitchRange(JNIEnv *env, jobject thiz, jint channel, jfloat pitch_range) {
    tsf_channel_set_pitchrange(g_tsf, channel, pitch_range);
}

// Set the tuning for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_setTuning(JNIEnv *env, jobject thiz, jint channel, jfloat tuning) {
    tsf_channel_set_tuning(g_tsf, channel, tuning);
}

// Turn on a note for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_noteOn(JNIEnv *env, jobject thiz, jint channel, jint key, jfloat velocity) {
    tsf_channel_note_on(g_tsf, channel, key, velocity);
}

// Turn off a note for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_noteOff(JNIEnv *env, jobject thiz, jint channel, jint key) {
    tsf_channel_note_off(g_tsf, channel, key);
}

// Turn off all notes for the channel
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_noteOffAll(JNIEnv *env, jobject thiz, jint channel) {
    tsf_channel_note_off_all(g_tsf, channel);
}

// Turn off all sounds for the channel (includes sustained notes)
JNIEXPORT void JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_soundsOffAll(JNIEnv *env, jobject thiz, jint channel) {
    tsf_channel_sounds_off_all(g_tsf, channel);
}

// Get the preset index for the channel
JNIEXPORT jint JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPresetIndex(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_preset_index(g_tsf, channel);
}

// Get the preset bank for the channel
JNIEXPORT jint JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPresetBank(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_preset_bank(g_tsf, channel);
}

// Get the preset number for the channel
JNIEXPORT jint JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPresetNumber(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_preset_number(g_tsf, channel);
}

// Get the pan (stereo position) for the channel
JNIEXPORT jfloat JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getPan(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_pan(g_tsf, channel);
}

// Get the volume (gain) for the channel
JNIEXPORT jfloat JNICALL
Java_pl_lemanski_mikroSoundFont_internal_ChannelDelegate_getVolume(JNIEnv *env, jobject thiz, jint channel) {
    return tsf_channel_get_volume(g_tsf, channel);
}