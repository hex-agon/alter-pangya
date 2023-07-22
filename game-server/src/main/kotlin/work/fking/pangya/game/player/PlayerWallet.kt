package work.fking.pangya.game.player

import kotlin.math.max

class PlayerWallet(
    pangBalance: Int = 10000,
    cookieBalance: Int = 0
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