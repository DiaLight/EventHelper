package dialight.inject;

import dialight.compatibility.TeamBc8;
import dialight.nms.ReflectionUtils;
import dialight.observable.ObservableObject;
import dialight.observable.WriteProxyObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.observable.set.ObservableSetWrapper;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Consumer;

public class TeamInject8 extends TeamInject {

    private static Class<?> cl_CraftTeam;
    private static Constructor<Team> m_CraftTeam_init;
    private static Field f_CraftTeam_team;

    private static Field f_ScoreboardTeam_members;
    private static Field f_ScoreboardTeam_eh_setDisplayName;
    private static Field f_ScoreboardTeam_eh_setPrefix;
    private static Field f_ScoreboardTeam_eh_setSuffix;
    private static Field f_ScoreboardTeam_eh_setAllowFriendlyFire;
    private static Field f_ScoreboardTeam_eh_setCanSeeFriendlyInvisibles;
    private static Field f_ScoreboardTeam_eh_setNameTagVisibility;
    private static Field f_ScoreboardTeam_eh_setCollisionRule;

//    private static Method m_ScoreboardTeam_setDisplayName_orig;
//    private static Method m_ScoreboardTeam_setPrefix_orig;
//    private static Method m_ScoreboardTeam_setSuffix_orig;
//    private static Method m_ScoreboardTeam_setAllowFriendlyFire_orig;
//    private static Method m_ScoreboardTeam_setCanSeeFriendlyInvisibles_orig;
//    private static Method m_ScoreboardTeam_setCollisionRule_orig;
//    private static Method m_ScoreboardTeam_setNameTagVisibility_orig;

