package dialight.teams.captain;

import dialight.eventhelper.project.ProjectApi;

public class SortByPriorityApi implements ProjectApi {

    private final SortByPriority proj;

    public SortByPriorityApi(SortByPriority proj) {
        this.proj = proj;
    }

}
