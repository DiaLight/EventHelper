package dialight.scheduler

class Delta(private val ticker: Ticker) {

    private var last = 0

    fun check() = ticker.ticks - last

    fun point(): Int {
        val delta = check()
        last = ticker.ticks
        return delta
    }

}