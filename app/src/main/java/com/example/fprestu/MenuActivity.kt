package com.example.fprestu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnTentang = findViewById<Button>(R.id.btnTentang)
        btnTentang.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        val btnDataset = findViewById<Button>(R.id.btnDataset)
        btnDataset.setOnClickListener {
            val intent = Intent(this, DatasetActivity::class.java)
            startActivity(intent)
        }

        val btnFitur = findViewById<Button>(R.id.btnFitur)
        btnFitur.setOnClickListener {
            val intent = Intent(this, FiturActivity::class.java)
            startActivity(intent)
        }

        val btnModel = findViewById<Button>(R.id.btnModel)
        btnModel.setOnClickListener {
            val intent = Intent(this, ModelActivity::class.java)
            startActivity(intent)
        }

        val btnSimulasi = findViewById<Button>(R.id.btnSimulasi)
        btnSimulasi.setOnClickListener {
            val intent = Intent(this, SimulasiActivity::class.java)
            startActivity(intent)
        }
    }
}