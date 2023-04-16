package com.pum.tachograph


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.pum.tachograph.databinding.FragmentUserListBinding


class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding?=null
    private val binding get() = _binding!!
    private val mainVm by activityViewModels<MainViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentUserListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVm.fraga=1
        val adapter=ULAdapter(users= mainVm.loadDataFromDb(), onUserClick ={ user -> mainVm.setUser(user)
            findNavController().navigate(R.id.action_userListFragment_to_userDetail)})

        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerView.adapter=adapter
        Log.d("AAA","List1")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null

    }

}