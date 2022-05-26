package com.android.example.network

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.retrofit.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface PunkService {
    @GET("{beers}")
    suspend fun listRepos(@Path("beers") user: String?): BeersResult
}

class MainActivity : AppCompatActivity() {
    var retrofit = Retrofit.Builder()
        .baseUrl("https://api.punkapi.com/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var punkService = retrofit.create(PunkService::class.java)
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        retrieveRepos()
    }


    fun retrieveRepos(){
        lifecycleScope.launch{
            try{
                val repos = punkService.listRepos("beers")
                showRepos(repos)
            } catch (e: Exception){
                Log.e("MainActivity", "Error retrieving repos: $e")
                Snackbar.make(findViewById(R.id.main_view), "Error retrieving repos",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry"){ retrieveRepos() }.show()
            }
        }
    }

    fun showRepos(repoResults: BeersResult){
        Log.d("MainActivity", "list of repos received, size: ${repoResults.size}")
        val list = findViewById<RecyclerView>(R.id.recycle_main)
        list.visibility = View.VISIBLE
        val adapter = RepoAdapter(repoResults)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
        }

    }
