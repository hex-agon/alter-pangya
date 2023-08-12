/*
 * This file is generated by jOOQ.
 */
package work.fking.pangya.game.persistence.jooq.indexes


import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal

import work.fking.pangya.game.persistence.jooq.tables.Account
import work.fking.pangya.game.persistence.jooq.tables.FlywaySchemaHistory
import work.fking.pangya.game.persistence.jooq.tables.PlayerAchievement
import work.fking.pangya.game.persistence.jooq.tables.PlayerAchievementMilestone
import work.fking.pangya.game.persistence.jooq.tables.PlayerCaddie
import work.fking.pangya.game.persistence.jooq.tables.PlayerCharacter



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val FLYWAY_SCHEMA_HISTORY_S_IDX: Index = Internal.createIndex(DSL.name("flyway_schema_history_s_idx"), FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, arrayOf(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS), false)
val IDX__ACCOUNT_NICKNAME: Index = Internal.createIndex(DSL.name("idx__account_nickname"), Account.ACCOUNT, arrayOf(Account.ACCOUNT.NICKNAME), true)
val IDX__ACCOUNT_USERNAME: Index = Internal.createIndex(DSL.name("idx__account_username"), Account.ACCOUNT, arrayOf(Account.ACCOUNT.USERNAME), true)
val IDX__ACCOUNT_UUID: Index = Internal.createIndex(DSL.name("idx__account_uuid"), Account.ACCOUNT, arrayOf(Account.ACCOUNT.UUID), false)
val IDX_PLAYER_ACHIEVEMENT: Index = Internal.createIndex(DSL.name("idx_player_achievement"), PlayerAchievement.PLAYER_ACHIEVEMENT, arrayOf(PlayerAchievement.PLAYER_ACHIEVEMENT.ACCOUNT_UID, PlayerAchievement.PLAYER_ACHIEVEMENT.IFF_ID), true)
val IDX_PLAYER_ACHIEVEMENT_MILESTONE: Index = Internal.createIndex(DSL.name("idx_player_achievement_milestone"), PlayerAchievementMilestone.PLAYER_ACHIEVEMENT_MILESTONE, arrayOf(PlayerAchievementMilestone.PLAYER_ACHIEVEMENT_MILESTONE.PLAYER_ACHIEVEMENT_UID, PlayerAchievementMilestone.PLAYER_ACHIEVEMENT_MILESTONE.IFF_ID), true)
val IDX_PLAYER_CADDIE: Index = Internal.createIndex(DSL.name("idx_player_caddie"), PlayerCaddie.PLAYER_CADDIE, arrayOf(PlayerCaddie.PLAYER_CADDIE.ACCOUNT_UID, PlayerCaddie.PLAYER_CADDIE.IFF_ID), true)
val IDX_PLAYER_CHARACTER: Index = Internal.createIndex(DSL.name("idx_player_character"), PlayerCharacter.PLAYER_CHARACTER, arrayOf(PlayerCharacter.PLAYER_CHARACTER.ACCOUNT_UID, PlayerCharacter.PLAYER_CHARACTER.IFF_ID), true)
