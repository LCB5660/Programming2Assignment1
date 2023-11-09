package com.example.programmingassignment2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.programmingassignment2.databinding.ActivityCatBinding


class CatActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityCatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.sprCat // This is the spinner that the user will use to select a category.
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
    // This function is called when the user selects an item in the spinner.
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val catValue = parent.getItemAtPosition(position)
            binding.btnShow.setOnClickListener{
                val intent = Intent(this, MainActivity :: class.java)
                intent.putExtra("category", catValue.toString())
                Log.d("catValue", catValue.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}