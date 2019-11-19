package dialight.eventhelper.database;

import dialight.eventhelper.project.ProjectApi;

public class DatabaseApi implements ProjectApi {

    private final Database proj;

    public DatabaseApi(Database proj) {
        this.proj = proj;
    }

}
