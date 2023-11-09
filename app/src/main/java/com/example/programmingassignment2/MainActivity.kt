package com.example.programmingassignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.programmingassignment2.databinding.ActivityMainBinding
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var SRApiList = mutableListOf<String>() // This is the list that will be displayed in the ListView.
    private lateinit var arrayAdapter: ArrayAdapter<String>
    val chooseCatResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {
            val category = result.data?.getStringExtra("category") // This is the category that the user selected.
            if (category != null) {
                // The "category" variable is a non-null string at this point.
                SRApiList.clear()
                retrieveQuoteWithVolley(category)
            }
        }
    }


    private fun retrieveQuoteWithVolley(category: String){
        val queue = Volley.newRequestQueue(this)
        val apiURL = "https://slime-rancher.vercel.app/api/$category"
        Log.d("url", apiURL)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, apiURL, null,
            { response ->
                try {
                    Log.d("response", response.toString())
                    val rootJSONOBJECT = JSONObject(response.toString());


                    // If the category is slimes, then we want to parse the slimes JSON array.
                    if (category == "slime") {
                        Log.d("slime", category)
                        val slimeSet = JSONArray(rootJSONOBJECT.getString("slimes"));

                        for (i in 0 until slimeSet.length()) {
                            val slimeObject =  slimeSet.getJSONObject(i)

                            val slimeName = slimeObject.getString("name")
                            val slimeDiet = slimeObject.getString("diet")
                            val slimeFavoriteFood = slimeObject.optString("favouriteFood", "N/A")

                            // `locations` is a JSONArray of strings.
                            val locationsArray = slimeObject.getJSONArray("locations")
                            val locationsList = mutableListOf<String>()

                            for (j in 0 until locationsArray.length()) {
                                locationsList.add(locationsArray.getString(j))
                            }

                            // Join the list of locations into a single string, separated by commas.
                            val slimeLocations = locationsList.joinToString(", ")

                            // You can now use slimeLocations in your string template.
                            val slimeInfo =
                                "Name: $slimeName, Diet: $slimeDiet, Favorite Food: $slimeFavoriteFood, Habitats: $slimeLocations"
                            SRApiList.add(slimeInfo)
                        }
                        arrayAdapter.notifyDataSetChanged()
                        binding.lvSR.setSelection(0)
                    // If the category is food, then we want to parse the food JSON array.
                    } else if (category == "food") {
                        Log.d("food", category)
                        val foodSet = JSONArray(rootJSONOBJECT.getString("foods"));
                        for (i in 0 until foodSet.length()) {
                            val foodObject = foodSet.getJSONObject(i)

                            val foodName = foodObject.getString("name")
                            val foodType = foodObject.getString("type")

                            val slimepediaObject = foodObject.getJSONObject("slimepedia")
                            val foodAbout = slimepediaObject.getString("about")

                            val foodInfo =
                                "Name: $foodName, Type: $foodType, About: $foodAbout"
                            SRApiList.add(SRApiList.size, foodInfo)
                        }
                        arrayAdapter.notifyDataSetChanged()
                        binding.lvSR.setSelection(0)

                    // If the category is locations, then we want to parse the locations JSON array.
                    } else if (category == "location"){
                        Log.d("location", category)
                        val locationSet = JSONArray(rootJSONOBJECT.getString("locations"));
                        for (i in 0 until locationSet.length()) {
                            val locationObject = locationSet.getJSONObject(i)

                            val locationName = locationObject.getString("name")

                            val slimepediaObject = locationObject.getJSONObject("slimepedia")
                            val locationAbout = slimepediaObject.getString("about")
                            val locationInfo =
                                "Name: $locationName, About: $locationAbout"
                            SRApiList.add(SRApiList.size, locationInfo)
                        }
                        arrayAdapter.notifyDataSetChanged()
                        binding.lvSR.setSelection(0)

                    // If the category is toys, then we want to parse the toys JSON array.
                    } else {
                        Log.d("toy", category)
                        val toySet = JSONArray(rootJSONOBJECT.getString("toys"));
                        for (i in 0 until toySet.length()) {
                            val toyObject = toySet.getJSONObject(i)

                            val toyName = toyObject.getString("name")
                            val toyDesc = toyObject.getString("description")
                            val toyInfo =
                                "Name: $toyName, Description: $toyDesc"
                            SRApiList.add(SRApiList.size, toyInfo)
                        }
                        arrayAdapter.notifyDataSetChanged()
                        binding.lvSR.setSelection(0)
                    }

                // If there is an error in parsing the JSON object, then we want to display an error message.
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Response error: " + e.printStackTrace(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Response error:", e.printStackTrace().toString())
                }

            },
            // If there is an error in getting the response, then we want to display an error message.
            {
                Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
            })
        queue.add(jsonObjectRequest)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, SRApiList)
        binding.lvSR.adapter = arrayAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        Log.d("menuinflate", "inflated")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("menu", "menu item selected")
        if (item.itemId == R.id.menu_cat) {
            chooseCatResult.launch(Intent(this, CatActivity::class.java))
        }
        return true
    }
}

