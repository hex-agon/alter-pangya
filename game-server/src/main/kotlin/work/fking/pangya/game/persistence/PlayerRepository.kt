package work.fking.pangya.game.persistence

import work.fking.pangya.game.persistence.jooq.tables.references.ACCOUNT
import work.fking.pangya.game.player.PlayerWallet

interface PlayerRepository {
    fun loadWallet(txCtx: TransactionalContext, playerUid: Int): PlayerWallet
    fun updateNickname(txCtx: TransactionalContext, playerUid: Int, nickname: String)
}

class InMemoryPlayerRepository : PlayerRepository {
    private val wallets = mutableMapOf<Int, PlayerWallet>()

    override fun loadWallet(txCtx: TransactionalContext, playerUid: Int): PlayerWallet = wallets[playerUid] ?: PlayerWallet()
    override fun updateNickname(txCtx: TransactionalContext, playerUid: Int, nickname: String) {
    }
}

class JooqPlayerRepository : PlayerRepository {

    override fun loadWallet(txCtx: TransactionalContext, playerUid: Int): PlayerWallet {
        val wallet = txCtx.jooq().select(ACCOUNT.PANG_BALANCE, ACCOUNT.COOKIE_BALANCE)
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

    override fun updateNickname(txCtx: TransactionalContext, playerUid: Int, nickname: String) {
        txCtx.jooq().update(ACCOUNT)
            .set(ACCOUNT.NICKNAME, nickname)
            .where(ACCOUNT.UID.eq(playerUid))
            .and(ACCOUNT.NICKNAME.isNull)
            .execute()
    }
}