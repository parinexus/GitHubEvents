package parinexus.sample.githubevents.libraries.design.utils

import android.text.format.DateUtils
import java.net.URI
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


fun formatAbsolute(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String {
    val zdt = Instant.ofEpochMilli(epochMillis).atZone(zoneId)
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
        .format(zdt)
}

fun formatRelative(epochMillis: Long, now: Long = System.currentTimeMillis()): String =
    DateUtils.getRelativeTimeSpanString(epochMillis, now, DateUtils.MINUTE_IN_MILLIS).toString()

fun humanRepoPath(url: String): String =
    runCatching {
        val uri = URI(url)
        val host = uri.host ?: return@runCatching url
        val path = (uri.path ?: "").trimEnd('/')
        if (host.contains("github")) path.trim().ifEmpty { host } else "$host$path"
    }.getOrDefault(url)

fun safeParseEpoch(raw: String?): Long? {
    if (raw.isNullOrBlank()) return null
    raw.trim().toLongOrNull()?.let { return if (it > 10_000_000_000L) it else it * 1000 }
    return runCatching { Instant.parse(raw).toEpochMilli() }.getOrNull()
}

fun splitAbsolute(absolute: String): Pair<String, String> {
    if (absolute.isBlank()) return "" to ""

    val comma = absolute.indexOf(',')
    if (comma == -1) return absolute.trim() to ""   // فالبک

    val left = absolute.substring(0, comma).trim()
    val right = absolute.substring(comma + 1).trim()

    val m = Regex("""^(\d{4})\s+(.+)$""").find(right)
    return if (m != null) {
        val year = m.groupValues[1]
        val time = m.groupValues[2]
        "$left, $year" to time
    } else {
        absolute.trim() to ""
    }
}