package work.fking.pangya.game.persistence

import org.jooq.DSLContext
import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_CHARACTER_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerCharacterRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_CHARACTER
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.CharacterRoster
import java.util.concurrent.atomic.AtomicInteger


interface CharacterRepository {
    fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CharacterRoster
    fun save(txCtx: TransactionalContext, playerUid: Int, character: Character): Character
}

class InMemoryCharacterRepository : CharacterRepository {
    private val uidSequence = AtomicInteger(1)
    private val playerCharacters = mutableMapOf<Int, MutableList<Character>>()

    override fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CharacterRoster = CharacterRoster(playerCharacters[playerUid] ?: mutableListOf())

    override fun save(txCtx: TransactionalContext, playerUid: Int, character: Character): Character {
        val characters = playerCharacters[playerUid] ?: mutableListOf()
        return characters.find { it.uid == character.uid } ?: character.copy(uid = uidSequence.getAndIncrement())
    }
}

class JooqCharacterRepository : CharacterRepository {
    private val characterMapper = RecordMapper<PlayerCharacterRecord, Character> {
        Character(
            uid = it.uid!!,
            iffId = it.iffId,
            hairColor = it.hairColor,
            partIffIds = it.partIffIds,
            partUids = it.partUids,
            auxParts = it.auxParts,
            cutInIffId = it.cutinIffId,
            stats = it.stats,
            mastery = it.mastery,
            cardIffIds = it.cards
        )
    }

    override fun loadRoster(txCtx: TransactionalContext, playerUid: Int): CharacterRoster {
        val characters = txCtx.jooq().selectFrom(PLAYER_CHARACTER)
            .where(PLAYER_CHARACTER.ACCOUNT_UID.eq(playerUid))
            .fetch(characterMapper)
        return CharacterRoster(characters)
    }

    override fun save(txCtx: TransactionalContext, playerUid: Int, character: Character): Character {
        val insert = txCtx.jooq().insertInto(PLAYER_CHARACTER)

        if (character.uid != -1) insert.set(PLAYER_CHARACTER.UID, character.uid)

        val uid = insert
            .set(PLAYER_CHARACTER.ACCOUNT_UID, playerUid)
            .set(PLAYER_CHARACTER.IFF_ID, character.iffId)
            .set(PLAYER_CHARACTER.HAIR_COLOR, character.hairColor)
            .set(PLAYER_CHARACTER.PART_IFF_IDS, character.partIffIds)
            .set(PLAYER_CHARACTER.PART_UIDS, character.partUids)
            .set(PLAYER_CHARACTER.AUX_PARTS, character.auxParts)
            .set(PLAYER_CHARACTER.CUTIN_IFF_ID, character.cutInIffId)
            .set(PLAYER_CHARACTER.STATS, character.stats)
            .set(PLAYER_CHARACTER.MASTERY, character.mastery)
            .set(PLAYER_CHARACTER.CARDS, character.cardIffIds)
            .onConflict(PLAYER_CHARACTER_PKEY.fields)
            .doUpdate()
            .set(PLAYER_CHARACTER.HAIR_COLOR, character.hairColor)
            .set(PLAYER_CHARACTER.PART_IFF_IDS, character.partIffIds)
            .set(PLAYER_CHARACTER.PART_UIDS, character.partUids)
            .set(PLAYER_CHARACTER.AUX_PARTS, character.auxParts)
            .set(PLAYER_CHARACTER.CUTIN_IFF_ID, character.cutInIffId)
            .set(PLAYER_CHARACTER.STATS, character.stats)
            .set(PLAYER_CHARACTER.MASTERY, character.mastery)
            .set(PLAYER_CHARACTER.CARDS, character.cardIffIds)
            .returningResult(PLAYER_CHARACTER.UID)
            .fetchOneInto(Int::class.java)

        return character.copy(
            uid = uid!!
        )
    }
}