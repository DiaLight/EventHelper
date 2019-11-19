package dialight.teams.priority;

import dialight.eventhelper.project.ProjectApi;

public class TeamPriorityApi implements ProjectApi {

    private final TeamPriorityProject proj;

    public TeamPriorityApi(TeamPriorityProject proj) {
        this.proj = proj;
    }

}
