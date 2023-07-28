package work.fking.pangya.game.model

import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val achievements = emptyMap<Int, Achievement>()

class Achievement(
    val iffId: Int,
    val completedWith: Int,
    val category: Int,
    val quests: List<Quest>
)

class Quest(
    val iffId: Int,
    val name: String
)

private fun loadAchievements(): Map<Int, Achievement> {
    val mapper = TomlMapper().registerKotlinModule()
    val inputStream = Achievement::class.java.getResourceAsStream("/iff/achievements.toml") ?: throw IllegalStateException("missing archievements.toml")
    return mapper.readValue<Map<Int, Achievement>>(inputStream)
}
