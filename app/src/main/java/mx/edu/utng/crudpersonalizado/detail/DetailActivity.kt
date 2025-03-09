package mx.edu.utng.crudpersonalizado.detail

import com.bumptech.glide.Glide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.edu.utng.crudpersonalizado.R
import mx.edu.utng.crudpersonalizado.data.db.DatabaseHelper
import mx.edu.utng.crudpersonalizado.ui.form.FormActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        databaseHelper = DatabaseHelper.getInstance(this)

        val nameText = findViewById<TextView>(R.id.detailName)
        val descriptionText = findViewById<TextView>(R.id.detailDescription)
        val priceText = findViewById<TextView>(R.id.detailPrice)
        val categoryText = findViewById<TextView>(R.id.detailCategory)
        val stockText = findViewById<TextView>(R.id.detailStock)
        val imageView = findViewById<ImageView>(R.id.detailImage)

        val editButton = findViewById<Button>(R.id.editButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        // Recibir datos del intent
        itemId = intent.getIntExtra("ITEM_ID", -1)
        val name = intent.getStringExtra("ITEM_NAME") ?: ""
        val description = intent.getStringExtra("ITEM_DESCRIPTION") ?: ""
        val price = intent.getDoubleExtra("ITEM_PRICE", 0.0)
        val category = intent.getStringExtra("ITEM_CATEGORY") ?: ""
        val stock = intent.getIntExtra("ITEM_STOCK", 0)
        val imageUrl = intent.getStringExtra("ITEM_IMAGE_URL") ?: ""

        // Mostrar datos en la pantalla
        nameText.text = name
        descriptionText.text = description
        priceText.text = "Precio: $price"
        categoryText.text = "Categoría: $category"
        stockText.text = "Cantidad en Almacen: $stock"

        // Dentro de onCreate()
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)

        // Botón para editar
        editButton.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java).apply {
                putExtra("ITEM_ID", itemId)
                putExtra("ITEM_NAME", name)
                putExtra("ITEM_DESCRIPTION", description)
                putExtra("ITEM_PRICE", price)
                putExtra("ITEM_CATEGORY", category)
                putExtra("ITEM_STOCK", stock)
                putExtra("ITEM_IMAGE_URL", imageUrl)
            }
            startActivity(intent)
        }

        // Botón para eliminar
        deleteButton.setOnClickListener {
            databaseHelper.deleteItem(itemId)
            finish()
        }
    }
}