package com.example.mediaplayer

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mediaplayer.MainActivity.Companion.musicFiles
import com.example.mediaplayer.PlayerActivity.Companion.mediaPlayer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayerActivity : AppCompatActivity() {

    lateinit var song_name: TextView
    lateinit var artist_name: TextView
    lateinit var duration_played: TextView
    lateinit var duration_total: TextView

    lateinit var cover_art: ImageView
    lateinit var nextBtn: ImageView
    lateinit var prebBtn: ImageView
    lateinit var backBtn: ImageView
    lateinit var shuffleBtn: ImageView
    lateinit var repeatBtn: ImageView

    lateinit var playPauseBtn: FloatingActionButton

    lateinit var seekBar: SeekBar

    var position  = -1

    companion object{
        @JvmField
        var listSongs : ArrayList<MusicFiles> = ArrayList<MusicFiles>()
        @JvmField
        var uri : Uri = Uri.EMPTY
        @JvmField
        var mediaPlayer : MediaPlayer = MediaPlayer()
    }

    private var handler = Handler()

    private lateinit var playThread : Thread
    private lateinit var prevThread : Thread
    private lateinit var nextThread : Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initViews()
        getIntenMethod()
        song_name.setText(listSongs.get(position).tittle)
        artist_name.setText(listSongs.get(position).artist)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //onStartTrackingTouch
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //onStopTrackingTouch
            }
        })
        this@PlayerActivity.runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null){
                    var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                    seekBar.setProgress(mCurrentPosition)
                    duration_played.setText(formattedTime(mCurrentPosition))
                }
                handler.postDelayed(this,1000)
            }
        })

    }

    override fun onResume() {
        playThreadBtn()
        nextThreadBtn()
        prevThreadBtn()
        super.onResume()
    }

    private fun prevThreadBtn() {
        prevThread = object : Thread() {
            override fun run() {
                super.run()
                prebBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        prevBtnClicked()
                    }
                })
            }
        }
        prevThread.start()
    }

    private fun prevBtnClicked() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
            position = (if((position - 1) < 0) (listSongs.size - 1) else(position - 1))
            uri = Uri.parse(listSongs.get(position).path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.setText(listSongs.get(position).tittle)
            artist_name.setText(listSongs.get(position).artist)
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause)
            mediaPlayer.start()
        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            position = (if((position - 1) < 0) (listSongs.size - 1) else(position - 1))
            uri = Uri.parse(listSongs.get(position).path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.setText(listSongs.get(position).tittle)
            artist_name.setText(listSongs.get(position).artist)
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow)
        }
    }

    private fun nextThreadBtn() {
        nextThread = object : Thread() {
            override fun run() {
                super.run()
                nextBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        nextBtnClicked()
                    }
                })
            }
        }
        nextThread.start()
    }

    private fun nextBtnClicked() {
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs.get(position).path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.setText(listSongs.get(position).tittle)
            artist_name.setText(listSongs.get(position).artist)
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause)
            mediaPlayer.start()
        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs.get(position).path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.setText(listSongs.get(position).tittle)
            artist_name.setText(listSongs.get(position).artist)
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow)
        }
    }

    private fun playThreadBtn() {
        playThread = object : Thread() {
            override fun run() {
                super.run()
                playPauseBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        playPauseBtnClicked()
                    }
                })
            }
        }
        playThread.start()
    }

    private fun playPauseBtnClicked() {
        if (mediaPlayer.isPlaying){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_arrow)
            mediaPlayer.pause()
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
        }
        else{
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause)
            mediaPlayer.start()
            seekBar.max = mediaPlayer.duration / 1000
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null){
                        var mCurrentPosition : Int = mediaPlayer.currentPosition / 1000
                        seekBar.setProgress(mCurrentPosition)
                    }
                    handler.postDelayed(this,1000)
                }
            })
        }
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalout = ""
        var totalNew = ""
        var seconds : String = (mCurrentPosition % 60).toString()
        var minutes : String = (mCurrentPosition / 60).toString()
        totalout = minutes + ":" + seconds
        totalNew = minutes + ":" + "0" + seconds
        if (seconds.length == 1){
            return  totalNew
        }
        else
            return totalout
    }
    private fun getIntenMethod() {
        position = intent.getIntExtra("position",-1)
        listSongs = musicFiles
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause)
            uri = Uri.parse(listSongs.get(position).path)
        }
        if (mediaPlayer != null){
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
        }
        else{
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
        }
        seekBar.max = mediaPlayer.duration / 1000
        metaData(uri)
    }

    private fun initViews() {
        song_name = findViewById(R.id.song_name)
        artist_name = findViewById(R.id.song_artist)
        duration_played = findViewById(R.id.duretionPlayed)
        duration_total = findViewById(R.id.durationTotal)

        cover_art = findViewById(R.id.cover_art)
        nextBtn = findViewById(R.id.id_next)
        prebBtn = findViewById(R.id.id_prev)
        backBtn = findViewById(R.id.back_btn)
        shuffleBtn = findViewById(R.id.id_shuffle)
        repeatBtn = findViewById(R.id.id_repeat)

        playPauseBtn = findViewById(R.id.play_pause)

        seekBar = findViewById(R.id.seekBar)
    }

    private fun metaData(uri: Uri){
        var retriever : MediaMetadataRetriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        var durationTotal = (listSongs.get(position).duration).toInt() / 1000
        duration_total.setText(formattedTime(durationTotal))
        var art : ByteArray? = retriever.embeddedPicture
        if (art != null)
        {
            Glide.with(this).asBitmap().load(art).into(cover_art)
        }
        else
        {
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(cover_art)
        }
    }
}


