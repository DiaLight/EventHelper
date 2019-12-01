package dialight.toollib;

import dialight.misc.Colorizer;

public class ToolMessages {

    public static final String pluginPrefix = Colorizer.apply("|go|Инструмент|gr|: ");

    public static String notFound(String id) {
        return pluginPrefix + Colorizer.apply("|r|Инструмент с ID «$id» не зарегистрирован");
    }

}
