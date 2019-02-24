package ru.dialight.eventhelper.utils

import org.spongepowered.api.text.format.TextColors
import java.util.ArrayList

object Colorizer {

    fun apply(format: String): String {
        var format = format
//        format = format.replace("\\|r\\|", TextColors.RED.toString())
//        format = format.replace("\\|g\\|", TextColors.GREEN.toString())
//        format = format.replace("\\|b\\|", TextColors.BLUE.toString())
//        format = format.replace("\\|a\\|", TextColors.AQUA.toString())
//        format = format.replace("\\|y\\|", TextColors.YELLOW.toString())
//        format = format.replace("\\|go\\|", TextColors.GOLD.toString())
//        format = format.replace("\\|gr\\|", TextColors.GRAY.toString())
//        format = format.replace("\\|dgr\\|", TextColors.DARK_GRAY.toString())
//        format = format.replace("\\|w\\|", TextColors.WHITE.toString())
////        format = format.replace("\\|_\\|", TextColors.UNDERLINE.toString())
//        if (format.indexOf('|') != -1) {
//            throw IllegalStateException("Bad format: $format")
//        }
////        format = format.replace("~", TextColors.STRIKETHROUGH.toString())
//        format = format.replace("`", TextColors.RESET.toString())
        return format
    }

    fun asList(vararg args: String): List<String> {
        val ret = ArrayList<String>(args.size)
        for (arg in args) {
            ret.add(apply(arg))
        }
        return ret
    }

    fun apply(vararg args: String) = Array(args.size) { apply(args[it]) }

    fun apply(lore: MutableList<String>): List<String> {
        val iterator = lore.listIterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            iterator.set(apply(next))
        }
        return lore
    }

}