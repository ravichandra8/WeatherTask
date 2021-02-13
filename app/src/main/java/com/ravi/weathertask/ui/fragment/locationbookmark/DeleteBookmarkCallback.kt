package com.ravi.weathertask.ui.fragment.locationbookmark

interface DeleteBookmarkCallback {
    fun bookmarkId(id:Int?)
    fun bookmarkItemClick(lat:String,lng:String)
}