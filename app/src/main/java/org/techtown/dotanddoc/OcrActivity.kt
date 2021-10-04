package org.techtown.dotanddoc

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OcrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        //intent.getParcelableExtra<Bitmap>("bitImage") 비트맵으로 받아올 경우.

        var ocrImage = intent.getStringExtra("ocrImage")
        val uri: Uri = Uri.parse(ocrImage) //이 uri 변수 사용하면 됩니다!!!
        
    }
}