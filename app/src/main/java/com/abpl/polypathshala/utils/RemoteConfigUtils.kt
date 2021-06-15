package com.abpl.polypathshala.utils

import android.app.Activity
import com.abpl.polypathshala.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfigUtils {
    const val SEMESTER = "semesters"
    const val TRADES = "trades"
    const val ABOUT_US = "about_us"


    var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    var configSettings: FirebaseRemoteConfigSettings? = null
    init {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings!!)
         mFirebaseRemoteConfig!!.setDefaultsAsync(
             R.xml.remote_configure
         )
    }
    fun fetchRemoteConfig(activity: Activity){
        mFirebaseRemoteConfig!!.fetch(0)
            .addOnCompleteListener(
                activity!!
            ) { task -> // If is successful, activated fetched
                if (task.isSuccessful) {
                    mFirebaseRemoteConfig!!.fetchAndActivate()
                }
            }
    }
    fun getRemoteConfigValue(key: String?): String? {
        return mFirebaseRemoteConfig!!.getString(key!!)
    }
}