package dialight.teams.event

import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.impl.AbstractEvent
import org.spongepowered.api.scoreboard.Team

abstract class ScoreboardEvent : AbstractEvent() {

    abstract class Team(val team: org.spongepowered.api.scoreboard.Team) : ScoreboardEvent() {

        private val cause = Cause.builder()
            .append(team)
            .build(EventContext.empty())

        override fun getCause() = cause

        class Add(team: org.spongepowered.api.scoreboard.Team) : Team(team)

        class UpdateInfo(team: org.spongepowered.api.scoreboard.Team) : Team(team)

        class Remove(team: org.spongepowered.api.scoreboard.Team) : Team(team)

    }

    abstract class TeamMember(val team: org.spongepowered.api.scoreboard.Team, val name: String) : ScoreboardEvent() {

        private val cause = Cause.builder()
            .append(team)
            .append(name)
            .build(EventContext.empty())

        override fun getCause() = cause

        class Add(team: org.spongepowered.api.scoreboard.Team, name: String) : TeamMember(team, name)

        class Remove(team: org.spongepowered.api.scoreboard.Team, name: String) : TeamMember(team, name)

    }

}