package com.jolufeja.tudas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jolufeja.tudas.R
import com.jolufeja.tudas.data.ListItem
import java.util.*


// Adapter to create a challenge card
class RecycleViewAdapter(
    private val context: Context,
    private val mDataList: ArrayList<ListItem>,
    private val layoutCard: Int,
    private val layoutHeader: Int,
    private val layoutFeedCard: Int,
    private val layoutRankingCard: Int,
    private val layoutGroupsRankingCard: Int,
    private val layoutFriendsCard: Int,
    private val layoutCreateGroupCard: Int,
    private val listener: (ListItem) -> Unit
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
            2 -> return FeedViewHolder(
                LayoutInflater.from(context).inflate(layoutFeedCard, parent, false)
            )
            3 -> return RankingViewHolder(
                LayoutInflater.from(context).inflate(layoutRankingCard, parent, false)
            )
            4 -> return GroupsViewHolder (
                LayoutInflater.from(context).inflate(layoutGroupsRankingCard, parent, false)
            )
            5 -> return FriendsViewHolder (
                LayoutInflater.from(context).inflate(layoutFriendsCard, parent, false)
            )
            6 -> return CreateGroupViewHolder(
                LayoutInflater.from(context).inflate(layoutCreateGroupCard, parent, false)
            )
        }
        return HeaderViewHolder(
            LayoutInflater.from(context).inflate(layoutHeader, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (mDataList[position].getType()) {
            0 -> {
                (holder as RecycleViewAdapter.CardViewHolder).bind(position)
            }
            1 -> {
                (holder as RecycleViewAdapter.HeaderViewHolder).bind(position)
            }
            2 -> {
                (holder as RecycleViewAdapter.FeedViewHolder).bind(position)
            }
            3 -> {
                (holder as RecycleViewAdapter.RankingViewHolder).bind(position)
            }
            4 -> {
                (holder as RecycleViewAdapter.GroupsViewHolder).bind(position)
            }
            5 -> {
                (holder as RecycleViewAdapter.FriendsViewHolder).bind(position)
            }
            6 -> {
                (holder as RecycleViewAdapter.CreateGroupViewHolder).bind(position)
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
        var title: TextView = itemView.findViewById<View>(R.id.challenge_title) as TextView
        var author: TextView =
            itemView.findViewById<View>(R.id.challenge_author) as TextView
        var timeLeft: TextView =
            itemView.findViewById<View>(R.id.challenge_time_left) as TextView
        var description: TextView =
            itemView.findViewById<View>(R.id.challenge_description) as TextView
        var reward: TextView =
            itemView.findViewById<View>(R.id.challenge_reward) as TextView
        var points: TextView =
            itemView.findViewById<View>(R.id.challenge_points) as TextView
        var button: Button = itemView.findViewById<View>(R.id.challenge_button) as Button

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]

            title.text = recyclerViewModel.title
            author.text = recyclerViewModel.author
            timeLeft.text = recyclerViewModel.timeLeft.toString();
            reward.text = recyclerViewModel.reward
            points.text = recyclerViewModel.points.toString();
            description.text = recyclerViewModel.description

            // OnClick Listener on Button
            button.setOnClickListener { listener(recyclerViewModel) }
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

    private inner class FeedViewHolder(itemView: View) :
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

    private inner class RankingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.ranking_name) as TextView
        var ranking: TextView = itemView.findViewById(R.id.ranking_ranking) as TextView
        var points: TextView = itemView.findViewById(R.id.ranking_points) as TextView
        var cardFrameLayout: FrameLayout = itemView.findViewById(R.id.card_feed) as FrameLayout

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]
            if(recyclerViewModel.rankingType == 1){
                cardFrameLayout.background.setTint(ContextCompat.getColor(context,R.color.orange));
            } else {
                cardFrameLayout.background.setTint(ContextCompat.getColor(context,R.color.primary));
            }
            name.text = recyclerViewModel.name
            ranking.text = recyclerViewModel.ranking.toString()
            points.text = recyclerViewModel.points.toString()
            //val androidColors : IntArray = context.resources.getIntArray(R.array.colorarray)
          //  val randomAndroidColor: Int = androidColors[Random().nextInt(androidColors.size)]

           // cardFrameLayout.background.setTint(randomAndroidColor)
        }
    }

    private inner class GroupsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var groupName: TextView = itemView.findViewById<View>(R.id.group_name) as TextView
        var groupSize: TextView =
            itemView.findViewById<View>(R.id.group_size) as TextView
        var openGroupButton: Button = itemView.findViewById<View>(R.id.open_group_button) as Button

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]

            groupName.text = recyclerViewModel.name
            groupSize.text = recyclerViewModel.size.toString();

            // OnClick Listener on Button
            openGroupButton.setOnClickListener { listener(recyclerViewModel) }

        }
    }

    private inner class FriendsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.friend_username) as TextView
        var deleteButton: Button = itemView.findViewById<View>(R.id.delete_button) as Button

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]

            username.text = recyclerViewModel.text

            // OnClick Listener on Button
            deleteButton.setOnClickListener { listener(recyclerViewModel) }

        }
    }

    private inner class CreateGroupViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.friend_username) as TextView
        var button: Button = itemView.findViewById<View>(R.id.add_friend_to_group) as Button

        fun bind(position: Int) {
            val recyclerViewModel = mDataList[position]
            username.text = recyclerViewModel.text
            // OnClick Listener on Button
            button.setOnClickListener { listener(recyclerViewModel) }
        }
    }
}