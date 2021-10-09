package org.techtown.dotanddoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        var resultTxt = intent.getStringExtra("resultTxt")
    }
}