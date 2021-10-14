package org.techtown.dotanddoc

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class TxtBrailleActivity : AppCompatActivity() {

    private var tryDownload: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.)

        val resultTxt = intent.getStringExtra("resultTxt") //이놈이 텍스트 파일 최종으로 가져온 겁니다.

        val nowDate = newFileName()
        val fileName = "$nowDate.txt"
        uploadFile(resultTxt, fileName)

        val downloadKey = "${nowDate.substring(0,8)}/$nowDate.brf" // "yyyyMMdd/yyyyMMdd_HHmmss.brf"
        //다운로드 파일 함수 키 파라미터에 public 빼세요
        Handler().postDelayed({
            downloadFile(downloadKey)
        }, 30000)

    }

    override fun onBackPressed() {
        tryDownload?.cancel()
        super.onBackPressed()
    }

    fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename"
    }

    private fun uploadFile(txtContent: String?, fileName: String) {
        val uploadFile = File(applicationContext.filesDir, fileName)
        //파일명에 '/'붙이지 말자 오류난다

        val stringTxtContent = txtContent.toString()
        uploadFile.writeText(stringTxtContent)


        val options = StorageUploadFileOptions.defaultInstance()

        val uploadKey = "${fileName.substring(0,8)}/${fileName.substring(0,15)}.txt"
        Log.d("MyAmplifyApp", uploadKey)

        Amplify.Storage.uploadFile(uploadKey, uploadFile, options,
            { Log.i("MyAmplifyApp", "Fraction completed: ${it.fractionCompleted}") },
            { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
            { Log.e("MyAmplifyApp", "Upload failed", it) }
        )
    }

    private fun downloadFile(uploadedKey: String) {
        // 저장위치: 내부 저장소 => 수정해야함
        val file = File("${applicationContext.filesDir}/download")
        //val file = File("${Environment.getExternalStorageDirectory()}")

        val options = StorageDownloadFileOptions.defaultInstance()

        var second = 0
        tryDownload = timer(period = 3000) {
            //3초에 한번씩 재시도
            second ++

            Amplify.Storage.downloadFile(uploadedKey, file, options,
                { progress -> Log.d("MyAmplifyApp", "Fraction completed: $progress") },
                { success ->
                    Log.d("MyAmplifyApp", "Successfully downloaded: $success")
                    cancel()
                    //startActivity() or Toast message
                },
                { exception -> Log.d("MyAmplifyApp", "Download Failure", exception) }
            )

            if (second == 60) {
                //60번 시도시 cancel()
                cancel()
                //alert()
            }
        }

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

        //TxtBrailleActivity의 뷰 = 로딩
        //1분동안 루프문 돌리기 -> withTimeout
        //다운로드에 성공하면 루프문 빠져나오면서 뷰전환
        //실패한 경우에는 alert띄워서 선택할 수 있게끔
        /*
            다시 시도하시겠습니까?
            yes -> uploadFile 함수 다시 호출
            no -> main으로 돌아감
         */
    }

}