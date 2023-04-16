package com.pum.tachograph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pum.tachograph.databinding.ActivityMainBinding
import com.pum.tachograph.room.DBViewModel


class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding : ActivityMainBinding
    private lateinit var navControler: NavController
    private lateinit var viewModel2: DBViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("AAAA","main")
        viewModel2=ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(application)
            .create(DBViewModel::class.java)


        val navHostFrag =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControler = navHostFrag.navController
        mainViewModel.fraga=4
        if(mainViewModel.fraga==0) navControler.navigate(R.id.startFragment)
        if(mainViewModel.fraga==1) navControler.navigate(R.id.userListFragment)
        if(mainViewModel.fraga==2) navControler.navigate(R.id.userDetail)
        if(mainViewModel.fraga==3) navControler.navigate(R.id.locationFragment)
        if(mainViewModel.fraga==4) navControler.navigate(R.id.oneMinuteFragment)
        //var ml=MyLocation(applicationContext)

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putByte("ddd",0)
    }
}