package dialight.freezer;

import dialight.eventhelper.project.ProjectApi;
import dialight.misc.ActionInvoker;
import dialight.misc.player.UuidPlayer;

import java.util.Collection;

public class FreezerApi implements ProjectApi {

    private final Freezer proj;

    public FreezerApi(Freezer proj) {
        this.proj = proj;
    }

    public void register(Collection<Frozen> frozens) {
        this.proj.register(frozens);
    }
    public void unregister(ActionInvoker invoker, Collection<UuidPlayer> targets) {
        this.proj.unregister(invoker, targets);
    }

    public void unregisterAll(ActionInvoker invoker) {
        this.proj.unregisterAll(invoker);
    }

}
