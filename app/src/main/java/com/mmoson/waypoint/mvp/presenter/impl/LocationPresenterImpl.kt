package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import com.mmoson.waypoint.mvp.presenter.LocationPresenter
import com.mmoson.waypoint.mvp.view.LocationView

class LocationPresenterImpl : LocationPresenter() {

    private var view: LocationView? = null
    lateinit var context: Context

    override fun attachView(view: LocationView) {
        this.view = view
    }

    override fun responseHandler(lt: String, lg: String) {
        var validLT = false
        var validLG = false
        if (lt.isEmpty()) {
            view?.responseHandler(0, 1)
        } else if (lt.toFloat() > 90.0f) {
            view?.responseHandler(0, 2)
        } else if (lt.toFloat() < 0.0f) {
            view?.responseHandler(0, 3)
        } else {
            validLT = true
        }
        if (lg.isEmpty()) {
            view?.responseHandler(1, 1)
        } else if (lg.toFloat() > 180.0f) {
            view?.responseHandler(1, 2)
        } else if (lg.toFloat() < 0.0f) {
            view?.responseHandler(1, 3)
        } else {
            validLG = true
        }
        if (validLG && validLT) {
            view?.responseHandler(0, 0)
            view?.responseHandler(1, 0)
            view?.responseHandler(0, 200)
        }
    }
}