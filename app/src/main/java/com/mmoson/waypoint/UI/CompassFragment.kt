package com.mmoson.waypoint.UI


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mmoson.waypoint.R
import com.mmoson.waypoint.mvp.presenter.CompassPresenter
import com.mmoson.waypoint.mvp.presenter.impl.CompassPresenterImpl
import com.mmoson.waypoint.mvp.view.CompassView
import com.mmoson.waypoint.utils.GPSUtils


class CompassFragment : Fragment(), CompassView{

    internal lateinit var context: Context
    private var presenter: CompassPresenter = CompassPresenterImpl()
    lateinit var rootView: View
    var receiver : BroadcastReceiver? = null
    var lt: Double = 0.0
    var lg: Double = 0.0

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
        context = view.context
        presenter.attachView(this)
        presenter.setCompass(context)
        arguments?.let {
            val safeArgs = CompassFragmentArgs.fromBundle(it)
            lt = safeArgs.latitude.toDouble()
            lg = safeArgs.longitude.toDouble()
            presenter.setDestLocation(lt, lg)
        }
        getStartLocation()
        registerReceiver()
    }

    fun getStartLocation(){
        val inst = GPSUtils.getInstance()
        inst.initPermissions(activity)
        inst.findDeviceLocation(activity)
        lg = inst.longitude.toDouble()
        lt = inst.latitude.toDouble()
        presenter.calculateDistance(lt, lg)
    }

    override fun onDetach() {
        super.onDetach()
        activity?.unregisterReceiver(receiver)
    }

    fun registerReceiver(){
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                presenter.calculateDistance(intent?.getDoubleExtra("LAT",0.0), intent?.getDoubleExtra("LON", 0.0))
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.mmoson.action.LOCATE")
        activity?.registerReceiver(receiver,intentFilter)
    }

    override fun showDistance(distance : String, dstFormat : String){
        val distanceEdit = rootView.findViewById<TextView>(R.id.distance)
        val dist = resources.getString(R.string.distance_from_destination) + " " + distance + " " + dstFormat
        distanceEdit.setText(dist)
    }

    override fun spinCompass(rotation: Float) {
        val compass = rootView.findViewById<ImageView>(R.id.compass)
        compass.rotation = rotation
    }

    override fun spinDestination(rotation: Float) {
        val destination = rootView.findViewById<ImageView>(R.id.destination)
        destination.rotation = rotation
    }

    override fun arrivedAtDestination() {
        Toast.makeText(context,"You arrived at destined location",
            Toast.LENGTH_SHORT).show()
    }
}