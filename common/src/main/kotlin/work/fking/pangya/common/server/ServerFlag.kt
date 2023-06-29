package work.fking.pangya.common.server

enum class ServerFlag(private val value: Int) {
    HIDDEN(1 shl 4),
    SORT_PRIORITY(1 shl 7),
    GRAND_PRIX(1 shl 11);

    fun value(): Int {
        return value
    }
}
