package dialight.extensions

fun <T> Iterator<T>.toList(): List<T> {
    val list = ArrayList<T>()
    while (hasNext()) list.add(next())
    return list
}

