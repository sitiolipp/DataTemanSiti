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

class RecyclerViewAdapterr (private val DataTemanSiti: ArrayList<datakonco>,
                            private val myListDataActivity: MyListData
) : RecyclerView.Adapter<RecyclerViewAdapterr.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Nama: TextView = itemView.findViewById(R.id.nama1)
        val Alamat: TextView = itemView.findViewById(R.id.alamat1)
        val NoHP: TextView = itemView.findViewById(R.id.nohp1)
        val ListItem: LinearLayout = itemView.findViewById(R.id.list_item)

        init {
            ListItem.setOnLongClickListener {
                val action = arrayOf("Update", "Delete")
                val alert = AlertDialog.Builder(itemView.context)
                alert.setItems(action) { dialog, i ->
                    when (i) {
                        0 -> {
                            val currentData = DataTemanSiti[adapterPosition]
                            val bundle = Bundle().apply {
                                putString("dataNama", currentData.nama)
                                putString("dataAlamat", currentData.alamat)
                                putString("dataNoHP", currentData.no_hp)
                                putString("getPrimaryKey", currentData.key)
                            }
                            val intent = Intent(itemView.context, UpdateData::class.java)
                                .apply {
                                    putExtras(bundle)
                                }
                            itemView.context.startActivity(intent)
                        }
                        1 -> {
                            listener?.onDeleteData(DataTemanSiti[adapterPosition], adapterPosition)
                        }
                    }
                }
                alert.create().show()
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from((parent.context)).inflate(
            R.layout.view_design, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = DataTemanSiti[position]
        holder.Nama.text = "Nama : ${currentData.nama}"
        holder.Alamat.text = "Alamat : ${currentData.alamat}"
        holder.NoHP.text = "No hp : ${currentData.no_hp}"
    }

    override fun getItemCount(): Int {
        return DataTemanSiti.size
    }
    interface DataListener {
        fun onDeleteData(dataKonco: datakonco, position: Int)
    }
    var listener: DataListener? = null

    init {
        this.listener = myListDataActivity
    }
}
