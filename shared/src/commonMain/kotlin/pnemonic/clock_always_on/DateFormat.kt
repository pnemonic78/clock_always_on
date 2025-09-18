package pnemonic.clock_always_on

import androidx.compose.ui.text.intl.PlatformLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

object DateFormat {
    // is pretty completely specified, such as {@code Tuesday, April 12, 1952 AD or 3:30:42pm PST}.
    const val FULL = 0

    // is longer, such as {@code January 12, 1952} or {@code 3:30:32pm}
    const val LONG = 1

    // is longer, such as {@code Jan 12, 1952}
    const val MEDIUM = 2

    // is completely numeric, such as {@code 12.13.52} or {@code 3:30pm}
    const val SHORT = 3

    const val DEFAULT = MEDIUM

    private val formatters: MutableMap<String, DateTimeFormat<LocalDateTime>> = mutableMapOf()

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun getInstance(pattern: String, locale: PlatformLocale): DateTimeFormat<LocalDateTime> {
        val formatterKey = "$pattern/$locale"
        var formatter = formatters[formatterKey]
        if (formatter == null) {
            if (pattern == "hh") {
                formatter = LocalDateTime.Format {
                    amPmHour()
                }
            } else {
                formatter = LocalDateTime.Format {
                    byUnicodePattern(pattern)
                }
            }
            formatters[formatterKey] = formatter
        }
        return formatter
    }
}