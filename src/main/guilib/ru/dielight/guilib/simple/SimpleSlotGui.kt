package ru.dielight.guilib.simple

import org.spongepowered.api.text.Text
import ru.dielight.guilib.GuiRegistry

abstract class SimpleSlotGui(registry: GuiRegistry, title: Text, width: Int, height: Int) :
    SimpleGui(registry, title, width, height) {

}

