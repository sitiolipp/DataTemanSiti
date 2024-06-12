package com.example.datatemansiti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datatemansiti.databinding.ActivityMainBinding
import com.example.datatemansiti.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class MyListData : AppCompatActivity(), RecyclerViewAdapterr.dataListener {
    private var recyclerView: RecyclerView? = null
    private var adapterr: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    val database = FirebaseDatabase.getInstance()
    private var DataTemanSiti = ArrayList<datakonco>()
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)
        recyclerView = findViewById(R.id.datalist)
        supportActionBar?.title = "DataTemanSiti"
        auth = FirebaseAuth.getInstance()
        myRecyclerView()
        getData()
    }

    private fun getData() {
        Toast.makeText(applicationContext, "Tunggu bentar, sabarr", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.reference

        getReference.child("Admin").child(getUserID).child("DataTemanSiti")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DataTemanSiti.clear()
                        for (snapshot in dataSnapshot.children) {
                            val teman = snapshot.getValue(datakonco::class.java)
                            teman?.key = snapshot.key
                            if (teman != null) {
                                DataTemanSiti.add(teman)
                            }
                        }
                        adapterr = RecyclerViewAdapterr(DataTemanSiti, this@MyListData)
                        recyclerView?.adapter = adapterr
                        (adapterr as RecyclerViewAdapterr).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data berhasil dimuat", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data gagal dimuat", Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun myRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: datakonco?, position: Int) {
        val getUserID: String = auth?.currentUser?.uid.toString()
        val getReference = database.reference

        getReference.child("Admin").child(getUserID).child("DataTemanSiti").child(data?.key.toString())
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@MyListData, "Data dihapus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@MyListData, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
            }
    }
}