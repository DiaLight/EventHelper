package dialight.captain.system

import dialight.captain.system.members.Captain
import dialight.captain.system.members.CSUsers

class CaptainsIterator(val users: CSUsers) {

    var it = users.getCaptains().iterator()

    init {
        users.onAddCaptain { cap ->
            it = users.getCaptains().iterator()
        }
        users.onRemoveCaptain { cap ->
            it = users.getCaptains().iterator()
        }
    }

    fun next(): Captain {
        if(!it.hasNext()) it = users.getCaptains().iterator()
        return it.next()
    }

}