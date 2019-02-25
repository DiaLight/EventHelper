package ru.dialight.eventhelper.tool

//import ru.dialight.eventhelper.utils.Colorizer
import ru.dialight.eventhelper.utils.Colorizer

class MainTool : Tool() {

    override val title = Colorizer.apply("|a|Вещь всея майнкрафта")
    override val lore = Colorizer.apply(
        mutableListOf(
            "|a|ПКМ|y|: Открыть \"Ультра мега инвентарь\"",
            "|a|Шифт|y|+|a|ПКМ|y|: Открыть последнюю открытую гуи.",
            "|y|Аналог: |g|/eh onOpen"
        )
    )

    init {

    }

}