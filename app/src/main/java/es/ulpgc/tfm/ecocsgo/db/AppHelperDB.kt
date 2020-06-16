package es.ulpgc.tfm.ecocsgo.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import es.ulpgc.tfm.ecocsgo.model.*
import java.sql.SQLException

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

    private fun existData(numeration: EquipmentNumeration) : Boolean {
        return false
    }

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

        if(equipmentAlreadyExists(weapon.numeration, weapon.name)){
            val selectionArgs = arrayOf(weapon.numeration.category.id.toString(),
                weapon.numeration.item.toString(), weapon.name)
            database!!.delete(AppDatabase.KEY_WEAPON_TABLE,
                AppDatabase.SELECTION_NUMERATION + " AND " +
                        AppDatabase.SELECTION_NAME, selectionArgs)
        }

        return database!!.insert(AppDatabase.KEY_WEAPON_TABLE, null, contentDB)
    }

    fun createUtility(utility: Equipment): Long {
        val contentDB = equipmentMapper(utility)

        if(equipmentAlreadyExists(utility.numeration)){
            val selectionArgs = arrayOf(
                utility.numeration.category.id.toString(), utility.numeration.item.toString())
            database!!.delete(AppDatabase.KEY_UTILITY_TABLE,
                AppDatabase.SELECTION_NUMERATION, selectionArgs)
        }

        return database!!.insert(AppDatabase.KEY_UTILITY_TABLE, null, contentDB)
    }

    fun createGrenade(grenade: Grenade): Long {
        val contentDB = equipmentMapper(grenade)

        if(equipmentAlreadyExists(grenade.numeration)){
            val selectionArgs = arrayOf(
                grenade.numeration.category.id.toString(), grenade.numeration.item.toString())
            database!!.delete(AppDatabase.KEY_GRENADE_TABLE,
                AppDatabase.SELECTION_NUMERATION, selectionArgs)
        }

        return database!!.insert(AppDatabase.KEY_GRENADE_TABLE, null, contentDB)
    }

    fun createEconomy(economy: EconomyGame): Long {
        val contentDB = economyMapper(economy)

        database!!.delete(AppDatabase.KEY_DEFEAT_TABLE, null, null)
        database!!.delete(AppDatabase.KEY_VICTORY_TABLE, null, null)

        var order = 1
        for(bonus in economy.defeatBonus){
            createDefeat(order, bonus)
            order++
        }

        for (victory in economy.victory){
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

    @Throws(SQLException::class)
    fun equipmentAlreadyExists(numeration: EquipmentNumeration): Boolean {
        val table = when(numeration.category){
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE-> AppDatabase.KEY_WEAPON_TABLE
            EquipmentCategoryEnum.GEAR -> AppDatabase.KEY_UTILITY_TABLE
            EquipmentCategoryEnum.GRENADE -> AppDatabase.KEY_GRENADE_TABLE
            else -> ""
        }

        val fields = when(numeration.category){
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE-> columnsWeapon
            EquipmentCategoryEnum.GEAR -> columnsUtility
            EquipmentCategoryEnum.GRENADE -> columnsGrenade
            else -> emptyArray()
        }

        val selectionArgs = arrayOf(numeration.category.id.toString(), numeration.item.toString())

        val cursor = database!!.query(true, table, fields, AppDatabase.SELECTION_NUMERATION,
            selectionArgs, null, null, null, null)

        val equipmentAlreadyExists = cursor.count != 0

        cursor.close()

        return equipmentAlreadyExists
    }

    fun equipmentAlreadyExists(numeration: EquipmentNumeration, name: String): Boolean {
        val table = when(numeration.category){
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE-> AppDatabase.KEY_WEAPON_TABLE
            EquipmentCategoryEnum.GEAR -> AppDatabase.KEY_UTILITY_TABLE
            EquipmentCategoryEnum.GRENADE -> AppDatabase.KEY_GRENADE_TABLE
            else -> ""
        }

        val fields = when(numeration.category){
            EquipmentCategoryEnum.PISTOL,
            EquipmentCategoryEnum.HEAVY,
            EquipmentCategoryEnum.SMG,
            EquipmentCategoryEnum.RIFLE-> columnsWeapon
            EquipmentCategoryEnum.GEAR -> columnsUtility
            EquipmentCategoryEnum.GRENADE -> columnsGrenade
            else -> emptyArray()
        }

        val selectionArgs = arrayOf(
            numeration.category.id.toString(), numeration.item.toString(), name)

        val cursor = database!!.query(true, table, fields,
            AppDatabase.SELECTION_NUMERATION + " AND " +
                    AppDatabase.SELECTION_NAME,
            selectionArgs, null, null, null, null)

        val equipmentAlreadyExists = cursor.count != 0

        cursor.close()

        return equipmentAlreadyExists
    }

    @Throws(SQLException::class)
    fun fetchWeaponByNumeration(numeration: EquipmentNumeration,
                                team : EquipmentTeamEnum = EquipmentTeamEnum.BOTH): Weapon? {
        val selectionArgs = arrayOf(
            numeration.category.id.toString(), numeration.item.toString(), team.name)

        val cursor = database!!.query(true, AppDatabase.KEY_WEAPON_TABLE,
            columnsWeapon, AppDatabase.SELECTION_NUMERATION + " AND " +
                    AppDatabase.SELECTION_TEAM, selectionArgs,
            null, null, null, null)

        cursor?.moveToFirst()

        val nameIndex = cursor.getColumnIndex(AppDatabase.KEY_NAME_FIELD)
        val costIndex = cursor.getColumnIndex(AppDatabase.KEY_COST_FIELD)
        val rewardIndex = cursor.getColumnIndex(AppDatabase.KEY_REWARD_FIELD)

        val name = cursor.getString(nameIndex)
        val cost = cursor.getInt(costIndex)
        val reward = cursor.getInt(rewardIndex)

        cursor.close()

        return if(numeration.category == EquipmentCategoryEnum.PISTOL)
            SecondaryWeapon(name, team, numeration, cost, reward)
        else MainWeapon(name, team, numeration, cost, reward)
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