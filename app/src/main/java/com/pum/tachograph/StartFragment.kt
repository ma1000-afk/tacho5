package com.pum.tachograph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pum.tachograph.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private  var _binding: FragmentStartBinding?=null
    private  val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentStartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener(){
            findNavController().navigate(R.id.action_startFragment_to_insertFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}