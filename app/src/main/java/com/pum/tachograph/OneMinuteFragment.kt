package com.pum.tachograph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color


import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.tv.interactive.AppLinkInfo
import androidx.appcompat.app.AppCompatActivity

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.pum.tachograph.databinding.FragmentOneMinuteBinding



import java.util.*
class OneMinuteFragment : Fragment(), SensorEventListener {
    private  var _binding: FragmentOneMinuteBinding?=null
    private  val binding get()=_binding!!
    lateinit var dataHelper: DataHelper
    private val timer = Timer()
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private  var timerValue:Long = 0
    var isSensorMoved = false
    var sensorSwitch = false
    var oneMinFail = false

    var currentSensorRead: Float = 0.0f
    var previousSensorRead: Float = 0.0f
    var sensorDelta: Float = 0.0f
    var sensorGravity: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentOneMinuteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataHelper = DataHelper(requireContext())

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        binding.moveRightBtb.setOnClickListener{
            findNavController().navigate(R.id.action_oneMinuteFragment_to_locationFragment)
        }
        binding.startButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener { resetAction()
            binding.root.setBackgroundColor(Color.WHITE)
            binding.prompt2TV.setBackgroundColor(Color.WHITE)
            binding.prompt1TV.setBackgroundColor(Color.WHITE)
            binding.prompt1TV.text="I'm Ready"
            binding.prompt2TV.text="For a ride"
            timerValue=0
            isSensorMoved = false
            sensorSwitch = false
            oneMinFail = false
        }

        Log.d("AAA", "onCreate")
        if (dataHelper.timerCounting()) {
            Log.d("AAA", dataHelper.timerCounting().toString() + " helper")
            startTimer()
        } else {
            stopTimer()
            Log.d("AAA", "stop")
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                Log.d("AAA", "stop2")
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)


    }/////onViewCreatedEnd

    override fun onSensorChanged(event: SensorEvent?) {
        // Checks for the sensor we have registered
        Log.d("AAA", "onSensor")
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // Sides = Tilting phone left(10) and right(-10)
            val x = event.values[0]
            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val y = event.values[1]
            val z = event.values[2]
            currentSensorRead = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            Log.d("AAA", currentSensorRead.toString())
            if (isSensorMoved == false) {
                previousSensorRead = currentSensorRead;sensorGravity = currentSensorRead;isSensorMoved = true
            }
        }
        sensorDelta = Math.abs(currentSensorRead - previousSensorRead)
        previousSensorRead = currentSensorRead
/*OldSensorControlBlock
        if (!sensorDelta.equals(0.0f) && sensorSwitch == false) {
            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            //binding.lineTv.text=sensorDelta.toString()
            startStopAction()
            sensorSwitch = true
        }
        if (sensorDelta.equals(0.0f) && sensorSwitch == true) {

            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            //binding.lineTv.text=sensorDelta.toString()
            startStopAction()
            sensorSwitch = false
        }
OldSensorControlBlockEnd */
        //StartCounting
        if (!sensorDelta.equals(0.0f) && sensorSwitch == false&&oneMinFail==false&&timerValue<29000) {
            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            binding.prompt1TV.text="Drive"
            binding.prompt2TV.text="Less then 30 seconds."
            startStopAction()

            sensorSwitch = true
        }
        if (!sensorDelta.equals(0.0f) && sensorSwitch == true&&oneMinFail==false&&timerValue<29000) {
            if(timerValue<30000&&timerValue>19999){binding.prompt1TV.setBackgroundColor(Color.RED);binding.prompt2TV.setBackgroundColor(Color.RED)}
            if(timerValue<20000&&timerValue>9999){binding.prompt1TV.setBackgroundColor(Color.YELLOW);binding.prompt2TV.setBackgroundColor(Color.YELLOW)}
            if(timerValue<10000&&timerValue>1){binding.prompt1TV.setBackgroundColor(Color.GREEN);binding.prompt2TV.setBackgroundColor(Color.GREEN)}

            sensorSwitch = true
        }


        //Just on time
        if (sensorDelta.equals(0.0f) && sensorSwitch == true&&oneMinFail==false&&timerValue<29000) {
            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            //binding.lineTv.text=sensorDelta.toString()
            binding.prompt1TV.text="O.K. STOP";
            binding.prompt2TV.text="Wait for wile.";
            binding.prompt2TV.setBackgroundColor(Color.RED)
            binding.prompt1TV.setBackgroundColor(Color.RED)
        }


        //To late
        if (!sensorDelta.equals(0.0f) && sensorSwitch == true&&oneMinFail==false&&timerValue>=29000&&timerValue<60000) {
            binding.prompt1TV.text="STOP";binding.prompt2TV.setBackgroundColor(Color.RED)
            binding.prompt2TV.text="Wait about 60 seconds.";binding.prompt1TV.setBackgroundColor(Color.RED)
            binding.root.setBackgroundColor(Color.RED)
            oneMinFail=true
        }

        //Patient
        if (sensorDelta.equals(0.0f) && sensorSwitch == true&&oneMinFail==false&&timerValue>59000) {
            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            //binding.lineTv.text=sensorDelta.toString()
            binding.prompt1TV.text="I'm Ready"
            binding.prompt2TV.text="For a ride"
            binding.root.setBackgroundColor(Color.WHITE)
            binding.prompt2TV.setBackgroundColor(Color.WHITE)
            binding.prompt1TV.setBackgroundColor(Color.WHITE)
            sensorSwitch =false
            oneMinFail==false
            isSensorMoved = false
            timerValue=0
            resetAction()

        }
        //UnPatient
        if (sensorDelta.equals(0.0f) && sensorSwitch == true&&oneMinFail==true&&timerValue>90000) {
            Log.d("AAA", "chproxstart " + sensorDelta.toString())
            //binding.lineTv.text=sensorDelta.toString()
            binding.prompt1TV.text="I'm Ready"
            binding.prompt2TV.text="For a ride"
            binding.root.setBackgroundColor(Color.WHITE)
            binding.prompt2TV.setBackgroundColor(Color.WHITE)
            binding.prompt1TV.setBackgroundColor(Color.WHITE)
            sensorSwitch =false
            oneMinFail=false
            isSensorMoved = false
            timerValue=0
            resetAction()

        }


    //    if(timerValue<30000&&timerValue>19999){binding.prompt1TV.setBackgroundColor(Color.RED);binding.prompt2TV.setBackgroundColor(Color.RED)}
    //    if(timerValue<20000&&timerValue>9999){binding.prompt1TV.setBackgroundColor(Color.YELLOW);binding.prompt2TV.setBackgroundColor(Color.YELLOW)}
    //    if(timerValue<10000&&timerValue>1){binding.prompt1TV.setBackgroundColor(Color.GREEN);binding.prompt2TV.setBackgroundColor(Color.GREEN)}
    //    if(timerValue>29999&&sensorSwitch==true){
    //        binding.root.setBackgroundColor(Color.RED)
    //        binding.prompt2TV.text="Wait 60 seconds"
    //        binding.prompt1TV.text="Faild, to long"
    //         oneMinFail = true
    //    }




    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }


    ///////////////////SensorEnd

    //////////Timer
    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time
                binding.timeTV.text = timeStringFromLong(time)
                Log.d("AAAAA",time.toString())
                timerValue=time
            }
        }
    }

    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        sensorSwitch =false
        oneMinFail==false
        isSensorMoved = false
        timerValue=0
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }

    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }

    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)
    }

    private fun startStopAction() {
        if (dataHelper.timerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        resetAction()
        _binding = null
    }

}///EndOfFragment