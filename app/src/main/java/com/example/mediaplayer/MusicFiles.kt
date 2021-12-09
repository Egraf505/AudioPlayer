package com.example.mediaplayer

import android.os.Parcel
import android.os.Parcelable

class MusicFiles() {
    private var path:String = ""
    private var tittle = ""
    private var artist = ""
    private var album = ""
    private var duration = ""

    constructor(path : String,tittle : String,artist : String,album : String,duration : String) : this() {
        this.path = path
        this.tittle = tittle
        this.artist = artist
        this.album = album
        this.duration = duration
    }

}
