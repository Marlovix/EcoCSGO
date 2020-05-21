package es.ulpgc.tfm.ecocsgo.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import es.ulpgc.tfm.ecocsgo.model.*

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
        return database!!.insert(AppDatabase.KEY_WEAPON_TABLE, null, contentDB)
    }

    fun createUtility(utility: Equipment): Long {
        val contentDB = equipmentMapper(utility)
        return database!!.insert(AppDatabase.KEY_UTILITY_TABLE, null, contentDB)
    }

    fun createGrenade(grenade: Grenade): Long {
        val contentDB = equipmentMapper(grenade)
        return database!!.insert(AppDatabase.KEY_GRENADE_TABLE, null, contentDB)
    }

    fun createEconomy(economy: EconomyGame): Long {
        val contentDB = economyMapper(economy)

        var order = 1
        for(bonus in economy.defeatBonus){
            createDefeat(order, bonus)
            order++
        }

        for (victory in economy.victory){
            createVictory(victory.key, victory.value)
        }

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

/*
    @Throws(SQLException::class)
    fun fetchCustomerByID(idCustomer: Int): Cursor? {
        val cursor: Cursor? = if (idCustomer == 0) database!!.query(
            AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
            null, null, null, null, null
        ) else database!!.query(
            true,
            AppDatabase.KEY_PRODUCT_TABLE,
            columnsProduct,
            AppDatabase.KEY_ID_CUSTOMER.toString() + " = " + idCustomer,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }

    @Throws(SQLException::class)
    fun fetchProductByID(idProduct: Int): Cursor? {
        val cursor: Cursor? = if (idProduct == 0) database!!.query(
            AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
            null, null, null, null, null
        ) else database!!.query(
            true, AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
            AppDatabase.KEY_ID_PRODUCT.toString() + " = " + idProduct, null, null, null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }

    @Throws(SQLException::class)
    fun fetchOrderByID(idOrder: Int): Cursor? {
        val cursor: Cursor? = if (idOrder == 0) database!!.query(
            AppDatabase.KEY_ORDER_TABLE, columnsOrder,
            null, null, null, null, null
        ) else database!!.query(
            true, AppDatabase.KEY_ORDER_TABLE, columnsOrder,
            AppDatabase.KEY_ID_ORDER.toString() + " = " + idOrder, null, null, null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun fetchAllWeapons(): Cursor? {

    }

    fun fetchAllCustomers(): Cursor? {
        val orderBy: String = AppDatabase.KEY_NAME_PRODUCT.toString() + " ASC"
        val cursor: Cursor? = database!!.query(
            AppDatabase.KEY_CUSTOMER_TABLE, columnsCustomer,
            null, null, null, null, orderBy
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun fetchAllProducts(): Cursor? {
        val orderBy: String = AppDatabase.KEY_NAME_PRODUCT.toString() + " ASC"
        val cursor: Cursor? = database!!.query(
            AppDatabase.KEY_PRODUCT_TABLE, columnsProduct,
            null, null, null, null, orderBy
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun fetchAllOrders(): Cursor? {
        val selectColumns = ("o." + AppDatabase.KEY_ID_ORDER.toString() + ","
                + AppDatabase.KEY_CODE_ORDER.toString() + ","
                + AppDatabase.KEY_DATE_ORDER.toString() + ","
                + AppDatabase.KEY_ID_CUSTOMER_ORDER.toString() + "," + "c." + AppDatabase.KEY_NAME_CUSTOMER.toString() + " AS customer_name,"
                + AppDatabase.KEY_ADDRESS_CUSTOMER.toString() + ","
                + AppDatabase.KEY_ID_PRODUCT_ORDER.toString() + "," + "p." + AppDatabase.KEY_NAME_PRODUCT.toString() + " AS product_name,"
                + AppDatabase.KEY_DESCRIPTION_PRODUCT.toString() + ","
                + AppDatabase.KEY_PRICE_PRODUCT.toString() + ","
                + AppDatabase.KEY_QUANTITY_ORDER)
        val selectSQL =
            ("SELECT " + selectColumns + " FROM " + AppDatabase.KEY_ORDER_TABLE + " AS o"
                    + " JOIN " + AppDatabase.KEY_CUSTOMER_TABLE + " AS c"
                    + " ON o." + AppDatabase.KEY_ID_CUSTOMER_ORDER + "=" + "c." + AppDatabase.KEY_ID_CUSTOMER
                    + " JOIN " + AppDatabase.KEY_PRODUCT_TABLE + " AS p"
                    + " ON o." + AppDatabase.KEY_ID_PRODUCT_ORDER + "=" + "p." + AppDatabase.KEY_ID_PRODUCT
                    + " ORDER BY"
                    + " c." + AppDatabase.KEY_NAME_CUSTOMER + ","
                    + " p." + AppDatabase.KEY_NAME_PRODUCT + ","
                    + " o." + AppDatabase.KEY_CODE_ORDER + " ASC")
        val cursor: Cursor? = database!!.rawQuery(selectSQL, null)
        cursor?.moveToFirst()
        return cursor
    }*/

    private fun equipmentMapper(equipment: Equipment): ContentValues {
        val initialValues = ContentValues()
        initialValues.put(AppDatabase.KEY_COST_FIELD, equipment.cost)
        initialValues.put(AppDatabase.KEY_NAME_FIELD, equipment.name)
        initialValues.put(AppDatabase.KEY_CATEGORY_FIELD, equipment.category.id)
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

/*
    fun updateCustomer(customer: Customer): Long {
        val id: Int = customer.getIdCustomer()
        val contentDB = customerMapper(customer)
        return database!!.update(AppDatabase.KEY_CUSTOMER_TABLE, contentDB, "_id=$id", null)
            .toLong()
    }

    fun updateProduct(product: Product): Long {
        val id: Int = product.getIdProduct()
        val contentDB = productMapper(product)
        return database!!.update(AppDatabase.KEY_PRODUCT_TABLE, contentDB, "_id=$id", null).toLong()
    }

    fun updateOrder(order: Order): Long {
        val id: Int = order.getIdOrder()
        val contentDB = orderMapper(order)
        return database!!.update(AppDatabase.KEY_ORDER_TABLE, contentDB, "_id=$id", null).toLong()
    }*/

}