package com.mmoson.waypoint.mvp.presenter

import com.mmoson.waypoint.mvp.view.CompassView

abstract class CompassPresenter {
    abstract fun attachView(view: CompassView)
    abstract fun setDestLocation(destLat : Double, destLon : Double)
    abstract fun calculateDistance(currentLat : Double?, currentLon : Double?)
}