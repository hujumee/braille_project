package org.techtown.dotanddoc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import java.io.File
import java.text.SimpleDateFormat

class TxtBrailleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        val resultTxt = intent.getStringExtra("resultTxt") //이놈이 텍스트 파일 최종으로 가져온 겁니다.

        var uploadKey = newFileName()
        uploadFile(resultTxt, uploadKey)
        Log.d("MyAmplifyApp", uploadKey)
        /*
        setOnClickListener
        downloadFile()
         */
    }

    fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.txt"
    }

    private fun uploadFile(txtContent: String?, fileName: String) {
        //resultTxt가 string일 경우
        val uploadFile = File(applicationContext.filesDir, fileName)

        val stringTxtContent = txtContent.toString()
        uploadFile.writeText(stringTxtContent)

        //파일 내용 확인
        //Log.d("MyAmplifyApp", "전달된 txt파일 내용: ${stringTxtContent}")

        val options = StorageUploadFileOptions.defaultInstance()

        Amplify.Storage.uploadFile(fileName, uploadFile, options,
            { Log.i("MyAmplifyApp", "Fraction completed: ${it.fractionCompleted}") },
            { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
            { Log.e("MyAmplifyApp", "Upload failed", it) }
        )
    }

    private fun downloadFile(uploadKey: String) {
        // 저장위치: 내부 저장소 => 수정해야함
        val file = File("${applicationContext.filesDir}/download")

        val options = StorageDownloadFileOptions.defaultInstance()

        Amplify.Storage.downloadFile(uploadKey, file, options,
            { Log.i("MyAmplifyApp", "Fraction completed: ${it.fractionCompleted}") },
            { Log.i("MyAmplifyApp", "Successfully downloaded: ${it.file.name}") },
            { Log.e("MyAmplifyApp", "Download Failure", it) }
        )

        /*
        해당 스토리지에 파일이 생성되기까지 시간이 걸림 비교적 짧은 한장 txt -> 30초정도
        만약 파일이 아직 생성되지 않은 경우, 로딩 띄우고 재시도하면서 기다리게 하도록 하는 코드 작성하는 게 좋을 듯함
        if(File = null) {
            기다려주세요 view call
            timeout정해두고 time동안 재시도
            time동안 call 불가 => retry()
        }
        //에러난 경우, upload함수를 다시 호출할 수 있도록 하는 retry 함수
        retry() {
            다시 시도하시겠습니까?
            yes -> uploadFile 함수 다시 호출
            no -> main으로 돌아감
         */
    }
}