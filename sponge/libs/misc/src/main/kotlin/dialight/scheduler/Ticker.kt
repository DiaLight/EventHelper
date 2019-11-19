package dialight.scheduler

import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.scheduler.Task
import java.util.*

class Ticker(val plugin: PluginContainer) : Runnable {

    private var task: Task? = null
    var ticks = 0
        private set

    fun start() {
        if(this.task != null) return
        val task = Task.builder()
            .execute(this)
            .intervalTicks(1)
            .submit(plugin)
        this.task = task
    }

    override fun run() {
        ticks++
    }

    fun stop() {
        task?.cancel()
        task = null
    }

}