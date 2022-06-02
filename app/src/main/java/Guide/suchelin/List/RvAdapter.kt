package Guide.suchelin.List

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import Guide.suchelin.R
import Guide.suchelin.DataClass.StoreDataClass

class RvAdapter(val context: Context?, val items: MutableList<StoreDataClass>):RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)

        return ViewHolder(view)
    }
    interface ItemClick{
        fun onClick(view :View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick!!.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(item: StoreDataClass){
            val imageViewShop = itemView.findViewById<ImageView>(R.id.rv_store_imageView)
            val textViewTitle = itemView.findViewById<TextView>(R.id.rv_store_title_textView)
            val textViewDetail = itemView.findViewById<TextView>(R.id.rv_store_detail_textView)
            val storeRank = itemView.findViewById<ImageView>(R.id.rv_rank_imageView)
            val storeMichelin = itemView.findViewById<ImageView>(R.id.rv_michelin_imageView)

            Glide.with(context!!)
                .load(item.imageUrl)
                .centerCrop()
                .into(imageViewShop)

            textViewTitle.text = item.name
            textViewDetail.text = item.detail

            // 미슐랭 스타 아이콘 설정
            storeMichelin.visibility =
                if(item.michelin == null) View.GONE
                else {
                    storeMichelin.setImageResource(item.getMichelinImage()!!)
                    View.VISIBLE
                }

            // 랭크 아이콘 설정
            storeRank.visibility =
                if(item.rank == null) View.GONE
                else {
                    storeRank.setImageResource(item.getRankImage()!!)
                    View.VISIBLE
                }
        }
    }

}