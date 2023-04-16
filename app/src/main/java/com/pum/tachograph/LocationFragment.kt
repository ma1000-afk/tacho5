package com.pum.tachograph




import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.pum.tachograph.databinding.FragmentLocationBinding
import com.vmadalin.easypermissions.EasyPermissions


import java.security.Permission
import java.security.Provider
import java.util.*

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding?=null
     lateinit var locationRequest: LocationRequest
    private val binding get() = _binding!!
    companion object {
        const val PERMISSION_ID= 1010
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentLocationBinding.inflate(layoutInflater, container, false)
        Log.d("AAA","Jestem tu")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.getpos.setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            Log.d("AAA:","hhh")
            RequestPermission()
            getLastLocation()
        }
        binding.button.setOnClickListener {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        _binding = null
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location:Location? = task.result
                    NewLocationData()
                    if(location == null){

                        Log.d("Debug:",isLocationEnabled().toString())
                        Log.d("AAA:","dd")
                    }else{


                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        Log.d("AAA:","rnn"+ location.longitude)
                        binding.textView.text = "You Current Location is : Long: "+ location.longitude.toString() + " , Lat: " + location.latitude.toString()
                        binding.textView2.text=getCityName(location.latitude,location.longitude).toString()
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        // locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback,null
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location? = locationResult.lastLocation
            if(lastLocation!=null){
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            binding.textView.text = "You Last Location is : Long: "+ lastLocation.longitude.toString() + " , Lat: " + lastLocation.latitude.toString() + "\n"
            binding.textView2.text=getCityName(lastLocation.latitude,lastLocation.longitude)
        }}
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            requireContext() as Activity,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager =activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        Log.d("AAA:","rnn1")
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        Log.d("AAA:","rnn2")
        var Adress = geoCoder.getFromLocation(lat,long,1)
        Log.d("AAA:","rnn3")
        if(Adress!=null){
            Log.d("AAA:","rnn3")
            cityName = Adress[0].countryCode
            countryName = Adress[0].countryName
            Log.d("AAA:","rnn4")
            // Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        }
        binding.ccp.setCountryForNameCode(cityName)
        return cityName
    }
}