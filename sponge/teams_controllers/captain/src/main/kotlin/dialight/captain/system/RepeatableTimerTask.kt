package dialight.captain.system

import org.spongepowered.api.scheduler.Task
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class RepeatableTimerTask(val plugin: Any, val name: String) : Consumer<Task> {

    private val tickListeners = ArrayList<(index: Int, max: Int) -> Unit>()
    private val finishListeners = ArrayList<() -> Unit>()

    private var task: Task? = null
    var running = false
        private set
    private var limit = 10
    var index = 0


    fun onTick(op: (index: Int, limit: Int) -> Unit) {
        tickListeners += op
    }
    fun onFinish(op: () -> Unit) {
        finishListeners += op
    }

    override fun accept(t: Task) {
        if(index > limit || !running) {
            t.cancel()
            running = false
            task = null
            for(op in finishListeners) op()
            return
        }
        for(op in tickListeners) op(index, limit)
        index++
    }

    fun stop() {
        val task = this.task ?: return
        task.cancel()
        running = false
        this.task = null
        for(op in finishListeners) op()
    }

    fun start(limit: Int) {
        stop()
        this.index = 0
        this.running = true
        this.task = Task.builder().run {
            name(name)
            interval(1, TimeUnit.SECONDS)
            execute(this@RepeatableTimerTask)
            submit(plugin)
        }
        this.limit = limit
    }

}
