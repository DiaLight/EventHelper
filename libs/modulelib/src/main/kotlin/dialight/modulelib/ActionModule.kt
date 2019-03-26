package dialight.modulelib

abstract class ActionModule(
    override val id: String,
    override val name: String
) : Module {

    override var enabled: Boolean
        get() = false
        set(value) {
            if(value) {
                onAction()
            }
        }

    abstract fun onAction(): Boolean

    override fun enable(): Boolean {
        enabled = true
        return true
    }

    override fun disable() = false


}