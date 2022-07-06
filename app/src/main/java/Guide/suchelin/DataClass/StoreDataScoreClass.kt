package Guide.suchelin.DataClass

import Guide.suchelin.R

data class StoreDataScoreClass(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val detail: String,
    val score: Long,
    val latitude : Double,
    val longitude : Double,
    val michelin : Int? = 2,
    val rank : Int? = 2
){
    fun getMichelinImage(): Int? =
        when(score.toInt()){
            in 5..100 -> R.drawable.ic_michelin_three
            in 3..4 -> R.drawable.ic_michelin_two
            in 1..2 -> R.drawable.ic_michelin_one
            else -> null
        }

    fun getMichelinVoteImage(): Int? =
        when(michelin){
            1 -> R.drawable.ic_vote_michelin_one
            2 -> R.drawable.ic_vote_michelin_two
            3 -> R.drawable.ic_vote_michelin_three
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
