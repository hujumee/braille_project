package org.techtown.dotanddoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.after_ocr.*

class BeforeTxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_ocr)

        val resultTxt: String? = intent.getStringExtra("resultTxt")

        trans_braille_btn.setOnClickListener({
            val BrailleIntent = Intent(this, TxtBrailleActivity::class.java)
            BrailleIntent.putExtra("resultTxt", resultTxt)
            startActivity(BrailleIntent)
        })
    }

}