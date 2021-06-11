package com.abpl.polypathshala.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.User

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.String
import java.util.*



class UserListAdapter( mContext: Context) :
    RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {
    var mLits: MutableList<User> = ArrayList()
    var mContext: Context
    var VIEW_TYPE_ITEM = 1
    fun clear() {
        mLits.clear()
        notifyDataSetChanged()
    }

    fun addItem(model: User) {
        mLits.add(model)
        notifyDataSetChanged()
    }

    fun removeItem(index: Int) {
        mLits.removeAt(index)
        notifyItemRemoved(index)
    }

    fun addAllItem(models: List<User>?) {
        mLits.addAll(models!!)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user_detail, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        viewHolder.textViewName.text = "${mLits[i].fisrtName} ${mLits[i].lastName}  "
        viewHolder.textViewEmail.text = mLits[i].email;
        viewHolder.textViewUserType.text = mLits[i].userType
        viewHolder.textViewUserTrade.text = mLits[i].trade
        viewHolder.textViewUserSem.text = mLits[i].semester

    }

    override fun getItemCount(): Int {
        return mLits.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewName: TextView
        var textViewEmail: TextView
        var textViewUserType: TextView
        var textViewUserTrade: TextView
        var textViewUserSem: TextView

        init {
            textViewName = itemView.findViewById(R.id.textViewName)
            textViewEmail = itemView.findViewById(R.id.textViewEmail)
            textViewUserType = itemView.findViewById(R.id.textViewUserType)
            textViewUserTrade = itemView.findViewById(R.id.textViewUserTrade)
            textViewUserSem = itemView.findViewById(R.id.textViewUserSem)

        }
    }

    init {
        this.mContext = mContext
    }
}