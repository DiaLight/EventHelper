package dialight.teams.observable.listener;

import dialight.teams.event.TeamEvent;

public interface TeamHandler {

    void onEvent(TeamEvent event);

    void update();

}
