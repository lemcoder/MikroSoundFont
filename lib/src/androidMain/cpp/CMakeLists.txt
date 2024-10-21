cmake_minimum_required(VERSION 3.10)

# Project name
project(core_native_lib)

# Add the shared native library
add_library(
        mikroSoundFontJNI                 # Name of the native library
        SHARED
        ${CMAKE_SOURCE_DIR}/mikro_sound_font_jni.c
)

# Include the shared header files from the project root's 'native/include' directory
include_directories(
        ${CMAKE_SOURCE_DIR}/../../../../native/src
)

# Find necessary libraries, e.g., the Android log library
find_library(
        log-lib
        log
)

# Link your native library against Android log library (or other required libraries)
target_link_libraries(
        mikroSoundFontJNI
        ${log-lib}
)
