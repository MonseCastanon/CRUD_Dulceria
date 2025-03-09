package mx.edu.utng.crudpersonalizado.data.model

data class Item(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val imageUrl: String
)
