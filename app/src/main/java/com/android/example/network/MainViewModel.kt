package com.android.example.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.punkapi.com/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var punkService = retrofit.create(PunkService::class.java)

    private var _repos = MutableLiveData<BeersResult>()
    val repos: LiveData<BeersResult>
       get() = _repos


    fun retrieveRepos(beername : String){
        CoroutineScope(Dispatchers.Main).launch {
            _repos.value= punkService.listRepos(beername)
        }
    }
}