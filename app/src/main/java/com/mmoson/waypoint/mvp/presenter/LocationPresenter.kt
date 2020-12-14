package com.mmoson.waypoint.mvp.presenter

import com.mmoson.waypoint.mvp.view.LocationView

abstract class LocationPresenter {
    abstract fun attachView(view: LocationView)
    abstract fun responseHandler(lt : String, lg : String)
}