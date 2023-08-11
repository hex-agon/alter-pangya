package work.fking.pangya.game.player

import kotlin.math.max

class PlayerWallet(
    pangBalance: Long = 10000,
    cookieBalance: Long = 0
) {
    var pangBalance = pangBalance
        set(value) {
            field = max(0, value)
        }
    var cookieBalance = cookieBalance
        set(value) {
            field = max(0, value)
        }
}