package com.mmoson.waypoint.UI


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    var lt: Float = 0.0f
    var lg: Float = 0.0f

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
        arguments?.let {
            val safeArgs = CompassFragmentArgs.fromBundle(it)
            lt = safeArgs.latitude.toFloat()
            lg = safeArgs.longitude.toFloat()
        }
        val ed = rootView.findViewById<TextView>(R.id.lat)
        ed.setText(lt.toString())
        val edd = rootView.findViewById<TextView>(R.id.lon)
        edd.setText(lg.toString())
        getStartLocation()
        registerReceiver()
    }

    fun getStartLocation(){
        val inst = GPSUtils.getInstance()
        inst.initPermissions(activity)
        inst.findDeviceLocation(activity)
        lg = inst.longitude.toFloat()
        lt = inst.latitude.toFloat()
    }

    fun registerReceiver(){
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val ed = rootView.findViewById<TextView>(R.id.lat)
                ed.setText(intent?.getDoubleExtra("LON", 0.0).toString())
                val edd = rootView.findViewById<TextView>(R.id.lon)
                edd.setText(intent?.getDoubleExtra("LAT", 0.0).toString())
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.mmoson.action.LOCATE")
        activity?.registerReceiver(receiver,intentFilter)
    }

    override fun showCurrentLocation(lat : String, lon : String){
        val ed = rootView.findViewById<TextView>(R.id.lat)
        ed.setText(lat)
        val edd = rootView.findViewById<TextView>(R.id.lon)
        edd.setText(lon)
    }
}