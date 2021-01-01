package com.example.notekeeperk.Adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notekeeperk.*
import com.example.notekeeperk.ui.activities.NoteActivity

class NoteRecycleAdapter(private val context:Context, private val notes:List<com.example.data.NoteInfo>)
    : RecyclerView.Adapter<NoteRecycleAdapter.ViewHolder>() {

        private val LayoutInflater = android.view.LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.inflate(R.layout.item_note_list, parent, false )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.textCourse?.text= note.course?.title
        holder.textTitle?.text = note.title
        holder.notePosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCourse = itemView.findViewById<TextView>(R.id.textCourse)
        val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        var notePosition = 0
        init {
            itemView?.setOnClickListener{
                val intent = Intent(context, NoteActivity::class.java)
                intent.putExtra(NOTE_POSITION, notePosition)
                context.startActivity(intent)

            }
        }


    }
}