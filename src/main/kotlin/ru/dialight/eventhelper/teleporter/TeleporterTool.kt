package ru.dialight.eventhelper.teleporter

import ru.dialight.eventhelper.tool.Tool
import ru.dialight.eventhelper.utils.Colorizer

class TeleporterTool : Tool() {

    override val title = Colorizer.apply("|a|Телепортатор игроков")

    override val lore = Colorizer.apply(
        "|y|Телепортирование",
        "|g|ЛКМ|y|: телепортировать всех, кто помечен",
        "|g|shift|y|+|g|ЛКМ|y|: телепортировать всех",
        "|y|Выбор игроков",
        "|g|ПКМ|y|: открыть редактор выделенных игроков",
        "|g|ПКМ по игроку|y|: пометить ирока",
        "|g|shift|y|+|g|ПКМ|y|: очистить список помеченных"
    )

}