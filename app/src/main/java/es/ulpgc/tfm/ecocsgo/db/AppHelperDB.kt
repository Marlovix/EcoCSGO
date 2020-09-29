package es.ulpgc.tfm.ecocsgo.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import es.ulpgc.tfm.ecocsgo.model.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AppHelperDB(private val helper: AppDatabase?) {
    private var database: SQLiteDatabase? = null

    private val columnsWeapon = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_COST_FIELD,
        AppDatabase.KEY_NAME_FIELD,
        AppDatabase.KEY_CATEGORY_FIELD,
        AppDatabase.KEY_ITEM_FIELD,
        AppDatabase.KEY_REWARD_FIELD,
        AppDatabase.KEY_TEAM_FIELD
    )
    private val columnsUtility = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_COST_FIELD,
        AppDatabase.KEY_NAME_FIELD,
        AppDatabase.KEY_CATEGORY_FIELD,
        AppDatabase.KEY_ITEM_FIELD,
        AppDatabase.KEY_TEAM_FIELD
    )
    private val columnsGrenade = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_COST_FIELD,
        AppDatabase.KEY_NAME_FIELD,
        AppDatabase.KEY_CATEGORY_FIELD,
        AppDatabase.KEY_ITEM_FIELD,
        AppDatabase.KEY_TEAM_FIELD
    )
    private val columnsEconomy = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_BEGINNING_FIELD,
        AppDatabase.KEY_DEFUSE_BONUS_FIELD,
        AppDatabase.KEY_EXPLOSION_BONUS_FIELD,
        AppDatabase.KEY_GRENADE_KILL_FIELD,
        AppDatabase.KEY_KILL_PARTNER_PENALTY_FIELD,
        AppDatabase.KEY_KNIFE_KILL_FIELD,
        AppDatabase.KEY_LEAVING_GAME_FIELD,
        AppDatabase.KEY_MAX_FIELD,
        AppDatabase.KEY_PLANT_BONUS_FIELD
    )
    private val columnsDefeat = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_ORDER_FIELD,
        AppDatabase.KEY_BONUS_FIELD
    )
    private val columnsVictory = arrayOf<String>(
        AppDatabase.KEY_ID_FIELD,
        AppDatabase.KEY_TYPE_FIELD,
        AppDatabase.KEY_BONUS_FIELD
    )

    fun open(): AppHelperDB {
        database = helper?.writableDatabase
        return this
    }

    fun close() {
        helper?.close()
    }

    fun createWeapon(weapon: Weapon): Long {
        val contentDB = weaponMapper(weapon)
        contentDB.getAsInteger(AppDatabase.KEY_ITEM_FIELD)

        if (equipmentAlreadyExists(weapon.numeration, weapon.name)) {
            val selectionArgs = arrayOf(
                weapon.numeration.category.id.toString(),
                weapon.numeration.item.toString(), weapon.name
            )
            database!!.delete(
                AppDatabase.KEY_WEAPON_TABLE,
                AppDatabase.SELECTION_NUMERATION + " AND " +
                        AppDatabase.SELECTION_NAME, selectionArgs
            )
        }

        return database!!.insert(AppDatabase.KEY_WEAPON_TABLE, null, contentDB)
    }

    fun createUtility(utility: Equipment): Long {
        val contentDB = equipmentMapper(utility)

        if (equipmentAlreadyExists(utility.numeration)) {
            val selectionArgs = arrayOf(
                utility.numeration.category.id.toString(), utility.numeration.item.toString()
            )
            database!!.delete(
                AppDatabase.KEY_UTILITY_TABLE,
                AppDatabase.SELECTION_NUMERATION, selectionArgs
            )
        }

        return database!!.insert(AppDatabase.KEY_UTILITY_TABLE, null, contentDB)
    }

    fun createGrenade(grenade: Grenade): Long {
        val contentDB = equipmentMapper(grenade)

        if (equipmentAlreadyExists(grenade.numeration)) {
            val selectionArgs = arrayOf(
                grenade.numeration.category.id.toString(), grenade.numeration.item.toString()
            )
            database!!.delete(
                AppDatabase.KEY_GRENADE_TABLE,
                AppDatabase.SELECTION_NUMERATION, selectionArgs
            )
        }

        return database!!.insert(AppDatabase.KEY_GRENADE_TABLE, null, contentDB)
    }

    fun createEconomy(economy: EconomyGame): Long {
        val contentDB = economyMapper(economy)

        database!!.delete(AppDatabase.KEY_DEFEAT_TABLE, null, null)
        database!!.delete(AppDatabase.KEY_VICTORY_TABLE, null, null)

        var order = 1
        for (bonus in economy.defeatBonus) {
            createDefeat(order, bonus)
            order++
        }

        for (victory in economy.victory) {
            createVictory(victory.key, victory.value)
        }

        database!!.delete(AppDatabase.KEY_ECONOMY_TABLE, null, null)
        return database!!.insert(AppDatabase.KEY_ECONOMY_TABLE, null, contentDB)
    }

    private fun createDefeat(order: Int, bonus: Int): Long {
        val contentDB = ContentValues()
        contentDB.put(AppDatabase.KEY_ORDER_FIELD, order)
        contentDB.put(AppDatabase.KEY_BONUS_FIELD, bonus)

        return database!!.insert(AppDatabase.KEY_DEFEAT_TABLE, null, contentDB)
    }

    private fun createVictory(type: TypeVictoryGameEnum, bonus: Int): Long {
        val contentDB = ContentValues()
        contentDB.put(AppDatabase.KEY_TYPE_FIELD, type.name)
        contentDB.put(AppDatabase.KEY_BONUS_FIELD, bonus)

        return database!!.insert(AppDatabase.KEY_VICTORY_TABLE, null, contentDB)
    }

    fun equipmentAlreadyExists(numeration: EquipmentNumeration): Boolean {
        val table = when (numeration.category) {
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE -> AppDatabase.KEY_WEAPON_TABLE
            EquipmentCategoryEnum.GEAR -> AppDatabase.KEY_UTILITY_TABLE
            EquipmentCategoryEnum.GRENADE -> AppDatabase.KEY_GRENADE_TABLE
            else -> ""
        }

        val fields = when (numeration.category) {
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE -> columnsWeapon
            EquipmentCategoryEnum.GEAR -> columnsUtility
            EquipmentCategoryEnum.GRENADE -> columnsGrenade
            else -> emptyArray()
        }

        val selectionArgs = arrayOf(numeration.category.id.toString(), numeration.item.toString())

        val cursor = database!!.query(
            true, table, fields, AppDatabase.SELECTION_NUMERATION,
            selectionArgs, null, null, null, null
        )

        val equipmentAlreadyExists = cursor.count != 0

        cursor.close()

        return equipmentAlreadyExists
    }

    fun equipmentAlreadyExists(numeration: EquipmentNumeration, name: String): Boolean {
        val table = when (numeration.category) {
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE -> AppDatabase.KEY_WEAPON_TABLE
            EquipmentCategoryEnum.GEAR -> AppDatabase.KEY_UTILITY_TABLE
            EquipmentCategoryEnum.GRENADE -> AppDatabase.KEY_GRENADE_TABLE
            else -> ""
        }

        val fields = when (numeration.category) {
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE -> columnsWeapon
            EquipmentCategoryEnum.GEAR -> columnsUtility
            EquipmentCategoryEnum.GRENADE -> columnsGrenade
            else -> emptyArray()
        }

        val selectionArgs = arrayOf(
            numeration.category.id.toString(), numeration.item.toString(), name
        )

        val cursor = database!!.query(
            true, table, fields,
            AppDatabase.SELECTION_NUMERATION + " AND " +
                    AppDatabase.SELECTION_NAME,
            selectionArgs, null, null, null, null
        )

        val equipmentAlreadyExists = cursor.count != 0

        cursor.close()

        return equipmentAlreadyExists
    }

    fun fetchWeaponByNumeration(
        numeration: EquipmentNumeration,
        team: EquipmentTeamEnum = EquipmentTeamEnum.BOTH
    ): Weapon? {
        val selectionArgs = arrayOf(
            numeration.category.id.toString(), numeration.item.toString(), team.name
        )

        val cursor = database!!.query(
            true, AppDatabase.KEY_WEAPON_TABLE,
            columnsWeapon, AppDatabase.SELECTION_NUMERATION + " AND " +
                    AppDatabase.SELECTION_TEAM, selectionArgs,
            null, null, null, null
        )

        cursor?.moveToFirst()

        val name = cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_NAME_FIELD))
        val cost = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_COST_FIELD))
        val reward = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_REWARD_FIELD))

        cursor.close()

        return if (numeration.category == EquipmentCategoryEnum.PISTOL)
            SecondaryWeapon(name, team, numeration, cost, reward)
        else MainWeapon(name, team, numeration, cost, reward)
    }

    fun fetchUtilityEquipment(): List<Equipment> {
        val cursor = database!!.query(
            AppDatabase.KEY_UTILITY_TABLE,
            columnsUtility, null, null,
            null, null, null, null
        )

        val results = ArrayList<Equipment>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name = cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_NAME_FIELD))
                val cost = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_COST_FIELD))
                val item = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_ITEM_FIELD))
                val numeration = EquipmentNumeration(item, EquipmentCategoryEnum.GEAR)
                val team = EquipmentTeamEnum.valueOf(
                    cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_TEAM_FIELD))
                )

                when (item) {
                    1 -> results.add(Vest(name, team, numeration, cost))
                    2 -> results.add(Helmet(name, team, numeration, cost))
                    3 -> results.add(Zeus(name, team, numeration, cost))
                    else -> results.add(DefuseKit(name, team, numeration, cost))
                }

                cursor.moveToNext()
            }
        }

        cursor.close()

        return results
    }

    fun fetchMainWeapons(): EnumMap<EquipmentCategoryEnum, List<MainWeapon>> {
        val selectionArgs = arrayOf("2", "3", "4")

        val cursor = database!!.query(
            AppDatabase.KEY_WEAPON_TABLE,
            columnsWeapon, AppDatabase.SELECTION_CATEGORY + " OR " +
                    AppDatabase.SELECTION_CATEGORY + " OR " +
                    AppDatabase.SELECTION_CATEGORY, selectionArgs,
            null, null, null, null
        )

        val heavy: ArrayList<MainWeapon> = ArrayList()
        val smg: ArrayList<MainWeapon> = ArrayList()
        val rifle: ArrayList<MainWeapon> = ArrayList()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name = cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_NAME_FIELD))
                val cost = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_COST_FIELD))
                val reward = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_REWARD_FIELD))
                val item = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_ITEM_FIELD))
                val category = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_CATEGORY_FIELD))
                val team = EquipmentTeamEnum.valueOf(
                    cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_TEAM_FIELD))
                )

                val numeration: EquipmentNumeration
                when (category) {
                    2 -> {
                        numeration = EquipmentNumeration(item, EquipmentCategoryEnum.HEAVY)
                        heavy.add(MainWeapon(name, team, numeration, cost, reward))
                    }
                    3 -> {
                        numeration = EquipmentNumeration(item, EquipmentCategoryEnum.SMG)
                        smg.add(MainWeapon(name, team, numeration, cost, reward))
                    }
                    4 -> {
                        numeration = EquipmentNumeration(item, EquipmentCategoryEnum.RIFLE)
                        rifle.add(MainWeapon(name, team, numeration, cost, reward))
                    }
                    else -> println("\nError load main weapon\n")
                }

                cursor.moveToNext()
            }
        }

        cursor.close()

        val results: EnumMap<EquipmentCategoryEnum, List<MainWeapon>> =
            EnumMap(EquipmentCategoryEnum::class.java)

        results[EquipmentCategoryEnum.HEAVY] = heavy
        results[EquipmentCategoryEnum.SMG] = smg
        results[EquipmentCategoryEnum.RIFLE] = rifle

        return results
    }

    fun fetchSecondaryWeapons(): EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>> {
        val selectionArgs = arrayOf("1")

        val cursor = database!!.query(
            AppDatabase.KEY_WEAPON_TABLE,
            columnsWeapon, AppDatabase.SELECTION_CATEGORY, selectionArgs,
            null, null, null, null
        )

        val pistol: ArrayList<SecondaryWeapon> = ArrayList()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name = cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_NAME_FIELD))
                val cost = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_COST_FIELD))
                val reward = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_REWARD_FIELD))
                val item = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_ITEM_FIELD))
                val numeration = EquipmentNumeration(item, EquipmentCategoryEnum.PISTOL)
                val team = EquipmentTeamEnum.valueOf(
                    cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_TEAM_FIELD))
                )

                pistol.add(SecondaryWeapon(name, team, numeration, cost, reward))

                cursor.moveToNext()
            }
        }

        cursor.close()

        val results: EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>> =
            EnumMap(EquipmentCategoryEnum::class.java)

        results[EquipmentCategoryEnum.PISTOL] = pistol

        return results
    }

    fun fetchEconomyGame(): EconomyGame {
        val cursor = database!!.query(
            AppDatabase.KEY_ECONOMY_TABLE,
            columnsEconomy, null, null,
            null, null, null, null
        )

        cursor.moveToFirst()

        val beginning = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_BEGINNING_FIELD))
        val defeatBonus = fetchDefeatBonus()
        val defuseBonus = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_DEFUSE_BONUS_FIELD))
        val explosionBonus =
            cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_EXPLOSION_BONUS_FIELD))
        val grenadeKill = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_GRENADE_KILL_FIELD))
        val killPartnerPenalty =
            cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_KILL_PARTNER_PENALTY_FIELD))
        val knifeKill = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_KNIFE_KILL_FIELD))
        val leavingGame = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_LEAVING_GAME_FIELD))
        val max = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_MAX_FIELD))
        val plantBonus = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_PLANT_BONUS_FIELD))
        val victory = fetchVictoryTypes()

        cursor.close()

        return EconomyGame(
            beginning, defeatBonus, defuseBonus,
            explosionBonus, grenadeKill, killPartnerPenalty, knifeKill, leavingGame,
            max, plantBonus, victory
        )
    }

    private fun fetchVictoryTypes(): HashMap<TypeVictoryGameEnum, Int> {
        val cursor = database!!.query(
            AppDatabase.KEY_VICTORY_TABLE,
            columnsVictory, null, null,
            null, null, null, null
        )

        val victoryTypes = HashMap<TypeVictoryGameEnum, Int>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val type =
                    TypeVictoryGameEnum.valueOf(cursor.getString(cursor.getColumnIndex(AppDatabase.KEY_TYPE_FIELD)))
                val bonus = cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_BONUS_FIELD))
                victoryTypes[type] = bonus
                cursor.moveToNext()
            }
        }

        cursor.close()

        return victoryTypes
    }

    private fun fetchDefeatBonus(): ArrayList<Int> {
        val cursor = database!!.query(
            AppDatabase.KEY_DEFEAT_TABLE,
            columnsDefeat, null, null,
            null, null, AppDatabase.KEY_ORDER_FIELD, null
        )

        val defeatBonus = ArrayList<Int>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                defeatBonus.add(cursor.getInt(cursor.getColumnIndex(AppDatabase.KEY_BONUS_FIELD)))
                cursor.moveToNext()
            }
        }

        cursor.close()

        return defeatBonus
    }

    private fun equipmentMapper(equipment: Equipment): ContentValues {
        val initialValues = ContentValues()
        initialValues.put(AppDatabase.KEY_COST_FIELD, equipment.cost)
        initialValues.put(AppDatabase.KEY_NAME_FIELD, equipment.name)
        initialValues.put(AppDatabase.KEY_CATEGORY_FIELD, equipment.numeration.category.id)
        initialValues.put(AppDatabase.KEY_ITEM_FIELD, equipment.numeration.item)
        initialValues.put(AppDatabase.KEY_TEAM_FIELD, equipment.team.name)

        return initialValues
    }

    private fun weaponMapper(weapon: Weapon): ContentValues {
        val initialValues = equipmentMapper(weapon)
        initialValues.put(AppDatabase.KEY_REWARD_FIELD, weapon.reward)
        return initialValues
    }

    private fun economyMapper(economy: EconomyGame): ContentValues {
        val initialValues = ContentValues()
        initialValues.put(AppDatabase.KEY_BEGINNING_FIELD, economy.beginning)
        initialValues.put(AppDatabase.KEY_DEFUSE_BONUS_FIELD, economy.defuseBonus)
        initialValues.put(AppDatabase.KEY_EXPLOSION_BONUS_FIELD, economy.explosionBonus)
        initialValues.put(AppDatabase.KEY_GRENADE_KILL_FIELD, economy.grenadeKill)
        initialValues.put(AppDatabase.KEY_KILL_PARTNER_PENALTY_FIELD, economy.killPartnerPenalty)
        initialValues.put(AppDatabase.KEY_KNIFE_KILL_FIELD, economy.knifeKill)
        initialValues.put(AppDatabase.KEY_LEAVING_GAME_FIELD, economy.leavingGame)
        initialValues.put(AppDatabase.KEY_MAX_FIELD, economy.max)
        initialValues.put(AppDatabase.KEY_PLANT_BONUS_FIELD, economy.plantBonus)
        return initialValues
    }

}