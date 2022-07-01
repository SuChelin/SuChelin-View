package Guide.suchelin.DataClass

import Guide.suchelin.R

data class StoreDataClassMap(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val detail: String,
    val latitude : Double,
    val longitude : Double,
    val michelin : Int? = 1,
    val rank : Int? = 1
) {
    fun getMichelinImage(): Int? =
        when(michelin){
            1 -> R.drawable.ic_michelin_one
            2 -> R.drawable.ic_michelin_two
            3 -> R.drawable.ic_michelin_three
            else -> null
        }

    fun getRankImage(): Int? =
        when(rank){
            1 -> R.drawable.ic_one_rank
            2 -> R.drawable.ic_two_rank
            3 -> R.drawable.ic_three_rank
            else -> null
        }
}
