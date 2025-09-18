package pnemonic.clock_always_on

enum class TimeFormat(val pattern: String?, val skeleton: String) {
    None(null, ""),
    Hours("HH", "H"),
    Hours12(null, "h"),
    Minutes("mm", "m"),
    Seconds("ss", "s"),
    HoursAndMinutes(null, "Hm"),
    HoursAndMinutes12(null, "hm"),
    HoursAndMinutesAndSeconds(null, "Hms"),
    HoursAndMinutesAndSeconds12(null, "hms"),
}