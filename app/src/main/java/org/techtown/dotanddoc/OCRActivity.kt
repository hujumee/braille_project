package org.techtown.dotanddoc

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.IOException

class OCRActivity : AppCompatActivity() {

    //edit.xml의 EditTextView
    val text_info : EditText = findViewById(R.id.content)
    val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit)
        //intent를 받아오는 코드 작성
        //imageFromPath(this, uri)
    }


    //uri로부터 이미지 가져오기
    private fun imageFromPath(context: Context, uri: Uri) {
        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, uri)
            //recognizeText(image)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //텍스트 인식
    private fun recognizeText(image: InputImage) {
        //이미지를 process method로 보냄
        val result = recognizer.process(image)
            .addOnSuccessListener {
                //이미지 인식이 성공했을 때
                //processTextBlock(result)
            }
            .addOnFailureListener {
                //이미지 인식이 실패했을 때
                //사용자에게 alert 작성
                //전 화면으로 돌아가는 코드 작성
            }
    }

    //process에서 받은 텍스트 블록 텍스트 추출
    private fun processTextBlock(result: Text) {
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
            }
        }
        //text_info.setText(resultText)
    }

}