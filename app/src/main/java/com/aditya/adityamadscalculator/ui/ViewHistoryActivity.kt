package com.aditya.adityamadscalculator.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adityamadscalculator.R
import com.aditya.adityamadscalculator.adapter.HistoryAdapter
import com.aditya.adityamadscalculator.models.HistoryData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewHistoryActivity : AppCompatActivity() {

    var historyDataList: ArrayList<HistoryData>? = null
    var firebaseAuth: FirebaseAuth? = null
    var recyclerView: RecyclerView? = null
    var historyData: HistoryData? = null
    var progressDialog: ProgressDialog? = null
    private var databaseReference: DatabaseReference? = null
    var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)
        firebaseAuth = FirebaseAuth.getInstance()
        historyDataList = ArrayList()
        recyclerView = findViewById(R.id.recyclerviewHistoryData)
        progressDialog = ProgressDialog(this@ViewHistoryActivity)
        databaseReference = FirebaseDatabase.getInstance().getReference("History").child(
            firebaseAuth!!.uid!!
        )
        val gridLayoutManager = GridLayoutManager(this@ViewHistoryActivity, 1)
        recyclerView!!.layoutManager = gridLayoutManager
        val historyAdapter = HistoryAdapter(historyDataList!!, this@ViewHistoryActivity)
        recyclerView!!.adapter = historyAdapter
        progressDialog!!.setMessage("Wait..")
        progressDialog!!.show()

        valueEventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    historyData = itemSnapshot.getValue(HistoryData::class.java)
                    historyDataList!!.add(historyData!!)
                    if (historyData == null) {
                        Toast.makeText(this@ViewHistoryActivity, "No History", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerView!!.adapter = historyAdapter
                historyAdapter.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewHistoryActivity, "Cancelled : " + error.message, Toast.LENGTH_SHORT)
                    .show()
                progressDialog!!.dismiss()
            }
        })
    }
}