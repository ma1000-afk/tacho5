package com.pum.tachograph

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pum.tachograph.databinding.FragmentUserDetailBinding
import com.pum.tachograph.room.User

class UserDetail : Fragment() {
    private var _binding: FragmentUserDetailBinding?=null
    private val mainVm by activityViewModels<MainViewModel> ()
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("AAA","Detail1")
        _binding=FragmentUserDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingUserData(mainVm.getUser())
        mainVm.fraga=2

    }

    private fun bindingUserData(user: User?) {
        user ?: return

        binding.nameTV.text="${user.name} ${user.surname}"
        binding.techTV.text= user.surname



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}