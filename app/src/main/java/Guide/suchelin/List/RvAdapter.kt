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
            holder?.itemView.setOnClickListener{v->
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
            val imageViewShop = itemView.findViewById<ImageView>(R.id.imageViewShop)
            val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDetail = itemView.findViewById<TextView>(R.id.textViewDetail)
            Glide.with(context!!)
                .load(item.imageUrl)
                .centerCrop()
                .into(imageViewShop)

            textViewTitle.text = item.name
            textViewDetail.text = item.detail
        }
    }

}