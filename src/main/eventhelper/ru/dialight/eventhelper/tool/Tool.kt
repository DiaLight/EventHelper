package ru.dialight.eventhelper.tool

import org.spongepowered.api.text.Text

abstract class Tool {

    abstract val title: Text
    abstract val lore: List<Text>

}