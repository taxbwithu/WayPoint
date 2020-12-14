package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import android.util.Log
import com.mmoson.waypoint.mvp.presenter.CompassPresenter
import com.mmoson.waypoint.mvp.view.CompassView
import com.mmoson.waypoint.utils.DistanceUtils

class CompassPresenterImpl : CompassPresenter() {

    private var view: CompassView? = null
    lateinit var context: Context
    var lt : Double = 0.0
    var lg : Double = 0.0

    override fun attachView(view: CompassView) {
        this.view = view
    }

    override fun setDestLocation(destLat: Double, destLon: Double) {
        this.lt = destLat
        this.lg = destLon
    }

    override fun calculateDistance(currentLat : Double?, currentLon : Double?) {
        var distance = 0.0
        if(currentLat != null && currentLon != null){
            distance = DistanceUtils().distance(lt, lg, currentLat, currentLon, 'K')
        }
        if (distance < 1.0){
            view?.showDistance(String.format("%.0f", distance*1000.0), "m.")
        }
        else{
            view?.showDistance(String.format("%.4f", distance), "km.")
        }
    }
}