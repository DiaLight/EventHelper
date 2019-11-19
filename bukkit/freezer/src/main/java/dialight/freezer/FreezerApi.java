package dialight.freezer;

import dialight.eventhelper.project.ProjectApi;

public class FreezerApi implements ProjectApi {

    private final Freezer proj;

    public FreezerApi(Freezer proj) {
        this.proj = proj;
    }

}
