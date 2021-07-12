package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.computations.either
import arrow.core.identity
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.tudas.adapters.RecycleViewAdapter
import com.jolufeja.tudas.data.FriendsItem
import com.jolufeja.tudas.data.ListItem
import com.jolufeja.tudas.service.user.FriendEntry
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.koin.android.ext.android.get

private val DefaultFriendsList = listOf(
    FriendsItem().apply {
        id = 0
        text = "Such empty :("
    }
)

class FriendsSettingsFragment(
    private val userService: UserService
) : Fragment(R.layout.fragment_friends_settings) {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var listOfFriends: ArrayList<FriendsItem> = ArrayList()
    private var finalList: MutableList<ListItem> = mutableListOf()


    private suspend fun buildFriendsList() = flow {
        either<CommonErrors, Unit> {
            emit(emptyList())

            val friends = userService
                .getFriendsOfCurrentUser()
                .bind()
                .toFriendsListItems()

            val friendsNonEmpty = when (friends.isEmpty()) {
                true -> DefaultFriendsList
                false -> friends
            }


            emit(friendsNonEmpty)
        }.fold(
            ifLeft = {
                Log.d("FriendsSettingsFragment", it.toString())
                emit(DefaultFriendsList)
            },
            ifRight = ::identity
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        mRecyclerView = view.findViewById(R.id.lists_recycler_view)
        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager


        mAdapter =
            context?.let {
                RecycleViewAdapter(
                    it,
                    finalList,
                    0,
                    0,
                    0,
                    0,
                    0,
                    R.layout.card_friends_settings,
                    0
                ) {
                    null
                }
            }
        mRecyclerView!!.adapter = mAdapter

        lifecycleScope.launchWhenCreated {
            buildFriendsList().collect { friends ->
                finalList = friends.toMutableList()
                (mAdapter as? RecycleViewAdapter)?.refreshData(friends)
                mAdapter?.notifyDataSetChanged()
            }
        }


        var addNewFriendsButton: Button = view.findViewById<View>(R.id.add_friends_button) as Button

        addNewFriendsButton.setOnClickListener {
            val addFriendFragment = AddFriendFragment(get())
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                ((view as ViewGroup).parent as View).id,
                addFriendFragment
            )
            transaction.addToBackStack("friends_list")
            transaction.commit()
        }

        var cancelButton: TextView = view.findViewById<View>(R.id.back_button) as TextView

        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack();
        }
    }
}


fun List<FriendEntry>.toFriendsListItems(): List<ListItem> = mapIndexed { i, friendEntry ->
    FriendsItem().apply {
        id = i
        text = friendEntry.name
    }
}
