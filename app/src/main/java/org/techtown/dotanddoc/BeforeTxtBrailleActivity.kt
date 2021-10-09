package org.techtown.dotanddoc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.after_ocr.*

class BeforeTxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_ocr)

        trans_braille_btn.setOnClickListener({
            val BrailleIntent = Intent(this, TxtBrailleActivity::class.java)
            startActivity(BrailleIntent)
        })
    }
}