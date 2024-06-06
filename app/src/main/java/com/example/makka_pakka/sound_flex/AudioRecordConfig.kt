package com.example.makka_pakka.sound_flex

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

object AudioRecordConfig {
    val RECORD_SOURCE = MediaRecorder.AudioSource.MIC
    val RECORD_SAMPLE_RATE = 44100
    val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    val RECORD_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    private val min_buffer_size =
        AudioRecord.getMinBufferSize(RECORD_SAMPLE_RATE, CHANNEL_CONFIG, RECORD_FORMAT)

    fun getMinBUfferSize(): Int {
        return min_buffer_size
    }
}