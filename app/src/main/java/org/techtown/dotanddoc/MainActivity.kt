package org.techtown.dotanddoc

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val FLAG_PERM_CAMERA = 98
    val FLAG_PERM_STORAGE = 99

    val FLAG_REQ_CAMERA = 101
    val FLAG_REQ_STORAGE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        setSupportActionBar(main_layout_toolbar) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu) // 홈버튼 이미지 변경
        main_navigationView.setNavigationItemSelectedListener(this) //navigation 리스너

        findViewById<View>(R.id.img_select_btn).setOnClickListener { view ->
            val popupMenu = PopupMenu(applicationContext, view)
            menuInflater.inflate(R.menu.img_select, popupMenu.getMenu())

            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

                override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                    if (menuItem.itemId == R.id.gallery) {
                        if (checkPermission(STORAGE_PERMISSION, FLAG_PERM_STORAGE)) {
                            openGallery()
                        }
                        return true

                    } else if (menuItem.itemId == R.id.camera) {
                        if (checkPermission(STORAGE_PERMISSION, FLAG_PERM_STORAGE)) {
                            openCamera()
                        }
                    }
                    return false
                }
            })
            popupMenu.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
                main_drawer_layout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val homeIntent = Intent(this, MainActivity::class.java)

        val transDocIntent = Intent(this, TransformedDocActivity::class.java)

        val transBrailleIntent = Intent(this, TransformedBrailleActivity::class.java)

        val bookmarkIntent = Intent(this, BookmarkActivity::class.java)

        val setIntent = Intent(this, SetActivity::class.java)

        when(item.itemId){
            R.id.item1-> startActivity(homeIntent)
            R.id.item2-> startActivity(transDocIntent)
            R.id.item3-> startActivity(transBrailleIntent)
            R.id.item4-> startActivity(bookmarkIntent)
            R.id.item5-> startActivity(setIntent)
        }
        return false
    }

    fun setViews() {
        fun onMenuItemClick(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.gallery) {
                openGallery()
                return true
            } else if (menuItem.itemId == R.id.camera) {
                openCamera()
            }
            return false
        }
    }

    fun openCamera() {
        if (checkPermission(CAMERA_PERMISSION, FLAG_PERM_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, FLAG_REQ_CAMERA)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, FLAG_REQ_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val afterCameraIntent = Intent(this, AfterCameraActivity::class.java)
            val OcrIntent = Intent(this, OcrActivity::class.java)

            when (requestCode){
                FLAG_REQ_CAMERA -> {
                    if (data?.extras?.get("data") != null) {
                        val bitmap = data?.extras?.get("data") as Bitmap
                        //imageView.setImageBitmap(bitmap)
                        val uri = saveImageFile(newFileName(), "image/jpg", bitmap)
                        afterCameraIntent.putExtra("image", uri.toString())
                        //afterCameraIntent.putExtra("bitImage", bitmap) 비트맵으로 보낼 경우.
                        startActivity(afterCameraIntent)

                        OcrIntent.putExtra("ocrImage", uri.toString())
                        startActivity(OcrIntent)
                    }
                }
                FLAG_REQ_STORAGE -> {
                    val uri = data?.data
                    //imageView.setImageURI(uri)
                }
            }
        }
    }


    fun saveImageFile(filename:String, mimeType:String, bitmap: Bitmap) : Uri? {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            if (uri != null) {
                var descriptor = contentResolver.openFileDescriptor(uri, "w")
                if (descriptor != null) {
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, values, null, null)
                    }
                }
            }
        } catch (e:java.lang.Exception) {
            Log.e("File", "error=${e.localizedMessage}")
        }
        return uri
    }

    fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.jpg"
    }

    fun checkPermission(permissions: Array<out String>, flag: Int) : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FLAG_PERM_STORAGE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
                setViews()
            }

            FLAG_PERM_CAMERA -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인 해야지만 카메라를 사용할 수 있습니다.", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                openCamera()
            }
        }
    }
}








