package szu.stclkhlww.shushengyiqi.playsound

/*
产生声波信息的工具类
 */
class WaveProducer {
    val tone = ByteArray(4 * 44100) //ByteArray(4 * DEFAULT_SAMPLE_RATE_IN_HZ)

    // 是否初始化过
    private var isPrepare = false

    // 生成正弦波
    fun prepare() {
        if (isPrepare) {
            return
        }
        //        for (i in 0 until 2 * DEFAULT_SAMPLE_RATE_IN_HZ) {
//            val cosWav = (WAV_RANGE * cos(2 * PI * START_FREQ * i / DEFAULT_SAMPLE_RATE_IN_HZ)).toInt()
//            cosWavBuffer[2 * i] = (cosWav and 0x00ff).toByte()
//            cosWavBuffer[2 * i + 1] = ((cosWav and 0xff00).ushr(8)).toByte()
//        }
        for (i in 0 until 2 * SAMPLE_RATE) {
            val cos_wave = (WAV_RANGE * Math.cos(2 * Math.PI * FREQUENCY * i / SAMPLE_RATE)).toInt()
            tone[2 * i] = (cos_wave and 0xff).toByte()
            tone[2 * i + 1] = (cos_wave and 0xff00 ushr 8).toByte()
        }
        isPrepare = true
    }

    companion object {
        //    采样率44100Hz
        const val SAMPLE_RATE = 44100

        //    频率19kHz
        const val FREQUENCY = 19000

        // 正弦波的振幅
        private const val WAV_RANGE = Short.MAX_VALUE
    }
}
