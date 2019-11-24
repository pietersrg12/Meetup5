package id.co.meetup5

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import id.co.plnntb.re100.helper.GPSTracker
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val REQUEST_PERMISSION_CODE = 100
    var currentPhoto: String? = null
    var gps: GPSTracker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("tambah foto")
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        val daftarIzin = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            daftarIzin.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            daftarIzin.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            daftarIzin.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            daftarIzin.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            daftarIzin.add(android.Manifest.permission.CAMERA)
        }
        if (daftarIzin.size > 0) {
            val iz = arrayOfNulls<String>(daftarIzin.size)
            for (i in 0 until daftarIzin.size) {
                iz[i] = daftarIzin.get(i)
            }
            ActivityCompat.requestPermissions(this, iz, REQUEST_PERMISSION_CODE)
        } else {
            gps = GPSTracker(this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_foto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        takePicture("${idpel.text}.jpg", 101)

        getKordinat.text = "${gps?.getLatitude()}, ${gps?.getLongitude()}"
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                101 -> {
                    hasilgambar.setImageURI(Uri.parse(currentPhoto))

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun takePicture(namafile: String, reqcode: Int) {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val filePhoto = File(getExternalFilesDir(null), namafile)

       val uriPhoto = FileProvider.getUriForFile(
           this,
            "id.co.meetup5.fileprovider",
            filePhoto
        )
        currentPhoto = filePhoto.absolutePath
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto)
        startActivityForResult(cameraIntent, reqcode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        gps = GPSTracker(this)
    }

}
