package com.example.datatemansiti

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datatemansiti.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.save -> {
                // Implement save logic here
                val getUserID = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNo_hp: String =  binding.nohp.getText().toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNo_hp)) {
                    Toast.makeText(this@MainActivity, "JANGAN ADA YANG KOSONG", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("DataTemanSiti").push()
                        .setValue(datakonco(getNama, getAlamat, getNo_hp))
                        .addOnCompleteListener(this) {
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.nohp.setText("")
                            Toast.makeText(this@MainActivity, "DATA TERSIMPAN",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainActivity, "Logout berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(applicationContext, Login::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@MainActivity, "Logout gagal",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
            R.id.show_data -> {

            }
        }
    }
}