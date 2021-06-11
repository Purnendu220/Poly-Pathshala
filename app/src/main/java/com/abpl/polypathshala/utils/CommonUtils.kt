package com.abpl.polypathshala.utils

import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.models.User


object CommonUtils {
    fun getUserFromHaspMap(map: MutableMap<String, Any>): User? {
        val id = if (map["id"] != null) map["id"].toString() else ""
        val fisrtName = if (map["fisrtName"] != null) map["fisrtName"].toString() else ""
        val lastName = if (map["lastName"] != null) map["lastName"].toString() else ""
        val email = if (map["email"] != null) map["email"].toString() else ""
        val mobile = if (map["mobile"] != null) map["mobile"].toString() else ""
        val password = if (map["password"] != null) map["password"].toString() else ""
        val semester = if (map["semester"] != null) map["semester"].toString() else ""
        val trade = if (map["trade"] != null) map["trade"].toString() else ""
        val userType = if (map["userType"] != null) map["userType"].toString() else ""


        return User(id, fisrtName, lastName, email, mobile, password, semester, trade, userType)

    }
    fun getSubjectFromHaspMap(map: MutableMap<String, Any>): SubjectModel? {


        val trade = if (map["trade"] != null) map["trade"].toString() else ""
        val semester = if (map["semester"] != null) map["semester"].toString() else ""
        val name = if (map["name"] != null) map["name"].toString() else ""
        val engFile = if (map["engFile"] != null) map["engFile"].toString() else ""
        val hinFile = if (map["hinFile"] != null) map["hinFile"].toString() else ""



        return SubjectModel(
            trade, semester, name, engFile, hinFile
        )


    }
    fun getInitials(name: String): String {
        if (name.isEmpty()) return "PP"

        // Since touuper() returns int,
        // we do typecasting
        var initials =Character.toUpperCase(name[0]).toString()
        print(
            Character.toUpperCase(
                name[0]
            )
        )

        // Traverse rest of the string and
        // print the characters after spaces.
        for (i in 1 until name.length - 1) {
            if (name[i] == ' ') {
                initials += Character.toUpperCase(name[i + 1]
                )

            }
        }
   return initials
    }

}


