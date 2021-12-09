package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Typeface
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Кнопочки
        val btsettings = findViewById<ImageButton>(R.id.BtSettings)
        //Обработчики
        btsettings.setOnClickListener{
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }
        initViewPager()
    }

    private fun initViewPager() {
        val viewPager : ViewPager = findViewById(R.id.viewpage)
        val tabLayout : TabLayout = findViewById(R.id.tab_layout)
        val viewPagerAdapter  = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragments(SongsFragment(), title = "Песни")
        viewPagerAdapter.addFragments(AlbumFragment(), title = "Альбомы")
        viewPager.setAdapter(viewPagerAdapter)
        tabLayout.setupWithViewPager(viewPager)
    }

    class ViewPagerAdapter public constructor(@NonNull fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        private lateinit var fragments : ArrayList<Fragment>
        private lateinit var titles : ArrayList<String>

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


}