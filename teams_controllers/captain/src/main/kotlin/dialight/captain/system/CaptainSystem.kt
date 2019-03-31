package dialight.captain.system

import dialight.observable.collection.ObservableCollectionWrapper
import dialight.observable.map.ObservableMapWrapper
import org.spongepowered.api.Sponge
import java.util.*
import kotlin.collections.ArrayList

class CaptainSystem(
    initial_members: MutableMap<UUID, Member> = hashMapOf(),
    initial_teams: MutableCollection<CSTeam> = hashSetOf()
) {

    val members = Members(initial_members)
    val teams = CSTeams(initial_teams)

    val arena = Arena(this)

    class Member(val uuid: UUID, val name: String, var team: String? = null, var isCaptain: Boolean = false) {
        override fun hashCode() = uuid.hashCode()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return uuid == (other as Member).uuid
        }
    }

    class CSTeam(val name: String, val mambers: Set<Member> = hashSetOf()) {
        override fun hashCode() = name.hashCode()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return name == (other as CSTeam).name
        }
    }

    class Members(map: MutableMap<UUID, Member>) : ObservableMapWrapper<UUID, Member>(map)
    class CSTeams(teams: MutableCollection<CSTeam> = hashSetOf()) : ObservableCollectionWrapper<CSTeam>(teams)

    fun collect(): Pair<Collection<Member>, Collection<Member>> {
        for((uuid, m) in members) {
            m.team = null
            m.isCaptain = false
        }
        val toSort = LinkedList<Member>(members.values)
        val captains = ArrayList<Member>()
        val rnd = Random()
        for(team in teams) {
            val m = toSort.removeAt(rnd.nextInt(toSort.size))
            m.team = team.name
            m.isCaptain = true
            captains.add(m)
        }
        return Pair(captains, toSort)
    }

    fun start() {
        arena.build()

    }

    fun interrupt() {
        arena.clear()
    }

}