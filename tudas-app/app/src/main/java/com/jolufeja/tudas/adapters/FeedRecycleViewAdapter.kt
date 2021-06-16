package com.jolufeja.tudas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.R
import com.jolufeja.tudas.data.FeedItem
import java.util.ArrayList

class FeedRecycleViewAdapter(
    private val mDataList: ArrayList<FeedItem>,
    val layout: Int,
    private val listener: (FeedItem) -> Unit

) :
    RecyclerView.Adapter<FeedRecycleViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRecycleViewAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = mDataList[position].text
        holder.type.text = mDataList[position].type
        holder.id.text = mDataList[position].id.toString();

        // OnClick Listener on Button
        holder.button.setOnClickListener { listener(mDataList[position]) }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var type: TextView = itemView.findViewById<View>(R.id.challenge_title) as TextView
        internal var text: TextView =
            itemView.findViewById<View>(R.id.challenge_author) as TextView
        internal var id: TextView =
            itemView.findViewById<View>(R.id.challenge_time_left) as TextView
        internal var button: Button = itemView.findViewById<View>(R.id.challenge_button) as Button
    }
}