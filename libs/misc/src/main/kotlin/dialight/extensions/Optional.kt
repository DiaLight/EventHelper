package dialight.extensions

import java.util.*

fun <T> Optional<T>.getOrNull(): T? = orElse(null)

