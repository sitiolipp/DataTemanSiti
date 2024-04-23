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
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHP: String? = null
    private lateinit var binding: ActivityUpdateDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        binding.update.setOnClickListener {
                cekNama = binding.newNama.getText().toString()
                cekAlamat = binding.newAlamat.getText().toString()
                cekNoHP = binding.newNohp.getText().toString()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHP!!)) {
                    Toast.makeText(
                        this@UpdateData, "Data jangan kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val setTeman = datakonco()
                    setTeman.nama = binding.newNama.getText().toString()
                    setTeman.alamat = binding.newAlamat.getText().toString()
                    setTeman.no_hp = binding.newNohp.getText().toString()
                    updateTeman(setTeman)
                }
            }
        }
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
    private val data: Unit
        private get() {
            val getNama = intent.getStringExtra("dataNama")
            val getAlamat = intent.getStringExtra("dataAlamat")
            val getNoHP = intent.getStringExtra("dataNoHP")
            binding.newNama.setText(getNama)
            binding.newAlamat.setText(getAlamat)
            binding.newNohp.setText(getNoHP)
        }
    private fun updateTeman(teman: datakonco) {
        val userID = auth!!.uid
        val getKey = intent.getStringExtra("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataTemanSiti")
            .child(getKey!!)
            .setValue(teman)
            .addOnSuccessListener {
                binding.newNama.setText("")
                binding.newAlamat.setText("")
                binding.newNohp.setText("")
                Toast.makeText(this@UpdateData, "Data diubah", Toast.LENGTH_SHORT).show()

                data

                finish()
            }
    }
    }
