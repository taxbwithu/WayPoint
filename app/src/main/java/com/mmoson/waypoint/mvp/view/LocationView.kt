package com.mmoson.waypoint.mvp.view

interface LocationView {
    fun startCompassFragment(lt: String, lg: String)
    fun responseHandler(editTextCode: Int, responseCode: Int)
}