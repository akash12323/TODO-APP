package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(val user:ArrayList<TODO>):
    RecyclerView.Adapter<TodoAdapter.ItemVieHolder>() {

    var onItemClick:((user: TODO)->Unit)? = null

    var trigger = MutableLiveData<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVieHolder {
        return ItemVieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = user.size

    override fun onBindViewHolder(holder: ItemVieHolder, position: Int) {
        holder.bind(user[position])
    }

    override fun getItemId(position: Int): Long {
        return user[position].id
    }

    inner class ItemVieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(user: TODO){
            itemView.apply {
                tv1.text = user.descrition
                tv2.text = user.detail
                setOnClickListener {
                    onItemClick?.invoke(user)
                }
            }
        }
    }
}