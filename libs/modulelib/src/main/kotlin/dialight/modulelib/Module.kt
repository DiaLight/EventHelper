package dialight.modulelib

abstract class Module(val id: String) {

    protected abstract fun onEnable()
    protected abstract fun onDisable()

}