package com.abpl.polypathshala.activities

import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.ApiConstants
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.services.SubjectService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.CommonUtils
import com.abpl.polypathshala.utils.RemoteConfigUtils
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        RemoteConfigUtils.fetchRemoteConfig(this)

        imageViewSplash.animate().scaleXBy(0.3f).scaleYBy(0.3f).setDuration(1000)
            .setInterpolator(BounceInterpolator()).start()

        startTimer()
    }
    private fun startTimer() {
        Handler().postDelayed({
            if (SignupService.instance!!.getauthRefrence().currentUser != null) {
             getUserDetail()
            } else {
                startNewActivity(LoginActivity::class.java)

            }
        }, 2000)
    }
    fun getUserDetail(){
        showLoading()
        val id =  SignupService.instance?.userAuthRefrence?.currentUser!!.uid
        SignupService.instance?.databasepRefrence?.document(id)?.addSnapshotListener { documentSnapshot, e ->
            try {
                val data: User? = documentSnapshot!!.data?.let { CommonUtils.getUserFromHaspMap(it) }
                StorePrefrence.user=data
                if (data?.userType == ApiConstants.Refrences.USER_ADMIN) {
                    startNewActivity(AdminActivity::class.java)
                } else {
                    startNewActivity(HomeActivity::class.java)
                }

            }catch (e: Exception){
                hideLoading()
            }


        }

    }

}