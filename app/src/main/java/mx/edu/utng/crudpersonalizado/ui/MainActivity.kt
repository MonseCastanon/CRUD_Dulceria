package mx.edu.utng.crudpersonalizado.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.utng.crudpersonalizado.R
import mx.edu.utng.crudpersonalizado.data.db.DatabaseHelper
import mx.edu.utng.crudpersonalizado.data.model.Item
import mx.edu.utng.crudpersonalizado.detail.DetailActivity
import mx.edu.utng.crudpersonalizado.ui.form.FormActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var itemList = listOf<Item>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper.getInstance(this)
        println("DatabaseHelper inicializado correctamente: $databaseHelper")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Inventario de Dulcería Monse :3"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        val addButton = findViewById<FloatingActionButton>(R.id.addButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargar elementos de la base de datos
        loadItems()

        // Buscar elementos en tiempo real
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        // Botón para agregar un nuevo elemento
        addButton.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadItems() // Recargar la lista cuando volvemos a MainActivity
    }

    private fun loadItems() {
        try {
            itemList = databaseHelper.getAllItems()
            adapter = ItemAdapter(itemList) { selectedItem ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("ITEM_ID", selectedItem.id)
                    putExtra("ITEM_NAME", selectedItem.name)
                    putExtra("ITEM_DESCRIPTION", selectedItem.description)
                    putExtra("ITEM_PRICE", selectedItem.price)
                    putExtra("ITEM_CATEGORY", selectedItem.category)
                    putExtra("ITEM_STOCK", selectedItem.stock)
                    putExtra("ITEM_IMAGE_URL", selectedItem.imageUrl)
                }
                startActivity(intent)
            }
            findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filterList(query: String?) {
        val filtered = if (query.isNullOrEmpty()) itemList else itemList.filter {
            it.name.contains(query, ignoreCase = true)
        }
        adapter = ItemAdapter(filtered) { selectedItem ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("ITEM_ID", selectedItem.id)
                putExtra("ITEM_NAME", selectedItem.name)
                putExtra("ITEM_DESCRIPTION", selectedItem.description)
                putExtra("ITEM_PRICE", selectedItem.price)
                putExtra("ITEM_CATEGORY", selectedItem.category)
                putExtra("ITEM_STOCK", selectedItem.stock)
                putExtra("ITEM_IMAGE_URL", selectedItem.imageUrl)
            }
            startActivity(intent)
        }
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter
    }
}
