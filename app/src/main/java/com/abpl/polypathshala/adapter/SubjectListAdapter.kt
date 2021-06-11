package com.abpl.polypathshala.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.SubjectModel

import java.util.*




class SubjectListAdapter( mContext: Context) :
    RecyclerView.Adapter<SubjectListAdapter.MyViewHolder>() {
    var mLits: MutableList<SubjectModel> = ArrayList()
    var mContext: Context
    var VIEW_TYPE_ITEM = 1
    fun clear() {
        mLits.clear()
        notifyDataSetChanged()
    }

    fun addItem(model: SubjectModel) {
        mLits.add(model)
        notifyDataSetChanged()
    }

    fun removeItem(index: Int) {
        mLits.removeAt(index)
        notifyItemRemoved(index)
    }

    fun addAllItem(models: List<SubjectModel>?) {
        mLits.addAll(models!!)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_subject_layout, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        viewHolder.txtSubjectName.text = mLits[i].name
        viewHolder.txtHindiFile.text = "View";
        viewHolder.txtEngFile.text = "View"

    }

    override fun getItemCount(): Int {
        return mLits.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtSubjectName: TextView
        var txtHindiFile: TextView
        var txtEngFile: TextView

        init {
            txtSubjectName = itemView.findViewById(R.id.txtSubjectName)
            txtHindiFile = itemView.findViewById(R.id.txtHindiFile)
            txtEngFile = itemView.findViewById(R.id.txtEngFile)


        }
    }

    init {
        this.mContext = mContext
    }
}