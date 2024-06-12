package com.example.datatemansiti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.datatemansiti.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UpdateData : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDataBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var cekNama: String = ""
    private var cekAlamat: String = ""
    private var cekNoHP: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        loadData()
        binding.update.setOnClickListener {
            cekNama = binding.newNama.text.toString()
            cekAlamat = binding.newAlamat.text.toString()
            cekNoHP = binding.newNohp.text.toString()
            if (isEmpty(cekNama) || isEmpty(cekAlamat) || isEmpty(cekNoHP)) {
                showToast("Data tidak boleh kosong")
            } else {
                val teman = datakonco(cekNama, cekAlamat, cekNoHP)
                updateTeman(teman)
            }
        }
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private fun loadData() {
        val getNama = intent.getStringExtra("dataNama")
        val getAlamat = intent.getStringExtra("dataAlamat")
        val getNoHP = intent.getStringExtra("dataNoHP")
        binding.newNama.setText(getNama)
        binding.newAlamat.setText(getAlamat)
        binding.newNohp.setText(getNoHP)
    }

    private fun updateTeman(teman: datakonco) {
        val userID = auth.uid
        val getKey = intent.getStringExtra("getPrimaryKey")
        if (userID != null && getKey != null) {
            database.child("Admin")
                .child(userID)
                .child("DataTemanSiti")
                .child(getKey)
                .setValue(teman)
                .addOnSuccessListener {
                    clearFields()
                    showToast("Data berhasil diubah")
                    finish()
                }
                .addOnFailureListener {
                    showToast("Gagal mengubah data")
                }
        }
    }

    private fun clearFields() {
        binding.newNama.text = null
        binding.newAlamat.text = null
        binding.newNohp.text = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}