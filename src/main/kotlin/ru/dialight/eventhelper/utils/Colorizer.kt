package ru.dialight.eventhelper.utils

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.*
import java.lang.IllegalStateException
import java.lang.StringBuilder

class Colorizer(texts: MutableList<String>) {

    private val input: MutableList<String> = texts
    private val result: MutableList<Text> = arrayListOf()

    constructor(vararg texts: String) : this(texts.toMutableList())

    fun parse() {
        for (singleInput in input) {
            result.add(parseSingle(singleInput))
        }
    }

    private fun parseSingle(string: String): Text {

        val childs: MutableList<Text> = arrayListOf()

        var index = 0
        var currentChar: Char

        var skipNext = false
        var state = ColorizerState()

        while (index < string.length) {
            currentChar = string[index++]
            if (skipNext) {
                state.text.append(currentChar)
                continue
            }
            when (currentChar) {
                '\\' -> skipNext = true
                '|' -> {
                    if (state.hasResult()) {
                        childs.add(state.getResult())
                    }
                    state = ColorizerState()
                    index = parseStyle(state, index, string)
                }
                else -> state.text.append(currentChar)
            }
        }

        if (state.hasResult()) {
            childs.add(state.getResult())
        }

        return when (childs.size) {
            0 -> Text.empty()
            1 -> childs[0]
            else -> {
                val result = Text.builder()
                childs.forEach { result.append(it) }
                result.build()
            }
        }
    }

    private fun parseStyle(state: ColorizerState, startIndex: Int, string: String): Int {
        var index = startIndex
        var currentChar: Char
        val builder = StringBuilder()

        while (index < string.length) {
            currentChar = string[index++]
            when (currentChar) {
                '?' -> state.styles.add(TextStyles.OBFUSCATED)
                '*' -> state.styles.add(TextStyles.BOLD)
                '~' -> state.styles.add(TextStyles.STRIKETHROUGH)
                '_' -> state.styles.add(TextStyles.UNDERLINE)
                '/' -> state.styles.add(TextStyles.ITALIC)
                '`' -> {
                    state.color = TextColors.NONE
                    state.styles.clear()
                }
                '|' -> {
                    state.color = getColor(builder.toString())
                    return index
                }
                else -> builder.append(currentChar)
            }
        }
        throw IllegalStateException("Expected symbol '|' in text '$string'")
    }

    private fun getColor(string: String): TextColor {
        return when (string) {
            "a", "aqua" -> TextColors.AQUA
            "bl", "black" -> TextColors.BLACK
            "b", "blue" -> TextColors.BLUE
            "da", "dark_aqua" -> TextColors.DARK_AQUA
            "db", "dark_blue" -> TextColors.DARK_BLUE
            "dgr", "dark_gray" -> TextColors.DARK_GRAY
            "dg", "dark_green" -> TextColors.DARK_GREEN
            "dp", "dark_purple" -> TextColors.DARK_PURPLE
            "dr", "dark_red" -> TextColors.DARK_RED
            "go", "gold" -> TextColors.GOLD
            "gr", "gray" -> TextColors.GRAY
            "g", "green" -> TextColors.GREEN
            "lp", "light_purple" -> TextColors.LIGHT_PURPLE
            "r", "red" -> TextColors.RED
            "w", "white" -> TextColors.WHITE
            "y", "yellow" -> TextColors.YELLOW
            else -> {
                println("unknown style '$string'")
                TextColors.NONE
            }
        }
    }

    fun getFirstResult(): Text {
        if (result.size > 0) {
            return result[0]
        }
        return Text.empty()
    }

    fun getResult(): List<Text> {
        return result
    }

    companion object {

        fun apply(text: String): Text {
            val colorizer = Colorizer(text)
            colorizer.parse()
            return colorizer.getFirstResult()
        }

        fun apply(texts: MutableList<String>): List<Text> {
            val colorizer = Colorizer(texts)
            colorizer.parse()
            return colorizer.getResult()
        }

    }

}

class ColorizerState {

    var text = StringBuilder()
    var color = TextColors.NONE
    var styles: MutableList<TextStyle> = arrayListOf()

    private var result: Text? = null

    fun hasResult(): Boolean {
        if (text.isEmpty()) {
            return false
        }
        result = Text.builder(this.text.toString())
            .color(this.color)
            .style(*styles.toTypedArray())
            .build()
        return true
    }

    fun getResult(): Text {
        return if (result != null) result!! else Text.empty()
    }

}