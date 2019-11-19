package dialight.extensions

import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.DataFormats
import java.io.StringWriter


fun String.toContainer(): DataContainer {
    return DataFormats.HOCON.read(this)
}

fun DataView.toJson(): String {
    val stringWriter = StringWriter()
    DataFormats.JSON.writeTo(stringWriter, this)
    return stringWriter.toString()
}
