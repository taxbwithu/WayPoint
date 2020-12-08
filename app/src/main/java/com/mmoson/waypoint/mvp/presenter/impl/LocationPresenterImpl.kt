package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import com.mmoson.waypoint.mvp.presenter.LocationPresenter
import com.mmoson.waypoint.mvp.view.LocationView

class LocationPresenterImpl : LocationPresenter() {

    private var view: LocationView? = null
    lateinit var context: Context

    override fun attachView(view: LocationView){
        this.view = view
    }
}