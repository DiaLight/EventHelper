package dialight.autorespawn;

import dialight.modulelib.Module;

public class AutoRespawnModule extends Module {

    public static final String ID = "autorespawn";

    private final AutoRespawn proj;

    public AutoRespawnModule(AutoRespawn proj) {
        super();
        this.proj = proj;
    }

    @Override
    public String getId() {
        return ID;
    }

}
