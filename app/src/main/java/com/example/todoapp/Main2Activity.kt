package com.example.todoapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar:Calendar

    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener

    val db by lazy {
        Room.databaseBuilder(this,
            AppDatabase::class.java,
            "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    var trigger = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar5)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        et1.setText(intent.getStringExtra("Description"))

        et2.setText(intent.getStringExtra("Details"))

        save.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)

            trigger.value = false

            GlobalScope.launch(Dispatchers.Main) {

                val a = withContext(Dispatchers.IO){
                    db.todoDao().insert(TODO(et1.text.toString(),et2.text.toString())) }

                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@Main2Activity,"Entry With id = $a",Toast.LENGTH_SHORT).show()
                }
            }
            startActivity(i)
            finish()
        }
        dateEdt.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdt->{
                setListener()
            }
        }
    }

    private fun setListener(){
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker, year:Int, month:Int, dayOfMonth:Int->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate(){
        val myFormat = "EEE , d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        dateEdt.setText(sdf.format(myCalendar.time))

        timeInpLay.visibility = View.VISIBLE
    }
}
