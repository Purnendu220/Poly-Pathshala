package com.abpl.polypathshala.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_sign_up.*

open class BaseActivity:AppCompatActivity() {
    lateinit var mContext: Context;
    var pDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mContext = this;
    }
    open fun hideLoading() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.cancel();
//        }
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog!!.dismiss()
        }
    }

    open fun showLoading(vararg title: String?) {
        var titleText: String? = "Loading ..."
        if (title != null && title.size > 0) {
            titleText = title[0]
        }
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#F6020D")
        pDialog!!.titleText = titleText
        pDialog!!.setCancelable(false)
        pDialog!!.show()
    }


    fun startNewActivity(cls: Class<*>?) {
        val start = Intent(this@BaseActivity, cls)
        startActivity(start)
        finish()
    }
    fun startNewActivityWithoutFinish(cls: Class<*>?) {
        val start = Intent(this@BaseActivity, cls)
        startActivity(start)
    }
}