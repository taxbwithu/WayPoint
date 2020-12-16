package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.rotationMatrix
import com.mmoson.waypoint.mvp.presenter.CompassPresenter
import com.mmoson.waypoint.mvp.view.CompassView
import com.mmoson.waypoint.utils.DistanceUtils

class CompassPresenterImpl (val distanceUtils: DistanceUtils = DistanceUtils()) : CompassPresenter(), SensorEventListener {

    private var view: CompassView? = null
    lateinit var context: Context
    var userArrived = false
    var sensorManager : SensorManager? = null
    var rotationMatrix = FloatArray(9)
    var azimuth : Int = 0
    var orientation = FloatArray(3)
    var lastGravity = FloatArray(3)
    var lastGravitySet = false
    var lastGeomagnetic = FloatArray(3)
    var lastGeomagneticSet = false
    var gravity : Sensor ? = null
    var geomagnetic : Sensor ? = null
    var rotationVector : Sensor ? = null
    var haveSensorGravity = false
    var haveSensorGeomagnetic = false
    var haveSensorRotationVector = false
    var lt : Double = 0.0
    var lg : Double = 0.0
    var currentLT : Double? = 0.0
    var currentLG : Double? = 0.0

    override fun attachView(view: CompassView) {
        this.view = view
    }

    override fun setDestLocation(destLat: Double, destLon: Double) {
        this.lt = destLat
        this.lg = destLon
    }

    override fun setCompass(context : Context){
        this.context = context
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        startCompass()
    }

    override fun calculateDistance(currentLat : Double?, currentLon : Double?) {
        var distance = 0.0
        currentLT = currentLat
        currentLG = currentLon
        if(currentLat != null && currentLon != null){
            distance = distanceUtils.distance(lt, lg, currentLat, currentLon, 'K')
        }
        if (distance < 1.0){
            view?.showDistance(String.format("%.0f", distance*1000.0), "m.")
            if(!userArrived && (distance*1000)<2.0){
                userArrived = true
            }
        }
        else{
            view?.showDistance(String.format("%.4f", distance), "km.")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            azimuth = (Math.toDegrees(SensorManager.getOrientation(rotationMatrix,orientation)[0].toDouble())+360).toInt()%360
        }
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY){
            System.arraycopy(event.values,0,lastGravity,0,event.values.size)
            lastGravitySet = true
        }
        else if(event?.sensor?.type == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR){
            System.arraycopy(event.values,0,lastGeomagnetic,0,event.values.size)
            lastGeomagneticSet = true
        }

        if(lastGravitySet && lastGeomagneticSet){
            SensorManager.getRotationMatrix(rotationMatrix, event?.values, lastGravity, lastGeomagnetic)
            SensorManager.getOrientation(rotationMatrix,orientation)
            azimuth = (Math.toDegrees(SensorManager.getOrientation(rotationMatrix,orientation)[0].toDouble())+360).toInt()%360
        }

        azimuth = Math.round(azimuth.toFloat())
        view?.spinCompass((-azimuth).toFloat())
        val angle = DistanceUtils().angle(currentLT!!, currentLG!!, lt, lg)
        view?.spinDestination((-azimuth).toFloat()-315.0f+angle.toFloat())
    }

    fun startCompass(){
        if(sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
            gravity = sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY)
            geomagnetic = sensorManager!!.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

            haveSensorGravity = sensorManager!!.registerListener(this,gravity,SensorManager.SENSOR_DELAY_UI)
            haveSensorGeomagnetic = sensorManager!!.registerListener(this, geomagnetic, SensorManager.SENSOR_DELAY_UI)
        }
        else{
            rotationVector = sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            haveSensorRotationVector = sensorManager!!.registerListener(this,rotationVector,SensorManager.SENSOR_DELAY_UI)
        }
    }
}