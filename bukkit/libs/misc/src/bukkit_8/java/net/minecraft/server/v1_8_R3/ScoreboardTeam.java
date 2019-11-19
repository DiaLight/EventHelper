//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class ScoreboardTeam extends ScoreboardTeamBase {
    private final Scoreboard a;
    private final String b;
    private final Set<String> c = Sets.newHashSet();
    private String d;
    private String e = "";
    private String f = "";
    private boolean g = true;
    private boolean h = true;
    private EnumNameTagVisibility i;
    private EnumNameTagVisibility j;
    private EnumChatFormat k;
    public Consumer<String> eh_setDisplayName;
    public Consumer<String> eh_setPrefix;
    public Consumer<String> eh_setSuffix;
    public Consumer<Boolean> eh_setAllowFriendlyFire;
    public Consumer<Boolean> eh_setCanSeeFriendlyInvisibles;
    public Consumer<EnumNameTagVisibility> eh_setNameTagVisibility;
    public Consumer<EnumNameTagVisibility> eh_setCollisionRule;
    public Object eh_patched;

    public ScoreboardTeam(Scoreboard var1, String var2) {
        this.i = EnumNameTagVisibility.ALWAYS;
        this.j = EnumNameTagVisibility.ALWAYS;
        this.k = EnumChatFormat.RESET;
        this.a = var1;
        this.b = var2;
        this.d = var2;
    }

    public String getName() {
        return this.b;
    }

    public String getDisplayName() {
        return this.d;
    }

    public void setDisplayName(String var1) {
        if (var1 == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            if(eh_setDisplayName != null) eh_setDisplayName.accept(var1);
            this.d = var1;
            this.a.handleTeamChanged(this);
        }
    }

    public Collection<String> getPlayerNameSet() {
        return this.c;
    }

    public String getPrefix() {
        return this.e;
    }

    public void setPrefix(String var1) {
        if (var1 == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        } else {
            if(eh_setPrefix != null) eh_setPrefix.accept(var1);
            this.e = var1;
            this.a.handleTeamChanged(this);
        }
    }

    public String getSuffix() {
        return this.f;
    }

    public void setSuffix(String var1) {
        if(eh_setSuffix != null) eh_setSuffix.accept(var1);
        this.f = var1;
        this.a.handleTeamChanged(this);
    }

    public String getFormattedName(String var1) {
        return this.getPrefix() + var1 + this.getSuffix();
    }

    public static String getPlayerDisplayName(ScoreboardTeamBase var0, String var1) {
        return var0 == null ? var1 : var0.getFormattedName(var1);
    }

    public boolean allowFriendlyFire() {
        return this.g;
    }

    public void setAllowFriendlyFire(boolean var1) {
        if(eh_setAllowFriendlyFire != null) eh_setAllowFriendlyFire.accept(var1);
        this.g = var1;
        this.a.handleTeamChanged(this);
    }

    public boolean canSeeFriendlyInvisibles() {
        return this.h;
    }

    public void setCanSeeFriendlyInvisibles(boolean var1) {
        if(eh_setCanSeeFriendlyInvisibles != null) eh_setCanSeeFriendlyInvisibles.accept(var1);
        this.h = var1;
        this.a.handleTeamChanged(this);
    }

    public EnumNameTagVisibility getNameTagVisibility() {
        return this.i;
    }

    public EnumNameTagVisibility j() {
        return this.j;
    }

    public void setNameTagVisibility(EnumNameTagVisibility var1) {
        if(eh_setNameTagVisibility != null) eh_setNameTagVisibility.accept(var1);
        this.i = var1;
        this.a.handleTeamChanged(this);
    }

    public void b(EnumNameTagVisibility var1) {
        if(eh_setCollisionRule != null) eh_setCollisionRule.accept(var1);
        this.j = var1;
        this.a.handleTeamChanged(this);
    }

    public int packOptionData() {
        int var1 = 0;
        if (this.allowFriendlyFire()) {
            var1 |= 1;
        }

        if (this.canSeeFriendlyInvisibles()) {
            var1 |= 2;
        }

        return var1;
    }

    public void a(EnumChatFormat var1) {
        this.k = var1;
    }

    public EnumChatFormat l() {
        return this.k;
    }
}
