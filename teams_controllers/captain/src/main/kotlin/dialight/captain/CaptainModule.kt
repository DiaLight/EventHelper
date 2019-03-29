package dialight.captain

import dialight.modulelib.AbstractModule
import org.spongepowered.api.Sponge

class CaptainModule(val plugin: CaptainPlugin) : AbstractModule(CaptainModule.ID, "Captain") {

    companion object {
        val ID = "captain"
    }

    override fun onEnable(): Boolean {
        // start system
        return true
    }

    override fun onDisable(): Boolean {
        // interrupt system
        return true
    }

}