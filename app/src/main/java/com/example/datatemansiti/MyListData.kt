package com.example.datatemansiti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datatemansiti.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class MyListData : AppCompatActivity(), RecyclerViewAdapterr.DataListener {
    private var recyclerView: RecyclerView? = null
    private var adapterr:RecyclerView.Adapter<*>? = null
    private var layoutManager:RecyclerView.LayoutManager? =  null

    val database = FirebaseDatabase.getInstance()
    private var DataTemanSiti = ArrayList<datakonco>()
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.datalist)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.title = "DataTemanSiti"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }
    private fun GetData() {
        DataTemanSiti.clear()
        Toast.makeText(applicationContext, "Tunggu dulu yaa", Toast.LENGTH_LONG).show()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser !=null) {
            val getUserID: String = currentUser.uid
            val getReference = FirebaseDatabase.getInstance().getReference()
            getReference.child("Admin").child(getUserID).child("DataTemanSiti")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataTemanSiti.clear()
                            for (snapshot in dataSnapshot.children) {
                                val teman = snapshot.getValue(datakonco::class.java)
                                teman?.key = snapshot.key
                                DataTemanSiti.add(teman!!)
                            }
                            adapterr = RecyclerViewAdapterr(DataTemanSiti, this@MyListData)
                            recyclerView?.adapter = adapterr
                            (adapterr as RecyclerViewAdapterr).notifyDataSetChanged()
                            Toast.makeText(
                                applicationContext,
                                "Data berhasil dimuat",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Data gagal dimuat", Toast.LENGTH_LONG)
                            .show()
                        Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                    }
                })
        } else {
            Toast.makeText(applicationContext, "User not sign in", Toast.LENGTH_LONG).show()
        }
    }
    private fun MyRecyclerView() {
        layoutManager =LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(dataKonco: datakonco, position: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val getUserID: String = currentUser.uid
            val getReference = FirebaseDatabase.getInstance().getReference()
            val dataSitiRef = getReference.child("Admin")
                .child(getUserID)
                .child("DataTemanSiti")
                    .child(dataKonco.key.toString())

            dataSitiRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Data dihapus",
                        Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "Gagal menghapus data: ${exception.message}",
                        Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(applicationContext, "User not sign in", Toast.LENGTH_LONG).show()
        }
    }
}