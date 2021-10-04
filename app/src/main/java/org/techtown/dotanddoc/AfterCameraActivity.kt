package org.techtown.dotanddoc

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.after_camera.*


class AfterCameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.after_camera)

        //intent.getParcelableExtra<Bitmap>("bitImage") 비트맵으로 받아올 경우.

        var image = intent.getStringExtra("image")
        val uri: Uri = Uri.parse(image)

        findViewById<ImageView>(R.id.imagePreview)
        imagePreview.setImageURI(uri)
    }
}



