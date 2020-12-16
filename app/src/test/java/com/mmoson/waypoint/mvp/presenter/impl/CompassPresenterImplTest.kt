package com.mmoson.waypoint.mvp.presenter.impl

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.mmoson.waypoint.UI.CompassFragment
import com.mmoson.waypoint.mvp.view.CompassView
import com.mmoson.waypoint.utils.DistanceUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class CompassPresenterImplTest {
    @JvmField
    @Rule
    var mockitoRule = MockitoJUnit.rule()

    lateinit var compassPresenter: CompassPresenterImpl

    @Mock
    lateinit var compassView: CompassView

    @Mock
    var context = mock(Context::class.java)

    @Mock
    var sensorManager = mock(SensorManager::class.java)

    @Mock
    var distanceUtils = mock(DistanceUtils::class.java)

    @Before
    fun setUp() {
        compassPresenter = CompassPresenterImpl(distanceUtils)
        compassView = mock(CompassFragment::class.java)
        compassPresenter.attachView(compassView)
    }

    @Test
    fun testSetDestLocation() {
        compassPresenter.setDestLocation(1.1, 1.1)
    }

    @Test
    fun testSetCompass() {
        `when`(context.getSystemService(Context.SENSOR_SERVICE))
            .thenReturn(sensorManager)
        compassPresenter.setCompass(context)
    }

    @Test
    fun testCalculateDistanceLong() {
        `when`(
            distanceUtils.distance(
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyChar()
            )
        )
            .thenReturn(20.0)

        compassPresenter.calculateDistance(1.0, 1.0)
        verify(compassView, times(1)).showDistance("20,0000", "km.")
    }


    @Test
    fun testCalculateDistanceShort() {
        `when`(
            distanceUtils.distance(
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyChar()
            )
        )
            .thenReturn(0.045)
        compassPresenter.calculateDistance(10.0, 10.0)
        verify(compassView, times(1)).showDistance("45", "m.")
    }

    @Test
    fun testOnSensorChanged_azimuthValue() {
        val event = mock(SensorEvent::class.java)
        compassPresenter.onSensorChanged(event)
        verify(compassView, times(1)).spinCompass(0.0f)
    }

    @Test
    fun testStartCompass_whenSensorIsNull() {
        val sensor = mock(Sensor::class.java)
        val listener = mock(SensorEventListener::class.java)
        `when`(context.getSystemService(Context.SENSOR_SERVICE))
            .thenReturn(sensorManager)
        `when`(sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)).thenReturn(null)
        `when`(sensorManager!!.getDefaultSensor(ArgumentMatchers.anyInt())).thenReturn(sensor)
        `when`(
            sensorManager!!.registerListener(
                listener,
                sensor,
                SensorManager.SENSOR_DELAY_UI
            )
        ).thenReturn(true)
        compassPresenter.setCompass(context)
        compassPresenter.startCompass()
    }

    @Test
    fun testStartCompass_whenSensorIsNotNull() {
        val sensor = mock(Sensor::class.java)
        val listener = mock(SensorEventListener::class.java)
        `when`(context.getSystemService(Context.SENSOR_SERVICE))
            .thenReturn(sensorManager)
        `when`(sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)).thenReturn(sensor)
        `when`(sensorManager!!.getDefaultSensor(ArgumentMatchers.anyInt())).thenReturn(sensor)
        `when`(
            sensorManager!!.registerListener(
                listener,
                sensor,
                SensorManager.SENSOR_DELAY_UI
            )
        ).thenReturn(true)
        compassPresenter.setCompass(context)
        compassPresenter.startCompass()
    }
}