package com.mmoson.waypoint.UI


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mmoson.waypoint.R
import com.mmoson.waypoint.mvp.presenter.LocationPresenter
import com.mmoson.waypoint.mvp.presenter.impl.LocationPresenterImpl
import com.mmoson.waypoint.mvp.view.CompassView


class CompassFragment : Fragment(), CompassView {

    internal lateinit var context: Context
    private var presenter: LocationPresenter = LocationPresenterImpl()
    lateinit var rootView: View
    var lt : Float = 0.0f
    var lg : Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_compass, container, false)
        super.onCreate(savedInstanceState)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = CompassFragmentArgs.fromBundle(it)
            lt = safeArgs.latitude.toFloat()
            lg = safeArgs.longitude.toFloat()
        }
        val ed = rootView.findViewById<TextView>(R.id.lat)
        ed.setText(lt.toString())
        val edd = rootView.findViewById<TextView>(R.id.lon)
        edd.setText(lg.toString())
    }
}