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
import retrofit2.http.GET
import retrofit2.http.Path


interface PunkService {
    @GET("{beers}")
    suspend fun listRepos(@Path("beers") user: String?): BeersResult
}

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        observerRepos()
        viewModel.retrieveRepos("beers")

    }


    fun observerRepos(){

        viewModel.repos.observe(this, { it -> showRepos(it)
        })
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
