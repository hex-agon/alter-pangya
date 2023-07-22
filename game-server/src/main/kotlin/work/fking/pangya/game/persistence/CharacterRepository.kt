package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.CharacterRoster
import work.fking.pangya.game.player.LUCIA
import work.fking.pangya.game.player.NURI_R
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.characterBaseParts
import java.util.concurrent.atomic.AtomicInteger

interface CharacterRepository {
    fun loadRoster(playerId: Int): CharacterRoster
    fun save(player: Player, character: Character): Character
}

class DefaultCharacterRepository : CharacterRepository {
    private val uidSequence = AtomicInteger(1)

    override fun loadRoster(playerId: Int): CharacterRoster = CharacterRoster(
        mutableListOf(
            Character(
                uid = 100,
                iffId = NURI_R,
                partIffIds = characterBaseParts(NURI_R)
            )
        )
    )

    override fun save(player: Player, character: Character): Character = character.copy(uid = uidSequence.getAndIncrement())
}