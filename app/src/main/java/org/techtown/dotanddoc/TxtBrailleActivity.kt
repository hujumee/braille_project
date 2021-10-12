package org.techtown.dotanddoc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        val resultTxt = intent.getStringExtra("resultTxt") //이놈이 텍스트 파일 최종으로 가져온 겁니다.
    }
}