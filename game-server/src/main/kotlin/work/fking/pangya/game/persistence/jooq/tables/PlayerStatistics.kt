/*
 * This file is generated by jOOQ.
 */
package work.fking.pangya.game.persistence.jooq.tables


import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl

import work.fking.pangya.game.persistence.jooq.AlterPangya
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_STATISTICS_PKEY
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_STATISTICS__FK_PLAYER_STATISTICS__ACCOUNT
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerStatisticsRecord


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class PlayerStatistics(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, PlayerStatisticsRecord>?,
    aliased: Table<PlayerStatisticsRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<PlayerStatisticsRecord>(
    alias,
    AlterPangya.ALTER_PANGYA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>alter_pangya.player_statistics</code>
         */
        val PLAYER_STATISTICS: PlayerStatistics = PlayerStatistics()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<PlayerStatisticsRecord> = PlayerStatisticsRecord::class.java

    /**
     * The column <code>alter_pangya.player_statistics.account_uid</code>.
     */
    val ACCOUNT_UID: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("account_uid"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.total_shots</code>.
     */
    val TOTAL_SHOTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("total_shots"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.total_putts</code>.
     */
    val TOTAL_PUTTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("total_putts"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.playtime_seconds</code>.
     */
    val PLAYTIME_SECONDS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("playtime_seconds"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.shot_time_seconds</code>.
     */
    val SHOT_TIME_SECONDS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("shot_time_seconds"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.longest_drive</code>.
     */
    val LONGEST_DRIVE: TableField<PlayerStatisticsRecord, Float?> = createField(DSL.name("longest_drive"), SQLDataType.REAL.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.pangya_shots</code>.
     */
    val PANGYA_SHOTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pangya_shots"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.timeouts</code>.
     */
    val TIMEOUTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("timeouts"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.oob_shots</code>.
     */
    val OOB_SHOTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("oob_shots"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.total_distance</code>.
     */
    val TOTAL_DISTANCE: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("total_distance"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.total_holes</code>.
     */
    val TOTAL_HOLES: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("total_holes"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.unfinished_holes</code>.
     */
    val UNFINISHED_HOLES: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("unfinished_holes"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.hole_in_ones</code>.
     */
    val HOLE_IN_ONES: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("hole_in_ones"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.bunker_shots</code>.
     */
    val BUNKER_SHOTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("bunker_shots"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.fairway_shots</code>.
     */
    val FAIRWAY_SHOTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("fairway_shots"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.albatross</code>.
     */
    val ALBATROSS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("albatross"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.successful_putts</code>.
     */
    val SUCCESSFUL_PUTTS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("successful_putts"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.longest_putt</code>.
     */
    val LONGEST_PUTT: TableField<PlayerStatisticsRecord, Float?> = createField(DSL.name("longest_putt"), SQLDataType.REAL.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.longest_chip_in</code>.
     */
    val LONGEST_CHIP_IN: TableField<PlayerStatisticsRecord, Float?> = createField(DSL.name("longest_chip_in"), SQLDataType.REAL.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.experience</code>.
     */
    val EXPERIENCE: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("experience"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.level</code>.
     */
    val LEVEL: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("level"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.pang_earned</code>.
     */
    val PANG_EARNED: TableField<PlayerStatisticsRecord, Long?> = createField(DSL.name("pang_earned"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.total_score</code>.
     */
    val TOTAL_SCORE: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("total_score"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.games_played</code>.
     */
    val GAMES_PLAYED: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("games_played"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.player_statistics.game_combo_current_streak</code>.
     */
    val GAME_COMBO_CURRENT_STREAK: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("game_combo_current_streak"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.player_statistics.game_combo_best_streak</code>.
     */
    val GAME_COMBO_BEST_STREAK: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("game_combo_best_streak"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.games_quit</code>.
     */
    val GAMES_QUIT: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("games_quit"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.player_statistics.pang_won_in_battle</code>.
     */
    val PANG_WON_IN_BATTLE: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pang_won_in_battle"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.pang_battle_wins</code>.
     */
    val PANG_BATTLE_WINS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pang_battle_wins"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.player_statistics.pang_battle_losses</code>.
     */
    val PANG_BATTLE_LOSSES: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pang_battle_losses"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.player_statistics.pang_battle_all_ins</code>.
     */
    val PANG_BATTLE_ALL_INS: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pang_battle_all_ins"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.player_statistics.pang_battle_combo</code>.
     */
    val PANG_BATTLE_COMBO: TableField<PlayerStatisticsRecord, Int?> = createField(DSL.name("pang_battle_combo"), SQLDataType.INTEGER.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<PlayerStatisticsRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<PlayerStatisticsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>alter_pangya.player_statistics</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>alter_pangya.player_statistics</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>alter_pangya.player_statistics</code> table reference
     */
    constructor(): this(DSL.name("player_statistics"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, PlayerStatisticsRecord>): this(Internal.createPathAlias(child, key), child, key, PLAYER_STATISTICS, null)
    override fun getSchema(): Schema? = if (aliased()) null else AlterPangya.ALTER_PANGYA
    override fun getPrimaryKey(): UniqueKey<PlayerStatisticsRecord> = PLAYER_STATISTICS_PKEY
    override fun getReferences(): List<ForeignKey<PlayerStatisticsRecord, *>> = listOf(PLAYER_STATISTICS__FK_PLAYER_STATISTICS__ACCOUNT)

    private lateinit var _account: Account

    /**
     * Get the implicit join path to the <code>alter_pangya.account</code>
     * table.
     */
    fun account(): Account {
        if (!this::_account.isInitialized)
            _account = Account(this, PLAYER_STATISTICS__FK_PLAYER_STATISTICS__ACCOUNT)

        return _account;
    }

    val account: Account
        get(): Account = account()
    override fun `as`(alias: String): PlayerStatistics = PlayerStatistics(DSL.name(alias), this)
    override fun `as`(alias: Name): PlayerStatistics = PlayerStatistics(alias, this)
    override fun `as`(alias: Table<*>): PlayerStatistics = PlayerStatistics(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): PlayerStatistics = PlayerStatistics(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): PlayerStatistics = PlayerStatistics(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): PlayerStatistics = PlayerStatistics(name.getQualifiedName(), null)
}
