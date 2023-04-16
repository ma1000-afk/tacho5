package com.pum.tachograph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


import com.pum.tachograph.databinding.UlRowBinding
import com.pum.tachograph.room.User

class ULAdapter(private val users:List<User>, private val onUserClick:(User)->Unit) :RecyclerView.Adapter<ULAdapter.MyVH>(){
    inner class MyVH(binding: UlRowBinding): ViewHolder(binding.root){
        val image=binding.iv1
        val name=binding.tv1
        val surname=binding.tv2
        init{
            binding.root.setOnClickListener{onUserClick(users[adapterPosition])}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return  MyVH(binding = UlRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {

        holder.name.text=users[position].name

        holder.surname.text=users[position].surname
    }

    override fun getItemCount(): Int {
        return  users.size
    }

}