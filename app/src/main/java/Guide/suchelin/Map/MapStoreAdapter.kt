package Guide.suchelin.Map

import Guide.suchelin.DataClass.StoreDataClassMap
import Guide.suchelin.DataClass.StoreDetailDataClass
import Guide.suchelin.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MapStoreAdapter(var storeList: ArrayList<StoreDataClassMap>, val control: MapControl)
    : RecyclerView.Adapter<MapStoreAdapter.MapStoreViewHolder>(){
    class MapStoreViewHolder(itemView: View, val control: MapControl) : RecyclerView.ViewHolder(itemView){
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

            // 미슐랭 스타 아이콘 설정
            storeMichelin.visibility =
                if(storeItem.michelin == null) View.GONE
                else {
                    storeMichelin.setImageResource(storeItem.getMichelinImage()!!)
                    View.VISIBLE
                }

            // 랭크 아이콘 설정
            storeRank.visibility =
                if(storeItem.rank == null) View.GONE
                else {
                    storeRank.setImageResource(storeItem.getRankImage()!!)
                    View.VISIBLE
                }

            // 아이템 선택했을 경우 StoreDetail 페이지로 넘어감
            storeParent.setOnClickListener {
                control.startStoreDetailActivity(storeItem.id, storeItem.name, storeItem.latitude, storeItem.longitude)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapStoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.maplist_ltem,
            parent,
            false
        )
        return MapStoreViewHolder(view, control)
    }

    override fun onBindViewHolder(holder: MapStoreViewHolder, position: Int) {
        holder.bind(storeList[position], position)
    }

    override fun getItemCount(): Int = storeList.size
}