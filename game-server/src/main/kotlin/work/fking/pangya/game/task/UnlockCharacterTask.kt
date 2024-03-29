package work.fking.pangya.game.task

import work.fking.pangya.game.model.IFF_TYPE_CHARACTER
import work.fking.pangya.game.model.iffTypeFromId
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.characterBaseParts

class UnlockCharacterTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val iffId: Int
) : Runnable {

    override fun run() {
        require(iffTypeFromId(iffId) == IFF_TYPE_CHARACTER) { "iffId is not a character" }
        val partIffIds = characterBaseParts(iffId)
        val character = persistenceCtx.characterRepository.save(
            txCtx = persistenceCtx.noTxContext(),
            playerUid = player.uid,
            character = Character(
                iffId = iffId,
                partIffIds = partIffIds
            )
        )
        player.characterRoster.entries.add(character)
    }
}