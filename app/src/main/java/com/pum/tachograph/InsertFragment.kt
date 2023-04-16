package com.pum.tachograph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.pum.tachograph.R
import com.pum.tachograph.databinding.FragmentInsertBinding
import com.pum.tachograph.room.DBViewModel
import com.pum.tachograph.room.User


class InsertFragment : Fragment() {


    private  var _binding: FragmentInsertBinding?=null
    private  val binding get()=_binding!!
    private val mainVm by activityViewModels<MainViewModel> ()
    private val DBVm by activityViewModels<DBViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentInsertBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVm.fraga=3
        binding.btn1.setOnClickListener(){
            val imie = binding.ev1.text.toString()
            val nazwiska = binding.ev2.text.toString()
            val u:User=User(imie,nazwiska)
            DBVm.insertUser(u)
        }
        binding.btn2.setOnClickListener(){
            findNavController().navigate(R.id.action_insertFragment_to_DBULFragment)

        }
        binding.btn3.setOnClickListener(){
            DBVm.deleteAllRows()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }}