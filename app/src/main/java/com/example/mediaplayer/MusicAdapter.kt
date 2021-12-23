package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.Array
import java.nio.file.Files.delete

class MusicAdapter(var mContext : Context, var mFiles : ArrayList<MusicFiles> ) : RecyclerView.Adapter<MusicAdapter.MyVieHolder>() {

    class MyVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var file_name : TextView
         var album_art : ImageView

        init {
            file_name = itemView.findViewById(R.id.music_file_name)
            album_art = itemView.findViewById(R.id.music_img)
        }
    }

    private fun getAllbumArt(uri: String) : ByteArray? {
        var retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        var art : ByteArray? = retriever.embeddedPicture
        retriever.release()
        return art
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVieHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false)
        return MyVieHolder(view)
    }

    override fun onBindViewHolder(holder: MyVieHolder, position: Int) {
        holder.file_name.setText(mFiles.get(position).tittle)
        var image : ByteArray? = getAllbumArt(mFiles.get(position).path)
        if (image !== null)
        {
            Glide.with(mContext).asBitmap().load(image).into(holder.album_art)
        }
        else{
            Glide.with(mContext).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(holder.album_art)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            var intent = Intent(mContext, PlayerActivity::class.java)
            intent.putExtra("position", position)
            mContext.startActivity(intent) })

    }


    override fun getItemCount(): Int {
        return mFiles.size
    }
}

