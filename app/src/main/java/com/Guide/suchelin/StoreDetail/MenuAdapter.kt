package com.Guide.suchelin.StoreDetail

import com.Guide.suchelin.DataClass.StoreMenuClass
import com.Guide.suchelin.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(val menuList: ArrayList<StoreMenuClass>)
    : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.menu_item_name_textView)
        val price = itemView.findViewById<TextView>(R.id.menu_item_price_textView)

        fun bind(menu: StoreMenuClass){
            name.text = menu.menuName
            price.text = menu.menuPrice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent,false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size

}