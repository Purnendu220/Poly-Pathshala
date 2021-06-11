package com.abpl.polypathshala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.adapter.UserListAdapter
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.utils.CommonUtils
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : BaseActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: UserListAdapter? = null
    var mList: MutableList<User> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        setUpRecyclerView()
        getUserList()

    }

    fun getUserList(){
        showLoading()
        SignupService.instance?.databasepRefrence?.addSnapshotListener { queryDocumentSnapshots, e ->
            hideLoading()
            mList = ArrayList()
            for (snapShot: DocumentSnapshot in queryDocumentSnapshots!!.documents) {
                var user:User? = snapShot?.data?.let { CommonUtils.getUserFromHaspMap(it) }
                user?.let { mList.add(it) }
            }
            if (adapter != null) {
                adapter!!.clear()
                adapter!!.addAllItem(mList)
                adapter!!.notifyDataSetChanged()
            }
        }
    }
    fun setUpRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        recyclerViewUserList.layoutManager = layoutManager

        adapter = UserListAdapter(this)
        recyclerViewUserList.adapter = adapter

    }
}