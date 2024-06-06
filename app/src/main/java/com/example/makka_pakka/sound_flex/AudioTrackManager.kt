package szu.stclkhlww.shushengyiqi.playsound

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

object AudioTrackManager {
    //    系统的音频播放器
    private var audio: AudioTrack? = null

    //    放音标记
    private var is_playing = false
    private val wave_producer = WaveProducer()

    //    开始播放高频声波
    fun startPlaying() {
//        创建播放器对象
        if (audio == null) {
            audio = AudioTrack(
                AudioManager.STREAM_MUSIC,
                WaveProducer.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                4 * WaveProducer.SAMPLE_RATE,
                AudioTrack.MODE_STATIC
            )
            wave_producer.prepare()
        }
        if (is_playing) {
            return
        }
        //        将WaveProducer的数据写入播放器, 播放高频声波
        audio!!.write(wave_producer.tone, 0, 4 * WaveProducer.SAMPLE_RATE)
        audio!!.setLoopPoints(0, 2 * WaveProducer.SAMPLE_RATE, -1)
        audio!!.play()
        is_playing = true
    }

    //    结束, 停播
    fun release() {
        if (is_playing) {
            audio!!.pause()
            audio!!.stop()
            is_playing = false
            audio!!.release()
        }
        audio = null
    }
}
