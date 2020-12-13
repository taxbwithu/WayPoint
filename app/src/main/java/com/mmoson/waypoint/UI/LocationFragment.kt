package com.mmoson.waypoint.UI

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mmoson.waypoint.R
import com.mmoson.waypoint.mvp.presenter.LocationPresenter
import com.mmoson.waypoint.mvp.presenter.impl.LocationPresenterImpl
import com.mmoson.waypoint.mvp.view.LocationView

class LocationFragment : Fragment(), LocationView {

    internal lateinit var context: Context
    private var presenter: LocationPresenter = LocationPresenterImpl()
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_location, container, false)
        super.onCreate(savedInstanceState)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = view.context

        val btn = rootView.findViewById<Button>(R.id.btn_compass)
        btn.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.nav_compass)
            val lt = rootView.findViewById<EditText>(R.id.input_latitude).text.toString()
            val lg = rootView.findViewById<EditText>(R.id.input_longitude).text.toString()
            startCompassFragment(lt, lg)
        }


    }

    override fun startCompassFragment(lt : String, lg : String){
        val compassAction = LocationFragmentDirections.navCompass(lt, lg)
        Navigation.findNavController(rootView).navigate(compassAction)
    }
}