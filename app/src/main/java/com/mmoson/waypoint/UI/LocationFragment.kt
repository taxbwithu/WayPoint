package com.mmoson.waypoint.UI

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.mmoson.waypoint.R
import com.mmoson.waypoint.mvp.presenter.LocationPresenter
import com.mmoson.waypoint.mvp.presenter.impl.LocationPresenterImpl
import com.mmoson.waypoint.mvp.view.LocationView


class LocationFragment : Fragment(), LocationView {

    internal lateinit var context: Context
    private var presenter: LocationPresenter = LocationPresenterImpl()
    lateinit var rootView: View
    lateinit var lt: String
    lateinit var lg: String

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
        presenter.attachView(this)
        val mainActivity = activity as AppCompatActivity
        val btn = rootView.findViewById<Button>(R.id.btn_compass)
        btn.setOnClickListener {
            val ltEdit = rootView.findViewById<TextInputEditText>(R.id.input_latitude)
            val lgEdit = rootView.findViewById<TextInputEditText>(R.id.input_longitude)
            lt = ltEdit.text.toString()
            lg = lgEdit.text.toString()
            presenter.responseHandler(lt, lg)
        }
        checkPermission(mainActivity)
    }

    override fun startCompassFragment(lt: String, lg: String) {
        closeKeyboard()
        val compassAction = LocationFragmentDirections.navCompass(lt, lg)
        Navigation.findNavController(rootView).navigate(compassAction)
    }

    fun closeKeyboard() {
        val mgr =
            rootView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(rootView.windowToken, 0)
    }

    override fun responseHandler(editTextCode: Int, responseCode: Int) {
        Log.d("TAG", "response: " + editTextCode)
        var textInput: TextInputEditText
        if (editTextCode == 0) {
            textInput = rootView.findViewById(R.id.input_latitude)
        } else {
            textInput = rootView.findViewById(R.id.input_longitude)
        }
        when (responseCode) {
            0 -> textInput.error = null
            1 -> textInput.setError("This field is empty")
            2 -> textInput.setError("Value too big")
            3 -> textInput.setError("Value too small")
            200 -> startCompassFragment(lt, lg)
        }
    }

    fun checkPermission(mainActivity: AppCompatActivity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val btn = rootView.findViewById<Button>(R.id.btn_compass)
            btn.isClickable = false
            btn.backgroundTintList = context.resources.getColorStateList(R.color.grey)
            requestPermissions(
                mainActivity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        } else {
            val btn = rootView.findViewById<Button>(R.id.btn_compass)
            btn.isClickable = true
            btn.backgroundTintList = context.resources.getColorStateList(R.color.purple_500)
        }
    }
}