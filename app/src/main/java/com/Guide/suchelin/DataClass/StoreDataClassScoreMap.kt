package com.Guide.suchelin.DataClass

import com.Guide.suchelin.R

data class StoreDataClassScoreMap(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val detail: String,
    val latitude : Double,
    val longitude : Double,
    val michelin : Int? = 2,
    val rank : Int? = 2,
    val score: Long,
) {
    fun getMichelinImage(): Int? =
        when(score.toInt()){
            in 5..100 -> R.drawable.ic_michelin_three
            in 3..4 -> R.drawable.ic_michelin_two
            in 1..2 -> R.drawable.ic_michelin_one
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
