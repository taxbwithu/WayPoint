package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import com.mmoson.waypoint.mvp.presenter.CompassPresenter
import com.mmoson.waypoint.mvp.view.CompassView

class CompassPresenterImpl : CompassPresenter() {

    private var view: CompassView? = null
    lateinit var context: Context

    override fun attachView(view: CompassView){
        this.view = view
    }
}