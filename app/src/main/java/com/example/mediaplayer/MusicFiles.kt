package com.example.mediaplayer

import android.os.Parcel
import android.os.Parcelable

class MusicFiles(var path : String,var tittle : String,var artist : String,var album : String,var duration : String) {

    init {
        this.path = path
        this.tittle = tittle
        this.artist = artist
        this.album = album
        this.duration = duration
    }

}
