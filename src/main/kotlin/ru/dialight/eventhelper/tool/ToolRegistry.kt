package ru.dialight.eventhelper.tool

import kotlin.reflect.KClass

class ToolRegistry {

    val tools = arrayListOf<Tool>()

    fun registerTool(tool: Tool) {
        tools += tool
    }

    @Deprecated("concept")
    fun registerToolCls(tool: KClass<out Tool>) {

    }

}