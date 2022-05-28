package Guide.suchelin.Vote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Guide.suchelin.R
import Guide.suchelin.DataClass.StoreDataClass
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class VoteRvAdapter(val context: Context?, val items: MutableList<StoreDataClass>):RecyclerView.Adapter<VoteRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vote_item,parent,false)

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
            val imageViewShop = itemView.findViewById<ImageView>(R.id.imgVote)
            val textViewTitle = itemView.findViewById<TextView>(R.id.textVote)
            Glide.with(context!!)
                .load(item.imageUrl)
                .centerCrop()
                .into(imageViewShop)

            textViewTitle.text = item.name
        }
    }

}