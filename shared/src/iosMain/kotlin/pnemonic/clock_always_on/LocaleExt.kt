package pnemonic.clock_always_on

import androidx.compose.ui.text.intl.PlatformLocale
import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.languageCode
import platform.Foundation.localeWithLocaleIdentifier

fun PlatformLocale.toNSLocale(): NSLocale {
    val c = countryCode
    if (c.isNullOrEmpty()) {
        return NSLocale.localeWithLocaleIdentifier(languageCode)
    }
    return NSLocale.localeWithLocaleIdentifier(languageCode + "_" + c)
}