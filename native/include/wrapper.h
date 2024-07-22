#include "tsf.h"

TSFDEF tsf* tsf_load_memory(const void* buffer, int size);
TSFDEF void tsf_set_output(tsf* f, enum TSFOutputMode outputmode, int samplerate, float global_gain_db);
TSFDEF int tsf_note_on(tsf* f, int preset_index, int key, float vel);
TSFDEF void tsf_render_short(tsf* f, short* buffer, int samples, int flag_mixing);
TSFDEF tsf* tsf_load_filename(const char* filename);