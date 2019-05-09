package dialight.teleporter;

import dialight.eventhelper.project.ProjectApi;

public class TeleporterApi implements ProjectApi {

    private final Teleporter proj;

    public TeleporterApi(Teleporter proj) {
        this.proj = proj;
    }

}
