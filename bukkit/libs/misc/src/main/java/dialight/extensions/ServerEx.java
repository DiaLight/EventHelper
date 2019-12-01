package dialight.extensions;

import org.bukkit.Server;
import org.bukkit.World;

public class ServerEx {

    private final Server server;

    public ServerEx(Server server) {
        this.server = server;
    }

    public World getMainWorld() {
        return server.getWorlds().get(0);
    }

    public static ServerEx of(Server server) {
        return new ServerEx(server);
    }

}
