package com.abpl.polypathshala.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class SignupService private constructor() {
    val databasepRefrence: CollectionReference
    var userAuthRefrence: FirebaseAuth
    fun getauthRefrence(): FirebaseAuth {
        return userAuthRefrence
    }

    companion object {
        var instance: SignupService? = null
            get() {
                if (field == null) {
                    field = SignupService()
                }
                return field
            }
            private set
    }


    init {
        userAuthRefrence = FirebaseAuth.getInstance()
        databasepRefrence =
            FirebaseFirestore.getInstance().collection(ApiConstants.Refrences.USER_REFRENCE)
    }
}