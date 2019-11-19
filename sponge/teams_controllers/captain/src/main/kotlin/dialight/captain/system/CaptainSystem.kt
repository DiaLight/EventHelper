package dialight.captain.system

import dialight.captain.CaptainMessages
import dialight.captain.CaptainPlugin
import dialight.captain.CaptainTool
import dialight.captain.system.arena.CSArena
import dialight.captain.system.members.Captain
import dialight.captain.system.members.Member
import dialight.captain.system.members.CSUsers
import dialight.extensions.getOrNull
import dialight.extensions.set
import dialight.freezer.Freezer
import dialight.scheduler.Ticker
import dialight.teams.Server_getScoreboard
import dialight.teams.mixin.removeMemberFromTeams
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockState
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.inventory.property.InventoryCapacity
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import org.spongepowered.common.item.inventory.util.InventoryUtil
import java.util.*

class CaptainSystem(
    val plugin: CaptainPlugin
) {

    var running = false
        private set

    private val users = CSUsers(plugin)
    private val arena = CSArena(users)

    private var sessionManager = SelectPlayerSession(plugin, users)
    private val captainsIterator = CaptainsIterator(users)
    val current: Captain
        get() = sessionManager.captain

    private val ticker = Ticker(plugin.container)
    private val listener = CaptainSystemListener(plugin, sessionManager, users, ticker)
    private val savedInvs = HashMap<UUID, Map<Int, ItemStackSnapshot>>()

    init {
        sessionManager.onFinishSession(this::onSessionFinish)
    }

    fun start(): Boolean {
        if(users.getCaptains().isEmpty()) return false
        users.broadcastMessage(CaptainMessages.initializing)
        val sb = Server_getScoreboard()
        for(usr in users.getUnselected()) {
            sb.removeMemberFromTeams(usr.name)
        }
        for(cap in users.getCaptains()) {
            cap.team?.addMember(Text.of(cap.name))
        }
        arena.build()
        for(usr in users) {
            usr.user?.let { user ->
                val inv = user.inventory
//                val capacity = inv.getInventoryProperty(InventoryCapacity::class.java).getOrNull()?.value!!
                val save = HashMap<Int, ItemStackSnapshot>()
                for(slot in inv.slots<Inventory>()) {
                    val item = slot.peek().getOrNull() ?: continue
                    val index = slot.getInventoryProperty(SlotIndex::class.java).getOrNull()?.value ?: continue
                    save[index] = item.createSnapshot()
                }
                savedInvs[user.uniqueId] = save
                inv.clear()
            }
            plugin.freezer.freezer.invoke(plugin.container, usr.resolveLocation(), Freezer.Action.FREEZE, usr.uuid)
            plugin.offlinelib.shadow.createShadow(usr.uuid, usr.name)
            plugin.teleporter.teleport(plugin.container, usr.uuid, usr.cs_location)
        }
        ticker.start()
        Sponge.getEventManager().registerListeners(plugin, listener)
        next()
        running = true
        return true
    }

    fun next() {
        val captain = captainsIterator.next()
        listener.clearLast(arena.getSearchLocation().extent)
        users.broadcastMessage(CaptainMessages.nextCaptain(captain.name, captain.textColor))
        plugin.teleporter.teleport(plugin.container, captain.uuid, arena.active)
        captain.player?.let { player ->
            plugin.toollib.giveTool(player, CaptainTool.ID)
        }
        sessionManager.start(captain, 20)
    }

    private fun onSessionFinish(captain: Captain, member: Member?) {
        plugin.teleporter.teleport(plugin.container, captain.uuid, captain.cs_location)
        captain.user?.let { user ->
            user.inventory.clear()
        }

        val selected = member ?: users.chooseRandom()
        users.select(captain, selected)
        if(member != null) {
            users.broadcastMessage(CaptainMessages.captainSelected(captain.name, selected.name, captain.textColor))
        } else {
            users.broadcastMessage(CaptainMessages.captainRandomSelected(captain.name, selected.name, captain.textColor))
        }

        selected.cs_location.add(.0, -1.0, .0).block = BlockState.builder()
            .blockType(BlockTypes.WOOL)
            .add(Keys.DYE_COLOR, captain.dyeColor)
            .build()

        if(users.hasUnselected()) {
            next()
        } else {
            finish()
        }
    }

    fun finish() {
        users.broadcastMessage(CaptainMessages.finish)
        arena.clear()
        for(usr in users) {
            plugin.offlinelib.shadow.removeShadow(usr.uuid)
            plugin.freezer.freezer.invoke(plugin.container, usr.resolveLocation(), Freezer.Action.UNFREEZE, usr.uuid)
            usr.user?.let { user ->
                savedInvs[user.uniqueId]?.let { saved ->
                    user.inventory.clear()
                    for((index, item) in saved) {
                        user.inventory[index] = item.createStack()
                    }
                }
                user.player.ifPresent { player ->
                    player.offer(Keys.GLOWING, false)
                }
            }
        }
        users.clear()
        ticker.stop()
        sessionManager.interrupt()
        Sponge.getEventManager().unregisterListeners(listener)
        running = false
    }

    fun setArenaLocation(location: Location<World>) { arena.maybeLoc = location }
    fun addMember(player: Player) = users.add(player)
    fun addMember(user: User) = users.add(user)
    fun addCaptain(player: Player, team: Team) = users.addCaptain(player, team)
    fun addCaptain(user: User, team: Team) = users.addCaptain(user, team)
    fun select(player: Player) = select(player.uniqueId)
    fun select(uuid: UUID): Boolean {
        val member = users.getUnselected(uuid) ?: return false
        sessionManager.select(member)
        return true
    }

    fun getCaptain(player: Player) = users.getCaptain(player)


}