package dialight.fake;

import dialight.nms.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public abstract class HotBarMessageFk {

    private static final Constructor<? extends HotBarMessageFk> constructor = ReflectionUtils.findCompatibleConstructor(HotBarMessageFk.class, Player.class);

    protected final Player player;

    public HotBarMessageFk(Player player) {
        this.player = player;
    }

    public abstract void sendMessage(String message);

    public static HotBarMessageFk of(Player player) {
        return ReflectionUtils.newInstance(constructor, player);
    }

}
