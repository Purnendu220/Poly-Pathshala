package com.abpl.polypathshala.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.adapter.SubjectListAdapter
import com.abpl.polypathshala.adapter.UserListAdapter
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.services.SubjectService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.CommonUtils
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_subject_list.*
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.activity_user_list.recyclerViewUserList
import java.lang.Exception

class SubjectListActivity : BaseActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: SubjectListAdapter? = null
    var mList: MutableList<SubjectModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_list)
        setUpRecyclerView()
        getSubjectList();
    }

    fun getSubjectList(){
        showLoading()
      val id =  SignupService.instance?.userAuthRefrence?.currentUser!!.uid
        SignupService.instance?.databasepRefrence?.document(id)?.addSnapshotListener { documentSnapshot, e ->
            try {
                val model: User? = documentSnapshot!!.data?.let { CommonUtils.getUserFromHaspMap(it) }
                StorePrefrence.user=model
                SubjectService.getDataBaseReference("${model?.semester}-${model?.trade}" )?.addSnapshotListener { queryDocumentSnapshots, e ->
                    hideLoading()
                    mList = ArrayList()
                    for (snapShot: DocumentSnapshot in queryDocumentSnapshots!!.documents) {
                        var user:SubjectModel? = snapShot?.data?.let { CommonUtils.getSubjectFromHaspMap(it) }
                        user?.let { mList.add(it) }
                    }
                    if (adapter != null) {
                        adapter!!.clear()
                        adapter!!.addAllItem(mList)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }catch (e:Exception){
                hideLoading()
            }


        }

    }
    fun setUpRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        subjectList.layoutManager = layoutManager

        adapter = SubjectListAdapter(this)
        subjectList.adapter = adapter

    }
}