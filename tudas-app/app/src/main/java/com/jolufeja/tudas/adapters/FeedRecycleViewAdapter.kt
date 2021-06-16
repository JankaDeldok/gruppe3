package com.jolufeja.tudas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.R
import com.jolufeja.tudas.data.ListItem
import java.util.*

class FeedRecycleViewAdapter(
    private val context: Context,
    private val mDataList: ArrayList<ListItem>,
    val layoutCard: Int,
    val layoutHeader: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        when (viewType) {
            0 -> return CardViewHolder(
                LayoutInflater.from(context).inflate(layoutCard, parent, false)
            )
        }
        return HeaderViewHolder(
            LayoutInflater.from(context).inflate(layoutHeader, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (mDataList[position].getType()) {
            0 -> {
                (holder as CardViewHolder).bind(position)
            }
            1 -> {
                (holder as HeaderViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mDataList[position].getType()
    }

    private inner class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.feed_text) as TextView
        var cardFrameLayout: FrameLayout = itemView.findViewById(R.id.card_feed) as FrameLayout

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]
            text.text = recyclerViewModel.text
            val androidColors : IntArray = context.resources.getIntArray(R.array.colorarray)
            val randomAndroidColor: Int = androidColors[Random().nextInt(androidColors.size)]
            cardFrameLayout.background.setTint(randomAndroidColor)
        }
    }

    private inner class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.header_text) as TextView
        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]
            text.text = recyclerViewModel.text
        }
    }


}