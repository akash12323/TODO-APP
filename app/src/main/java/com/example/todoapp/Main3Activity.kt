package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Main3Activity : AppCompatActivity() {

    val db by lazy {
        Room.databaseBuilder(this,AppDatabase::class.java,"app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        setSupportActionBar(toolbar3)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView1.setText(intent.getStringExtra("description"))
        textView2.setText(intent.getStringExtra("detail"))
        val p = intent.getLongExtra("ID",0L).toString()

        update.setOnClickListener {
            val i = Intent(this,Main2Activity::class.java)

            i.putExtra("Description",textView1.text.toString())
            i.putExtra("Details",textView2.text.toString())

            GlobalScope.launch(Dispatchers.Main){
                withContext(Dispatchers.IO){
                    db.todoDao().delete(p.toLong())
                }
            }
            startActivity(i)
            finish()
        }
    }
}

/*

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="70dp"
        android:layout_marginStart="16dp">
        <EditText
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/editText"
            android:hint="enter the id to delete"/>

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="@drawable/button_background"
            android:text="Delete"
            android:id="@+id/delete"
            android:layout_marginStart="65dp"/>

    </LinearLayout>


    delete.setOnClickListener {
            trigger.value = false
            GlobalScope.launch(Dispatchers.Main){
                if (editText.text.toString() == userId.text.toString()){
                    var a = editText.text.toString()
                    withContext(Dispatchers.IO){ db.todoDao().delete(a.toLong())}
                }
                else{
                    withContext(Dispatchers.Main){ Toast.makeText(this@MainActivity,"no user found",Toast.LENGTH_SHORT).show()}
                }
                //withContext(Dispatchers.IO){ db.todoDao().delete(3)}
            }
        }

 */