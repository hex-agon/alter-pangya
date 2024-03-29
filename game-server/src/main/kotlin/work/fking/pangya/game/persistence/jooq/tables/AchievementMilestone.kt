/*
 * This file is generated by jOOQ.
 */
package work.fking.pangya.game.persistence.jooq.tables


import java.util.function.Function

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row3
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl

import work.fking.pangya.game.persistence.jooq.AlterPangya
import work.fking.pangya.game.persistence.jooq.keys.ACHIEVEMENT_MILESTONE_PKEY
import work.fking.pangya.game.persistence.jooq.keys.ACHIEVEMENT_MILESTONE__FK__ACHIEVEMENT_MILESTONE
import work.fking.pangya.game.persistence.jooq.tables.records.AchievementMilestoneRecord


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class AchievementMilestone(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, AchievementMilestoneRecord>?,
    aliased: Table<AchievementMilestoneRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<AchievementMilestoneRecord>(
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
         * The reference instance of
         * <code>alter_pangya.achievement_milestone</code>
         */
        val ACHIEVEMENT_MILESTONE: AchievementMilestone = AchievementMilestone()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<AchievementMilestoneRecord> = AchievementMilestoneRecord::class.java

    /**
     * The column <code>alter_pangya.achievement_milestone.iff_id</code>.
     */
    val IFF_ID: TableField<AchievementMilestoneRecord, Int?> = createField(DSL.name("iff_id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column
     * <code>alter_pangya.achievement_milestone.achievement_iff_id</code>.
     */
    val ACHIEVEMENT_IFF_ID: TableField<AchievementMilestoneRecord, Int?> = createField(DSL.name("achievement_iff_id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>alter_pangya.achievement_milestone.name</code>.
     */
    val NAME: TableField<AchievementMilestoneRecord, String?> = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<AchievementMilestoneRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<AchievementMilestoneRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>alter_pangya.achievement_milestone</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>alter_pangya.achievement_milestone</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>alter_pangya.achievement_milestone</code> table reference
     */
    constructor(): this(DSL.name("achievement_milestone"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, AchievementMilestoneRecord>): this(Internal.createPathAlias(child, key), child, key, ACHIEVEMENT_MILESTONE, null)
    override fun getSchema(): Schema? = if (aliased()) null else AlterPangya.ALTER_PANGYA
    override fun getPrimaryKey(): UniqueKey<AchievementMilestoneRecord> = ACHIEVEMENT_MILESTONE_PKEY
    override fun getReferences(): List<ForeignKey<AchievementMilestoneRecord, *>> = listOf(ACHIEVEMENT_MILESTONE__FK__ACHIEVEMENT_MILESTONE)

    private lateinit var _achievement: Achievement

    /**
     * Get the implicit join path to the <code>alter_pangya.achievement</code>
     * table.
     */
    fun achievement(): Achievement {
        if (!this::_achievement.isInitialized)
            _achievement = Achievement(this, ACHIEVEMENT_MILESTONE__FK__ACHIEVEMENT_MILESTONE)

        return _achievement;
    }

    val achievement: Achievement
        get(): Achievement = achievement()
    override fun `as`(alias: String): AchievementMilestone = AchievementMilestone(DSL.name(alias), this)
    override fun `as`(alias: Name): AchievementMilestone = AchievementMilestone(alias, this)
    override fun `as`(alias: Table<*>): AchievementMilestone = AchievementMilestone(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): AchievementMilestone = AchievementMilestone(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): AchievementMilestone = AchievementMilestone(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): AchievementMilestone = AchievementMilestone(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<Int?, Int?, String?> = super.fieldsRow() as Row3<Int?, Int?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Int?, Int?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Int?, Int?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
