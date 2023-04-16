package com.pum.tachograph


import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pum.tachograph.databinding.FragmentDBULBinding

import com.pum.tachograph.room.DBViewModel
import com.pum.tachograph.room.User


class DBULFragment : Fragment() {
    private var _binding: FragmentDBULBinding?=null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<MainViewModel> ()
    private val DBVm by activityViewModels<DBViewModel> ()

    private lateinit var adapter2:DBAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentDBULBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVm.fraga=4
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        var us=DBVm.getAllUser()
        us.observe(viewLifecycleOwner, Observer{
            if(it.isNotEmpty()){
                adapter2 =DBAdapter(it,onUserClick ={ user -> mainVm.setUser(user)
                    findNavController().navigate(R.id.action_DBULFragment_to_userDetail)})
                binding.recyclerView.adapter=adapter2
            }
        })

/*
        val adapter2=DBAdapter(users = us) { user ->
            mainVm.setUser(user)
            Log.d("LLLL","Jestem")
            findNavController().navigate(R.id.action_DBULFragment_to_userDetail)
        }
         binding.recyclerView.adapter=adapter2
*/


        Log.d("AAA","List1")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null

    }

}