package Guide.suchelin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class DataSet(
    val imageUrl: String = "",
    val name: String = "",
    val detail: String = "",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val score : Int = 0
//distance
)
