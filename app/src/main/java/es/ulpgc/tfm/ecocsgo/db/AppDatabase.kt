package es.ulpgc.tfm.ecocsgo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class AppDatabase(var context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_WEAPON_TABLE)
        db.execSQL(CREATE_UTILITY_TABLE)
        db.execSQL(CREATE_GRENADE_TABLE)
        db.execSQL(CREATE_ECONOMY_TABLE)
        db.execSQL(CREATE_DEFEAT_TABLE)
        db.execSQL(CREATE_VICTORY_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $KEY_WEAPON_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $KEY_UTILITY_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $KEY_GRENADE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $KEY_ECONOMY_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $KEY_DEFEAT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $KEY_VICTORY_TABLE")
        onCreate(db)
    }

    fun listaPuntuaciones(): Array<String> {
        val result: Array<String> = emptyArray()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $KEY_WEAPON_TABLE", null)
        while (cursor.moveToNext()) {
            cursor.getInt(0).toString()
            Toast.makeText(context, cursor.getString(2), Toast.LENGTH_SHORT).show()
            break;
        }
        cursor.close()
        db.close()
        return result
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ecocsgo"

        const val KEY_WEAPON_TABLE = "weapon"
        const val KEY_UTILITY_TABLE = "utility"
        const val KEY_GRENADE_TABLE = "grenade"

        const val KEY_ID_FIELD = "_id"
        const val KEY_COST_FIELD = "cost"
        const val KEY_NAME_FIELD = "name"
        const val KEY_CATEGORY_FIELD = "category"
        const val KEY_ITEM_FIELD = "item"
        const val KEY_REWARD_FIELD = "reward"
        const val KEY_TEAM_FIELD = "team"

        private const val CREATE_WEAPON_TABLE =
            ("CREATE TABLE " + KEY_WEAPON_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_COST_FIELD + " INTEGER NOT NULL, "
                    + KEY_NAME_FIELD + " VARCHAR(30) NOT NULL, "
                    + KEY_CATEGORY_FIELD + " INTEGER NOT NULL, "
                    + KEY_ITEM_FIELD + " INTEGER NOT NULL, "
                    + KEY_REWARD_FIELD + " INTEGER NOT NULL, "
                    + KEY_TEAM_FIELD + " VARCHAR(2) NOT NULL);")

        private const val CREATE_UTILITY_TABLE =
            ("CREATE TABLE " + KEY_UTILITY_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_COST_FIELD + " INTEGER NOT NULL, "
                    + KEY_NAME_FIELD + " VARCHAR(30) NOT NULL, "
                    + KEY_CATEGORY_FIELD + " INTEGER NOT NULL, "
                    + KEY_ITEM_FIELD + " INTEGER NOT NULL, "
                    + KEY_TEAM_FIELD + " VARCHAR(2) NOT NULL);")

        private const val CREATE_GRENADE_TABLE =
            ("CREATE TABLE " + KEY_GRENADE_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_COST_FIELD + " INTEGER NOT NULL, "
                    + KEY_NAME_FIELD + " VARCHAR(30) NOT NULL, "
                    + KEY_CATEGORY_FIELD + " INTEGER NOT NULL, "
                    + KEY_ITEM_FIELD + " INTEGER NOT NULL, "
                    + KEY_TEAM_FIELD + " VARCHAR(2) NOT NULL);")

        const val KEY_ECONOMY_TABLE = "economy"
        const val KEY_BEGINNING_FIELD = "beginning"
        const val KEY_DEFUSE_BONUS_FIELD = "defuse_bonus"
        const val KEY_EXPLOSION_BONUS_FIELD = "explosion_bonus"
        const val KEY_GRENADE_KILL_FIELD = "grenade_kill"
        const val KEY_KILL_PARTNER_PENALTY_FIELD = "kill_partner_penalty"
        const val KEY_KNIFE_KILL_FIELD = "knife_kill"
        const val KEY_LEAVING_GAME_FIELD = "leaving_game"
        const val KEY_MAX_FIELD = "max"
        const val KEY_PLANT_BONUS_FIELD = "plant_bonus"

        private const val CREATE_ECONOMY_TABLE =
            ("CREATE TABLE " + KEY_ECONOMY_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_BEGINNING_FIELD + " INTEGER NOT NULL, "
                    + KEY_DEFUSE_BONUS_FIELD + " INTEGER NOT NULL, "
                    + KEY_EXPLOSION_BONUS_FIELD + " INTEGER NOT NULL, "
                    + KEY_GRENADE_KILL_FIELD + " INTEGER NOT NULL, "
                    + KEY_KILL_PARTNER_PENALTY_FIELD + " INTEGER NOT NULL, "
                    + KEY_KNIFE_KILL_FIELD + " INTEGER NOT NULL, "
                    + KEY_LEAVING_GAME_FIELD + " INTEGER NOT NULL, "
                    + KEY_MAX_FIELD + " INTEGER NOT NULL, "
                    + KEY_PLANT_BONUS_FIELD + " INTEGER NOT NULL);")

        const val KEY_VICTORY_TABLE = "victory"
        const val KEY_DEFEAT_TABLE = "defeat"

        const val KEY_ORDER_FIELD = "order_bonus"
        const val KEY_TYPE_FIELD = "type"
        const val KEY_BONUS_FIELD = "bonus"

        private const val CREATE_DEFEAT_TABLE =
            ("CREATE TABLE " + KEY_DEFEAT_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_ORDER_FIELD + " INTEGER NOT NULL, "
                    + KEY_BONUS_FIELD + " INTEGER NOT NULL);")

        private const val CREATE_VICTORY_TABLE =
            ("CREATE TABLE " + KEY_VICTORY_TABLE + " ("
                    + KEY_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_TYPE_FIELD + " VARCHAR(10) NOT NULL, "
                    + KEY_BONUS_FIELD + " INTEGER NOT NULL);")
    }
}
