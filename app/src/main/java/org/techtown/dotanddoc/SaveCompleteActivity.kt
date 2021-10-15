package org.techtown.dotanddoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SaveCompleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.braille_download)
        //뷰 추가해서 수정해주세요 - 추가완료

        val finishDownload = findViewById<Button>(R.id.finish_download)

        finishDownload.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(mainIntent)
        }
    }
}