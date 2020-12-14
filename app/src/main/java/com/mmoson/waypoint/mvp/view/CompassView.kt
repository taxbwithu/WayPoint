package com.mmoson.waypoint.mvp.view

interface CompassView {
    fun showDistance(distance : String, dstFormat : String)
    fun spinCompass(rotation : Float)
    fun spinDestination(rotation : Float)
}