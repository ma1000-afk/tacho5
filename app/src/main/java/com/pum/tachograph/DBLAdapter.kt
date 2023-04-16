package com.pum.tachograph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


import com.pum.tachograph.databinding.UlRowBinding
import com.pum.tachograph.room.User
//, private val onUserClick:(User)->Unit
class DBAdapter(private val users: List<User>, private val onUserClick:(User)->Unit) :RecyclerView.Adapter<DBAdapter.MyVH2>(){
    inner class MyVH2(binding: UlRowBinding): ViewHolder(binding.root){

        val name=binding.tv1
        val surname=binding.tv2

        init{
            binding.root.setOnClickListener{onUserClick(users[adapterPosition])}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH2 {
        return  MyVH2(binding = UlRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyVH2, position: Int) {

        holder.name.text=users[position].name

        holder.surname.text=users[position].surname
    }

    override fun getItemCount(): Int {
        return  users.size
    }

}