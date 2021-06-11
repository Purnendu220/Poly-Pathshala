package com.abpl.polypathshala.services

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


object SubjectService  {
    lateinit var databaseReference: CollectionReference


    fun getDataBaseReference(reference:String):CollectionReference{
        databaseReference = FirebaseFirestore.getInstance().collection(reference)
        return databaseReference
    }
}