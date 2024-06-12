package com.example.datatemansiti

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterr (private val listTeman: ArrayList<datakonco>,
                            private val context: Context
) : RecyclerView.Adapter<RecyclerViewAdapterr.ViewHolder>() {
    var listener: dataListener? = null

    init {
        this.listener = context as? MyListData
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Nama: TextView = itemView.findViewById(R.id.nama1)
        val Alamat: TextView = itemView.findViewById(R.id.alamat1)
        val NoHP: TextView = itemView.findViewById(R.id.nohp1)
        val ListItem: LinearLayout = itemView.findViewById(R.id.list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.view_design, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teman = listTeman[position]
        holder.Nama.text = "Nama : ${teman.nama}"
        holder.Alamat.text = "Alamat : ${teman.alamat}"
        holder.NoHP.text = "No HP : ${teman.no_hp}"

        holder.ListItem.setOnLongClickListener { view ->
            val action = arrayOf("Update", "Delete")
            val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
            alert.setItems(action) { _, i ->
                when (i) {
                    0 -> {
                        val bundle = Bundle().apply {
                            putString("dataNama", teman.nama)
                            putString("dataAlamat", teman.alamat)
                            putString("dataNoHP", teman.no_hp)
                            putString("getPrimaryKey", teman.key)
                        }
                        val intent = Intent(view.context, UpdateData::class.java)
                        intent.putExtras(bundle)
                        view.context.startActivity(intent)
                    }
                    1 -> {
                        listener?.onDeleteData(teman, position)
                    }
                }
            }
            alert.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
        return listTeman.size
    }

    interface dataListener {
        fun onDeleteData(data: datakonco?, position: Int)
    }
}