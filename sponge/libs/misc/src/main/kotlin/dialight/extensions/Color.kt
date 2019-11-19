package dialight.extensions

import com.google.common.collect.ImmutableMap
import org.spongepowered.api.data.type.DyeColor
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.text.format.TextColor
import org.spongepowered.api.text.format.TextColors


private val dyeToChat = ImmutableMap.builder<DyeColor, TextColor>()
    .put(DyeColors.BLACK, TextColors.DARK_GRAY)
    .put(DyeColors.BLUE, TextColors.DARK_BLUE)
    .put(DyeColors.BROWN, TextColors.GOLD)
    .put(DyeColors.CYAN, TextColors.AQUA)
    .put(DyeColors.GRAY, TextColors.GRAY)
    .put(DyeColors.GREEN, TextColors.DARK_GREEN)
    .put(DyeColors.LIGHT_BLUE, TextColors.BLUE)
    .put(DyeColors.LIME, TextColors.GREEN)
    .put(DyeColors.MAGENTA, TextColors.LIGHT_PURPLE)
    .put(DyeColors.ORANGE, TextColors.GOLD)
    .put(DyeColors.PINK, TextColors.LIGHT_PURPLE)
    .put(DyeColors.PURPLE, TextColors.DARK_PURPLE)
    .put(DyeColors.RED, TextColors.DARK_RED)
    .put(DyeColors.SILVER, TextColors.GRAY)
    .put(DyeColors.WHITE, TextColors.WHITE)
    .put(DyeColors.YELLOW, TextColors.YELLOW)
    .build()


private val chatToDye = ImmutableMap.builder<TextColor, DyeColor>()
    .put(TextColors.WHITE, DyeColors.WHITE)
    .put(TextColors.BLACK, DyeColors.BLACK)
    .put(TextColors.DARK_GRAY, DyeColors.GRAY)
    .put(TextColors.GRAY, DyeColors.SILVER)
    .put(TextColors.DARK_RED, DyeColors.BROWN)
    .put(TextColors.RED, DyeColors.RED)
    .put(TextColors.GREEN, DyeColors.LIME)
    .put(TextColors.DARK_GREEN, DyeColors.GREEN)
    .put(TextColors.BLUE, DyeColors.LIGHT_BLUE)
    .put(TextColors.DARK_BLUE, DyeColors.BLUE)
    .put(TextColors.YELLOW, DyeColors.YELLOW)
    .put(TextColors.GOLD, DyeColors.ORANGE)
    .put(TextColors.AQUA, DyeColors.CYAN)
    .put(TextColors.DARK_AQUA, DyeColors.CYAN)
    .put(TextColors.LIGHT_PURPLE, DyeColors.MAGENTA)
    .put(TextColors.DARK_PURPLE, DyeColors.PURPLE)
    .build()


private val dyeToBanner = ImmutableMap.builder<DyeColor, Int>()
    .put(DyeColors.BLACK, 0)
    .put(DyeColors.RED, 1)
    .put(DyeColors.GREEN, 2)
    .put(DyeColors.BROWN, 3)
    .put(DyeColors.BLUE, 4)
    .put(DyeColors.PURPLE, 5)
    .put(DyeColors.CYAN, 6)
    .put(DyeColors.SILVER, 7)  // light gray
    .put(DyeColors.GRAY, 8)
    .put(DyeColors.PINK, 9)
    .put(DyeColors.LIME, 10)
    .put(DyeColors.YELLOW, 11)
    .put(DyeColors.LIGHT_BLUE, 12)
    .put(DyeColors.MAGENTA, 13)
    .put(DyeColors.ORANGE, 14)
    .put(DyeColors.WHITE, 15)
    .build()

val DyeColor.textColor: TextColor
    get() = dyeToChat[this] ?: TextColors.WHITE

val TextColor.dyeColor: DyeColor
    get() = chatToDye[this] ?: DyeColors.WHITE

val DyeColor.bannerDurability: Int
    get() = dyeToBanner[this] ?: 15
