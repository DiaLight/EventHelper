package dialight.modulelib

interface Module {

    val id: String
    val name: String
    var enabled: Boolean

    fun enable(): Boolean

    fun disable(): Boolean

    fun toggle() = if(enabled) disable() else enable()


}