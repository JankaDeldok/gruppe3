package com.jolufeja.tudas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


// Adapter to create a challenge card
class ChallengesRecycleViewAdapter(
    private val mDataList: ArrayList<Challenges>,
    val layout: Int,
    private val listener: (Challenges) -> Unit

) :
    RecyclerView.Adapter<ChallengesRecycleViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = mDataList[position].title
        holder.author.text = mDataList[position].author
        holder.timeLeft.text = mDataList[position].timeLeft.toString();
        holder.reward.text = mDataList[position].reward
        holder.points.text = mDataList[position].points.toString();
        holder.description.text = mDataList[position].description

        // OnClick Listener on Button
        holder.button.setOnClickListener { listener(mDataList[position]) }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView = itemView.findViewById<View>(R.id.challenge_title) as TextView
        internal var author: TextView =
            itemView.findViewById<View>(R.id.challenge_author) as TextView
        internal var timeLeft: TextView =
            itemView.findViewById<View>(R.id.challenge_time_left) as TextView
        internal var description: TextView =
            itemView.findViewById<View>(R.id.challenge_description) as TextView
        internal var reward: TextView =
            itemView.findViewById<View>(R.id.challenge_reward) as TextView
        internal var points: TextView =
            itemView.findViewById<View>(R.id.challenge_points) as TextView
        internal var button: Button = itemView.findViewById<View>(R.id.challenge_button) as Button
    }

}