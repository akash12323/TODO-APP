package com.example.todoapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    val db by lazy {
        Room.databaseBuilder(this,
            AppDatabase::class.java,
            "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    val list = arrayListOf<TODO>()
    val adapter = TodoAdapter(list)

    var trigger = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        adapter.onItemClick = {
            val i = Intent(this@MainActivity,Main3Activity::class.java)

            val s:String = it.descrition
            val p:String = it.detail

            i.putExtra("description",s)
            i.putExtra("detail",p)

            startActivity(i)
        }

        addBtn.setOnClickListener(){
            val i = Intent(this,Main2Activity::class.java)
            startActivity(i)
            finish()
        }

        db.todoDao().getAllUser().observe(this, Observer {
            list.clear()
            trigger.value = true
            list.addAll(it)
            adapter.notifyDataSetChanged()
        })

        databaseRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL,false)
            adapter = this@MainActivity.adapter
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        return itemTouchHelper.attachToRecyclerView(databaseRv)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)

        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView
        searchView.setQueryHint("type here to search")

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                displayTodo()
                return true
            }
        })

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    displayTodo(newText)
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun displayTodo(newText:String = ""){
        db.todoDao().getAllUser().observe(this,Observer{
            if (it.isNotEmpty()){
                list.clear()
                list.addAll(
                    it.filter {todo ->
                        todo.descrition.contains(newText,true)
                    }
                )
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.settings){
            Toast.makeText(this,"you clicked settings",Toast.LENGTH_SHORT).show()
        }
        else if (id == R.id.search){
            Toast.makeText(this,"you clicked search",Toast.LENGTH_SHORT).show()
        }
        else if (id == R.id.reminder){
            Toast.makeText(this,"you clicked set reminder",Toast.LENGTH_SHORT).show()
        }
        return true
    }

    val simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.START
                or ItemTouchHelper.END
                or ItemTouchHelper.UP
                or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
                or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            Collections.swap(list,fromPosition,toPosition)
            databaseRv.adapter?.notifyItemMoved(fromPosition,toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.LEFT){
                GlobalScope.launch(Dispatchers.IO) { db.todoDao().delete(adapter.getItemId(position)) }
            }
            else if (direction == ItemTouchHelper.RIGHT){
                GlobalScope.launch(Dispatchers.IO) { db.todoDao().delete(adapter.getItemId(position)) }
            }
        }
    }

}

