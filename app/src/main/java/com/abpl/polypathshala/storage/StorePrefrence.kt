package com.abpl.polypathshala.storage

import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.ApiConstants
import com.google.gson.Gson
import com.preference.PowerPreference
import com.preference.Preference
import java.util.*


object StorePrefrence  {
    private val prefrence: Preference = PowerPreference.getDefaultFile()
    private val gson: Gson = Gson()
    var user: User?
        get() = prefrence.getObject(ApiConstants.Prefrences.USER_KEY, User::class.java, null)
        set(user) {
            prefrence.putObject(ApiConstants.Prefrences.USER_KEY, user)
        }

    fun clearPrefrence() {
        prefrence.clear()
    }

    var token: String?
        get() = prefrence.getString(ApiConstants.Prefrences.TOKEN, "")
        set(s) {
            prefrence.putString(ApiConstants.Prefrences.TOKEN, s)
        }


}