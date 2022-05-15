package mingyuk99.suchelin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class RvAdapter(val context: Context?, val items: MutableList<dataSet>):RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(item: dataSet){
            val imageViewShop = itemView.findViewById<ImageView>(R.id.imageViewShop)
            val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)

            Glide.with(context!!)
                .load(item.imageUrl)
                .centerCrop()
                .into(imageViewShop)

            textViewTitle.text = item.name

        }
    }

}