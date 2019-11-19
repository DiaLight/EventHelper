package dialight.autorespawn;

import dialight.eventhelper.project.ProjectApi;

public class AutoRespawnApi implements ProjectApi {

    private final AutoRespawn proj;

    public AutoRespawnApi(AutoRespawn proj) {
        this.proj = proj;
    }

}
