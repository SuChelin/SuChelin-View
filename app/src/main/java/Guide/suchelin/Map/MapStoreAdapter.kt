package Guide.suchelin.Map

import Guide.suchelin.DataClass.StoreDataClassMap
import Guide.suchelin.DataClass.StoreDetailDataClass
import Guide.suchelin.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MapStoreAdapter(var storeList: ArrayList<StoreDataClassMap>, val allScore: HashMap<String, Long>, val control: MapControl)
    : RecyclerView.Adapter<MapStoreAdapter.MapStoreViewHolder>(){
    class MapStoreViewHolder(itemView: View, val allScore: HashMap<String, Long>, val control: MapControl) : RecyclerView.ViewHolder(itemView){
        val storeImage = itemView.findViewById<ImageView>(R.id.map_store_imageView)
        val storeTitle = itemView.findViewById<TextView>(R.id.map_store_title_textView)
        val storeDetail = itemView.findViewById<TextView>(R.id.map_store_detail_textView)
        val storeRank = itemView.findViewById<ImageView>(R.id.map_store_rank_imageView)
        val storeMichelin = itemView.findViewById<ImageView>(R.id.map_store_michelin_imageView)
        val storeParent = itemView.findViewById<RelativeLayout>(R.id.map_store_parent)
        val suwonParent = itemView.findViewById<LinearLayout>(R.id.map_suwon_parent)

        fun bind(storeItem: StoreDataClassMap, position: Int){
            if(position == 0){
                storeParent.visibility = View.GONE
                suwonParent.visibility = View.VISIBLE
                return
            } else {
                storeParent.visibility = View.VISIBLE
                suwonParent.visibility = View.GONE
            }

            storeTitle.text = storeItem.name
            storeDetail.text = storeItem.detail

            Glide.with(itemView)
                .load(storeItem.imageUrl)
                .centerCrop()
                .into(storeImage)

            Log.d("getAdapter", "$allScore")
            // 미슐랭 스타 아이콘 설정
            val thisScore = allScore.get(storeItem.id.toString())
            Log.d("getAdapterScore", "${storeItem.id.toString()} : $thisScore")

            storeMichelin.visibility =
                if(setMichelinImage(thisScore!!) == null) View.GONE
                else {
                    storeMichelin.setImageResource(setMichelinImage(thisScore!!)!!)
                    View.VISIBLE
                }

            // 랭크 아이콘 설정
            storeRank.visibility =
                if(storeItem.rank == null) View.GONE
                else {
                    storeRank.setImageResource(storeItem.getRankImage()!!)
                    View.GONE
                }

            // 아이템 선택했을 경우 StoreDetail 페이지로 넘어감
            storeParent.setOnClickListener {
                control.startStoreDetailActivity(storeItem.id, storeItem.name, storeItem.latitude, storeItem.longitude, thisScore)
            }
        }

        private fun setMichelinImage(score: Long): Int?{
            return when(score.toInt()){
                in 200..99999 -> R.drawable.ic_michelin_three
                in 100..199 -> R.drawable.ic_michelin_two
                in 50..99 -> R.drawable.ic_michelin_one
                else -> null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapStoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.maplist_ltem,
            parent,
            false
        )
        return MapStoreViewHolder(view, allScore, control)
    }

    override fun onBindViewHolder(holder: MapStoreViewHolder, position: Int) {
        holder.bind(storeList[position], position)
    }

    override fun getItemCount(): Int = storeList.size


}