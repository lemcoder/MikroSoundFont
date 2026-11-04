// stb_vorbis.c is compiled on its own; its declarations turn on TinySoundFont's
// SF3 (Ogg Vorbis compressed samples) support.
#define STB_VORBIS_HEADER_ONLY
#include "stb_vorbis.c"

#define TSF_IMPLEMENTATION
#include "tsf.h"