    static {
        try {
            cl_CraftTeam = Class.forName("org.bukkit.craftbukkit." + ReflectionUtils.SERVER_VERSION + ".scoreboard.CraftTeam", false, CraftScoreboard.class.getClassLoader());

            m_CraftTeam_init = (Constructor<Team>) cl_CraftTeam.getDeclaredConstructor(CraftScoreboard.class, ScoreboardTeam.class);
            m_CraftTeam_init.setAccessible(true);

            f_CraftTeam_team = cl_CraftTeam.getDeclaredField("team");
            f_CraftTeam_team.setAccessible(true);

            f_ScoreboardTeam_members = ScoreboardTeam.class.getDeclaredField("c");
            f_ScoreboardTeam_members.setAccessible(true);

            // *** hooks ***
            ScoreboardTeam.class.getDeclaredField("eh_patched");  // require class to be patched
            f_ScoreboardTeam_eh_setDisplayName = ScoreboardTeam.class.getDeclaredField("eh_setDisplayName");
            f_ScoreboardTeam_eh_setPrefix = ScoreboardTeam.class.getDeclaredField("eh_setPrefix");
            f_ScoreboardTeam_eh_setSuffix = ScoreboardTeam.class.getDeclaredField("eh_setSuffix");
            f_ScoreboardTeam_eh_setAllowFriendlyFire = ScoreboardTeam.class.getDeclaredField("eh_setAllowFriendlyFire");
            f_ScoreboardTeam_eh_setCanSeeFriendlyInvisibles = ScoreboardTeam.class.getDeclaredField("eh_setCanSeeFriendlyInvisibles");
            f_ScoreboardTeam_eh_setNameTagVisibility = ScoreboardTeam.class.getDeclaredField("eh_setNameTagVisibility");
            f_ScoreboardTeam_eh_setCollisionRule = ScoreboardTeam.class.getDeclaredField("eh_setCollisionRule");

//            m_ScoreboardTeam_setDisplayName_orig = ScoreboardTeam.class.getDeclaredMethod("setDisplayName_orig");
//            m_ScoreboardTeam_setPrefix_orig = ScoreboardTeam.class.getDeclaredMethod("setPrefix_orig");
//            m_ScoreboardTeam_setSuffix_orig = ScoreboardTeam.class.getDeclaredMethod("setSuffix_orig");
//            m_ScoreboardTeam_setAllowFriendlyFire_orig = ScoreboardTeam.class.getDeclaredMethod("setAllowFriendlyFire_orig");
//            m_ScoreboardTeam_setCanSeeFriendlyInvisibles_orig = ScoreboardTeam.class.getDeclaredMethod("setCanSeeFriendlyInvisibles_orig");
//            m_ScoreboardTeam_setNameTagVisibility_orig = ScoreboardTeam.class.getDeclaredMethod("setNameTagVisibility_orig");
//            m_ScoreboardTeam_setCollisionRule_orig = ScoreboardTeam.class.getDeclaredMethod("b_orig");
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> members_orig;
    private ObservableSet<String> members;
    private final ObservableObject<String> setDisplayName = new ObservableObject<>(null);
    private final WriteProxyObservableObject<String> setDisplayName_api = new WriteProxyObservableObject<>(setDisplayName);
    private final ObservableObject<String> setPrefix = new ObservableObject<>(null);
    private final WriteProxyObservableObject<String> setPrefix_api = new WriteProxyObservableObject<>(setPrefix);
    private final ObservableObject<String> setSuffix = new ObservableObject<>(null);
    private final WriteProxyObservableObject<String> setSuffix_api = new WriteProxyObservableObject<>(setSuffix);
    private final ObservableObject<Boolean> setAllowFriendlyFire = new ObservableObject<>(null);
    private final WriteProxyObservableObject<Boolean> setAllowFriendlyFire_api = new WriteProxyObservableObject<>(setAllowFriendlyFire);
    private final ObservableObject<Boolean> setCanSeeFriendlyInvisibles = new ObservableObject<>(null);
    private final WriteProxyObservableObject<Boolean> setCanSeeFriendlyInvisibles_api = new WriteProxyObservableObject<>(setCanSeeFriendlyInvisibles);
    private final ObservableObject<NameTagVisibility> setNameTagVisibility = new ObservableObject<>(null);
    private final WriteProxyObservableObject<NameTagVisibility> setNameTagVisibility_api = new WriteProxyObservableObject<>(setNameTagVisibility);
    private final ObservableObject<NameTagVisibility> setCollisionRule = new ObservableObject<>(null);
    private final WriteProxyObservableObject<NameTagVisibility> setCollisionRule_api = new WriteProxyObservableObject<>(setCollisionRule);
    private final ObservableObject<ChatColor> color = new ObservableObject<>(null);
    private final WriteProxyObservableObject<ChatColor> color_api = new WriteProxyObservableObject<>(color);

    public TeamInject8(Team team) {
        super(team);
        setDisplayName_api.onProxyChange(this, (prev, value) -> getNms().setDisplayName(value));
        setPrefix_api.onProxyChange(this, (prev, value) -> getNms().setPrefix(value));
        setSuffix_api.onProxyChange(this, (prev, value) -> getNms().setSuffix(value));
        setAllowFriendlyFire_api.onProxyChange(this, (prev, value) -> getNms().setAllowFriendlyFire(value));
        setCanSeeFriendlyInvisibles_api.onProxyChange(this, (prev, value) -> getNms().setCanSeeFriendlyInvisibles(value));
        setNameTagVisibility_api.onProxyChange(this, (prev, value) -> getNms().setNameTagVisibility(bukkitToNotch(value)));
        setCollisionRule_api.onProxyChange(this, (prev, value) -> getNms().b(bukkitToNotch(value)));
        color_api.onProxyChange(this, (prev, value) -> getNms().setPrefix(TeamBc8.rstripColor(getNms().getPrefix()) + value.toString()));
        setPrefix.onChange(this, (prev, value) -> color.setValue(TeamBc8.parseColor(value)));
    }

    @Override public Object getHandle() {
        try {
            return f_CraftTeam_team.get(this.team);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ScoreboardTeam getNms() {
        return (ScoreboardTeam) getHandle();
    }

    @Override public boolean inject() {
        ScoreboardTeam team = getNms();
        try {
            members_orig = (Set<String>) f_ScoreboardTeam_members.get(team);
            members = new ObservableSetWrapper<>(members_orig);
            f_ScoreboardTeam_members.set(team, members);

            setDisplayName.setValue(team.getDisplayName());
            setPrefix.setValue(team.getPrefix());
            setSuffix.setValue(team.getSuffix());
            setAllowFriendlyFire.setValue(team.allowFriendlyFire());
            setCanSeeFriendlyInvisibles.setValue(team.canSeeFriendlyInvisibles());
            setNameTagVisibility.setValue(notchToBukkit(team.getNameTagVisibility()));
            setCollisionRule.setValue(notchToBukkit(team.j()));

            f_ScoreboardTeam_eh_setDisplayName.set(team, (Consumer<String>) setDisplayName::setValue);  // Consumer<String>
            f_ScoreboardTeam_eh_setPrefix.set(team, (Consumer<String>) setPrefix::setValue);  // Consumer<String>
            f_ScoreboardTeam_eh_setSuffix.set(team, (Consumer<String>) setSuffix::setValue);  // Consumer<String>
            f_ScoreboardTeam_eh_setAllowFriendlyFire.set(team, (Consumer<Boolean>) setAllowFriendlyFire::setValue);  // Consumer<Boolean>
            f_ScoreboardTeam_eh_setCanSeeFriendlyInvisibles.set(team, (Consumer<Boolean>) setCanSeeFriendlyInvisibles::setValue);  // Consumer<Boolean>
            f_ScoreboardTeam_eh_setNameTagVisibility.set(team, (Consumer<ScoreboardTeamBase.EnumNameTagVisibility>) vis -> setNameTagVisibility.setValue(notchToBukkit(vis)));  // Consumer<EnumNameTagVisibility>
            f_ScoreboardTeam_eh_setCollisionRule.set(team, (Consumer<ScoreboardTeamBase.EnumNameTagVisibility>) vis -> setCollisionRule.setValue(notchToBukkit(vis)));  // Consumer<EnumNameTagVisibility>

            setDisplayName.setValue(getNms().getDisplayName());
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

//    private void setDisplayName(String displayName) {
//        try {
//            m_ScoreboardTeam_setDisplayName_orig.invoke(getNms(), displayName);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override public boolean uninject() {
        ScoreboardTeam team = getNms();
        try {
            f_ScoreboardTeam_members.set(team, members_orig);
            members.clear();

            f_ScoreboardTeam_eh_setDisplayName.set(team, null);
            f_ScoreboardTeam_eh_setPrefix.set(team, null);
            f_ScoreboardTeam_eh_setSuffix.set(team, null);
            f_ScoreboardTeam_eh_setAllowFriendlyFire.set(team, null);
            f_ScoreboardTeam_eh_setCanSeeFriendlyInvisibles.set(team, null);
            f_ScoreboardTeam_eh_setNameTagVisibility.set(team, null);
            f_ScoreboardTeam_eh_setCollisionRule.set(team, null);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public ObservableSet<String> getMembers() {
        return members;
    }

    @Override public ObservableObject<String> displayName() {
        return setDisplayName_api;
    }

    @Override public ObservableObject<String> prefix() {
        return setPrefix_api;
    }

    @Override public ObservableObject<String> suffix() {
        return setSuffix_api;
    }

    @Override public ObservableObject<Boolean> allowFriendlyFire() {
        return setAllowFriendlyFire_api;
    }

    @Override public ObservableObject<Boolean> canSeeFriendlyInvisibles() {
        return setCanSeeFriendlyInvisibles_api;
    }

    @Override public ObservableObject<NameTagVisibility> nameTagVisibility() {
        return setNameTagVisibility_api;
    }

    @Override public ObservableObject<NameTagVisibility> collisionRule() {
        return setCollisionRule_api;
    }

    @Override public ObservableObject<ChatColor> color() {
        return color_api;
    }

    public static ScoreboardTeamBase.EnumNameTagVisibility bukkitToNotch(NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
            case NEVER:
                return ScoreboardTeamBase.EnumNameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }
    public static NameTagVisibility notchToBukkit(ScoreboardTeamBase.EnumNameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return NameTagVisibility.ALWAYS;
            case NEVER:
                return NameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return NameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    public static Team toBukkit(CraftScoreboard scoreboard, ScoreboardTeam team) {
        try {
            return m_CraftTeam_init.newInstance(scoreboard, team);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
