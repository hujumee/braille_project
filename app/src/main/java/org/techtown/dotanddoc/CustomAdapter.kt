package org.techtown.dotanddoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter : RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<ListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transformed_braille_list_iem,
            parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = listData[position]
        holder.setListData(data)
    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textTitleTextView = itemView.findViewById<TextView>(R.id.item_text)

    fun setText(listData: ListData) {
        textTitleTextView.text = listData.title
    }

    fun setListData(listData: ListData) {
    }
}
