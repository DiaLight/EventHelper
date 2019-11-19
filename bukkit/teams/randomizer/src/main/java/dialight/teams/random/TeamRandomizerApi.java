package dialight.teams.random;

import dialight.eventhelper.project.ProjectApi;

public class TeamRandomizerApi implements ProjectApi {

    private final TeamRandomizerProject proj;

    public TeamRandomizerApi(TeamRandomizerProject proj) {
        this.proj = proj;
    }

}
