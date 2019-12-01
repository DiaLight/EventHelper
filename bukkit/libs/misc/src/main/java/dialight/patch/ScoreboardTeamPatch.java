package dialight.patch;

import dialight.nms.ReflectionUtils;

import java.lang.reflect.Constructor;

public abstract class ScoreboardTeamPatch {

    private static final Constructor<? extends ScoreboardTeamPatch> constructor =
            ReflectionUtils.findCompatibleConstructor(ScoreboardTeamPatch.class);

    public abstract boolean patch();

    public static ScoreboardTeamPatch of() {
        return ReflectionUtils.newInstance(constructor);
    }

}
