package work.fking.pangya.game.persistence

import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_CADDIE_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerCaddieRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_CADDIE
import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.CaddieRoster
import java.util.concurrent.atomic.AtomicInteger

interface CaddieRepository {
    fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CaddieRoster
    fun save(txCtx: TransactionalContext, playerUid: Int, caddie: Caddie): Caddie
}

class InMemoryCaddieRepository : CaddieRepository {
    private val uidSequence = AtomicInteger(1)
    private val playerCaddies = mutableMapOf<Int, MutableList<Caddie>>()

    override fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CaddieRoster = CaddieRoster(playerCaddies[playerUid] ?: mutableListOf())

    override fun save(txCtx: TransactionalContext, playerUid: Int, caddie: Caddie): Caddie {
        val characters = playerCaddies[playerUid] ?: mutableListOf()
        return characters.find { it.uid == caddie.uid } ?: caddie.copy(uid = uidSequence.getAndIncrement())
    }
}

class JooqCaddieRepository : CaddieRepository {
    private val caddieMapper = RecordMapper<PlayerCaddieRecord, Caddie> {
        Caddie(
            uid = it.uid!!,
            iffId = it.iffId,
            level = it.level,
            experience = it.experience
        )
    }

    override fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CaddieRoster {
        val caddies = txCtx.jooq().selectFrom(PLAYER_CADDIE)
            .where(PLAYER_CADDIE.ACCOUNT_UID.eq(playerUid))
            .fetch(caddieMapper)
        return CaddieRoster(caddies)
    }

    override fun save(txCtx: TransactionalContext, playerUid: Int, caddie: Caddie): Caddie {
        val insert = txCtx.jooq().insertInto(PLAYER_CADDIE)

        if (caddie.uid != -1) insert.set(PLAYER_CADDIE.UID, caddie.uid)

        val uid = insert
            .set(PLAYER_CADDIE.ACCOUNT_UID, playerUid)
            .set(PLAYER_CADDIE.IFF_ID, caddie.iffId)
            .set(PLAYER_CADDIE.LEVEL, caddie.level)
            .set(PLAYER_CADDIE.EXPERIENCE, caddie.experience)
            .onConflict(PLAYER_CADDIE_PKEY.fields)
            .doUpdate()
            .set(PLAYER_CADDIE.LEVEL, caddie.level)
            .set(PLAYER_CADDIE.EXPERIENCE, caddie.experience)
            .returning(PLAYER_CADDIE.UID)
            .fetchOneInto(Int::class.java)

        return caddie.copy(
            uid = uid!!
        )
    }
}