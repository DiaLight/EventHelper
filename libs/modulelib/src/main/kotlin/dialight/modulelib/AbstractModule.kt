package dialight.modulelib

abstract class AbstractModule(
    override val id: String,
    override val name: String,
    enable: Boolean = false
    ) : Module {

    override var enabled: Boolean = enable
        set(value) {
            if(field == value) return
            if(value) {
                if(onEnable()) field = true
            } else {
                if(onDisable()) field = false
            }
        }

    override fun enable(): Boolean {
        if(enabled) return false
        enabled = true
        return enabled
    }

    override fun disable(): Boolean {
        if(!enabled) return false
        enabled = false
        return !enabled
    }

    abstract fun onEnable(): Boolean
    abstract fun onDisable(): Boolean

}