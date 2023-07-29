package work.fking.pangya.game.persistence

import org.jooq.DSLContext
import work.fking.pangya.game.persistence.jooq.tables.references.ACCOUNT
import work.fking.pangya.game.player.PlayerWallet

interface PlayerRepository {
    fun loadWallet(playerUid: Int): PlayerWallet
}

class InMemoryPlayerRepository : PlayerRepository {
    private val wallets = mutableMapOf<Int, PlayerWallet>()

    override fun loadWallet(playerUid: Int): PlayerWallet = wallets[playerUid] ?: PlayerWallet()
}

class JooqPlayerRepository(private val jooq: DSLContext) : PlayerRepository {

    override fun loadWallet(playerUid: Int): PlayerWallet {
        val wallet = jooq.select(ACCOUNT.PANG_BALANCE, ACCOUNT.COOKIE_BALANCE)
            .from(ACCOUNT)
            .where(ACCOUNT.UID.eq(playerUid))
            .fetchOne {
                PlayerWallet(
                    pangBalance = it[ACCOUNT.PANG_BALANCE] as Int,
                    cookieBalance = it[ACCOUNT.COOKIE_BALANCE] as Int
                )
            }
        return wallet ?: throw IllegalStateException("could not load player wallet for playerUid=$playerUid")
    }
}