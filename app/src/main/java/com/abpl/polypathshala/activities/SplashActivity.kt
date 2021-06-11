package com.abpl.polypathshala.activities

import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import com.abpl.polypathshala.R
import com.abpl.polypathshala.services.ApiConstants
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.storage.StorePrefrence
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        imageViewSplash.animate().scaleXBy(0.3f).scaleYBy(0.3f).setDuration(1000)
            .setInterpolator(BounceInterpolator()).start()

        startTimer()
    }
    private fun startTimer() {
        Handler().postDelayed({
            if (SignupService.instance!!.getauthRefrence().currentUser != null) {
                var data =StorePrefrence.user
                if (data?.userType == ApiConstants.Refrences.USER_ADMIN) {
                    startNewActivity(AdminActivity::class.java)
                } else {
                    startNewActivity(HomeActivity::class.java)
                }

            } else {
                startNewActivity(LoginActivity::class.java)

            }
        }, 2000)
    }
}