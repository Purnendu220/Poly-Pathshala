package com.abpl.polypathshala.utils

import com.abpl.polypathshala.models.SubjectModel

interface OnItemClick {
    fun onItemClick(url: SubjectModel, position:Int)
}