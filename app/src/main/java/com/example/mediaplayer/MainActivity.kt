package com.example.mediaplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 1
    companion object{
        @JvmField
        var musicFiles : ArrayList<MusicFiles> = ArrayList<MusicFiles>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission()
        //Кнопочки
        val btsettings = findViewById<ImageButton>(R.id.BtSettings)
        //Обработчики
        btsettings.setOnClickListener{
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

    }

    private fun permission() {

        if (ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf<String>(WRITE_EXTERNAL_STORAGE) , REQUEST_CODE)
        else {
            musicFiles = getAllAudio(this)
            initViewPager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this)
                initViewPager()
            }
            else
                ActivityCompat.requestPermissions(this, arrayOf<String>(WRITE_EXTERNAL_STORAGE) , REQUEST_CODE)
    }

    private fun initViewPager() {
        val viewPager : ViewPager = findViewById(R.id.viewpage)
        val tabLayout : TabLayout = findViewById(R.id.tab_layout)
        val viewPagerAdapter  = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragments(SongsFragment(), title = "Песни")
        viewPagerAdapter.addFragments(AlbumFragment(), title = "Альбомы")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class ViewPagerAdapter public constructor(@NonNull fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        private  var fragments : ArrayList<Fragment> = ArrayList<Fragment>()
        private  var titles : ArrayList<String> = ArrayList<String>()

        fun addFragments(fragment: Fragment, title : String){
            fragments.add(fragment)
            titles.add(title)
        }

        @NonNull
        @Override
        override fun getItem(position : Int):Fragment{
            return fragments.get(position)
        }

        @Override
        override fun getCount():Int{
            return fragments.size
        }

        @NonNull
        @Override
        override fun getPageTitle(position: Int): CharSequence? {
            return titles.get(position)
        }
    }

    fun getAllAudio(context: Context) : ArrayList<MusicFiles> {
        var tempAudioList = ArrayList<MusicFiles>()
        var uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, //for path
            MediaStore.Audio.Media.ARTIST
        )
        var cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                var album = cursor.getString(0)
                var title = cursor.getString(1)
                var duration = cursor.getString(2)
                var path = cursor.getString(3)
                var artist = cursor.getString(4)

                var musicFiles = MusicFiles(path, title, artist, album, duration)

                //take Log.e for check
                Log.e("Path :" + path, "Album :" + album)
                tempAudioList.add(musicFiles)
            }
            cursor.close()
        }
        return tempAudioList
    }

}