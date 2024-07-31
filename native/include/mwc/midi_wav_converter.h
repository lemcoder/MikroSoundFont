#include <stddef.h>

void generate(const char *midiFilePath, const char *outputWavPath);

unsigned char *generate_from_midi_buffer(const unsigned char *midiBuffer, size_t midiBufferSize, size_t *outputSize);

int initialize_soundfont(const char *soundFontPath);

void release_soundfont();