package com.example.fprestu

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimulasiActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private val mModelPath = "mobile.tflite"

    private lateinit var resultText: TextView
    private lateinit var ram: EditText
    private lateinit var px_height: EditText
    private lateinit var battery_power: EditText
    private lateinit var px_width: EditText
    private lateinit var mobile_wt: EditText
    private lateinit var int_memory: EditText
    private lateinit var sc_w: EditText
    private lateinit var talk_time: EditText
    private lateinit var fc: EditText
    private lateinit var sc_h: EditText
    private lateinit var btnCheck: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulasi)

        // Initialize UI elements
        resultText = findViewById(R.id.txtResult)
        ram = findViewById(R.id.ram)
        px_height = findViewById(R.id.px_height)
        battery_power = findViewById(R.id.battery_power)
        px_width = findViewById(R.id.px_width)
        mobile_wt = findViewById(R.id.mobile_wt)
        int_memory = findViewById(R.id.int_memory)
        sc_w = findViewById(R.id.sc_w)
        talk_time = findViewById(R.id.talk_time)
        fc = findViewById(R.id.fc)
        sc_h = findViewById(R.id.sc_h)
        btnCheck = findViewById(R.id.btnCheck)

        // Set button click listener
        btnCheck.setOnClickListener {
            Log.d("SimulasiActivity", "btnCheck button clicked")

            // Validate inputs
            if (!validateInputs()) {
                resultText.text = "Harap Isi dahulu semua Pertanyaan dengan benar"
                return@setOnClickListener
            }

            // Perform inference and update UI
            val result = doInference(
                ram.text.toString(),
                px_height.text.toString(),
                battery_power.text.toString(),
                px_width.text.toString(),
                mobile_wt.text.toString(),
                int_memory.text.toString(),
                sc_w.text.toString(),
                talk_time.text.toString(),
                fc.text.toString(),
                sc_h.text.toString()
            )
            Log.d("SimulasiActivity", "Inference result: $result")

            runOnUiThread {
                resultText.text = when (result) {
                    0 -> "Spect Low Price"
                    1 -> "Spect Medium Price"
                    2 -> "Spect High Price"
                    3 -> "Spect Flagship"
                    else -> "Unknown"
                }
            }
        }

        // Initialize TensorFlow Lite interpreter
        initInterpreter()
    }

    private fun validateInputs(): Boolean {
        val inputs = listOf(
            battery_power, fc, int_memory,
            mobile_wt, px_height, px_width, ram,
            sc_h, sc_w, talk_time
        )

        return inputs.all { it.text.toString().isNotEmpty() }
    }

    private fun initInterpreter() {
        val options = Interpreter.Options().apply {
            numThreads = 4
            useNNAPI = true
        }
        interpreter = Interpreter(loadModelFile(assets, mModelPath), options)
    }

    private fun doInference(
        input1: String,
        input2: String,
        input3: String,
        input4: String,
        input5: String,
        input6: String,
        input7: String,
        input8: String,
        input9: String,
        input10: String,
    ): Int {
        val inputVal = Array(1) { FloatArray(10).apply {
            this[0] = input1.toFloat()
            this[1] = input2.toFloat()
            this[2] = input3.toFloat()
            this[3] = input4.toFloat()
            this[4] = input5.toFloat()
            this[5] = input6.toFloat()
            this[6] = input7.toFloat()
            this[7] = input8.toFloat()
            this[8] = input9.toFloat()
            this[9] = input10.toFloat()
        }}

        // Log input values
        Log.d("SimulasiActivity", "Input values: ${inputVal.contentDeepToString()}")

        val output = Array(1) { FloatArray(4) }
        interpreter.run(inputVal, output)

        // Log output values
        Log.d("SimulasiActivity", "Output values: ${output.contentDeepToString()}")

        // Extract the first row from the 2D output array
        val outputRow = output[0]

        // Get the index of the maximum value in the output row
        var maxIndex = 0
        var maxValue = outputRow[0]
        for (i in 1 until outputRow.size) {
            if (outputRow[i] > maxValue) {
                maxValue = outputRow[i]
                maxIndex = i
            }
        }
        return maxIndex
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
