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

// Render audio (this is a simplified example)
JNIEXPORT jfloatArray JNICALL Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_renderFloat(JNIEnv *env, jobject obj, jint samples, jboolean isMixing) {
    if (g_tsf) {
        float buffer[samples];
        tsf_render_float(g_tsf, buffer, samples, isMixing);

        jfloatArray output = (*env)->NewFloatArray(env, samples);
        (*env)->SetFloatArrayRegion(env, output, 0, samples, buffer);
        return output;
    }
    return NULL;
}

JNIEXPORT jboolean JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_loadMemory(JNIEnv *env, jobject thiz, jbyteArray memory, jint size) {
    // Get pointer to the memory array
    jbyte *memoryBuffer = (*env)->GetByteArrayElements(env, memory, NULL);

    if (g_tsf) {
        tsf_close(g_tsf);  // Close the existing SoundFont if loaded
    }

    // Load the SoundFont from memory
    g_tsf = tsf_load_memory(memoryBuffer, size);

    // Release the memory array
    (*env)->ReleaseByteArrayElements(env, memory, memoryBuffer, 0);

    // Return true if the SoundFont was loaded successfully
    return g_tsf != NULL ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_pl_lemanski_mikroSoundFont_internal_SoundFontDelegate_loadFilename(JNIEnv *env, jobject thiz, jstring path) {
    // Convert the jstring path to a C-style string
    const char *filePath = (*env)->GetStringUTFChars(env, path, NULL);

    if (g_tsf) {
        tsf_close(g_tsf);  // Close the existing SoundFont if loaded
    }

    // Load the SoundFont from the file
    g_tsf = tsf_load_filename(filePath);

    // Release the C-style string
    (*env)->ReleaseStringUTFChars(env, path, filePath);

    // Return true if the SoundFont was loaded successfully
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
