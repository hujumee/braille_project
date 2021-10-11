package org.techtown.dotanddoc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        val resultTxt = intent.getStringExtra("resultTxt")

        Log.d("areyouOK", resultTxt.toString())
    }
}