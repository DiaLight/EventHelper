package dialight.freezer;

import org.bukkit.entity.Player;

public class FreezeReason {

    private final Player invoker;
    private final String reason;

    public FreezeReason(Player invoker, String reason) {
        this.invoker = invoker;
        this.reason = reason;
    }

    public Player getInvoker() {
        return invoker;
    }

    public String getReason() {
        return reason;
    }

}
