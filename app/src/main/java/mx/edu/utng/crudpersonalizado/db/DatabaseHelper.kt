package mx.edu.utng.crudpersonalizado.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import mx.edu.utng.crudpersonalizado.data.model.Item

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(context, "items.db", null, 1) {

    companion object {
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            if (INSTANCE == null) {
                INSTANCE = DatabaseHelper(context.applicationContext)
            }
            return INSTANCE!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE items (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            description TEXT NOT NULL,
            price REAL NOT NULL,
            category TEXT NOT NULL,
            stock INTEGER NOT NULL,
            imageUrl TEXT
        )
    """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS items")
        onCreate(db)
    }

    fun insertItem(item: Item): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
            put("price", item.price)
            put("category", item.category)
            put("stock", item.stock)
            put("imageUrl", item.imageUrl)
        }
        val result = db.insert("items", null, values)
        db.close()
        return result != -1L
    }

    fun getAllItems(): List<Item> {
        val itemList = mutableListOf<Item>()
        try {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM items", null)

            while (cursor.moveToNext()) {
                val item = Item(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    description = cursor.getString(2),
                    price = cursor.getDouble(3),
                    category = cursor.getString(4),
                    stock = cursor.getInt(5),
                    imageUrl = cursor.getString(6)
                )
                itemList.add(item)
            }
            cursor.close()
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return itemList
    }

    fun updateItem(item: Item): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
            put("price", item.price)
            put("category", item.category)
            put("stock", item.stock)
            put("imageUrl", item.imageUrl)
        }
        val result = db.update("items", values, "id=?", arrayOf(item.id.toString()))
        db.close()
        return result > 0
    }


    fun deleteItem(itemId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("items", "id=?", arrayOf(itemId.toString()))
        db.close()
        return result > 0
    }

}
