package dialight.guilib;

import dialight.extensions.Colorizer;

/**
 * Created by DiaLight on 17.06.2016.
 */
public class GuiMessages {

    public static final String pluginPrefix = Colorizer.apply("|go|Gui|gr|: ");

    public static String unexpectedItem =
            pluginPrefix + Colorizer.apply("|r|Удален неожиданный предмет");

    public static String unusedAction =
            pluginPrefix + Colorizer.apply("|r|Данное действие не обрабатывается");

}
