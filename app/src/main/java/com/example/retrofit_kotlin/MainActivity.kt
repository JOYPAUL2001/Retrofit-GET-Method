package com.example.retrofit_kotlin

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import okio.IOException

const val TAG = "Main Activity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var todoadapter: TodoAdapter
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        lifecycleScope.launch {
            binding.progress.isVisible = true
            val response = try{
                RetrofitInstance.api.getTodo()
            }catch (e: IOException){
                Log.e(TAG,"IOException: you might not have internet connection")
                binding.progress.isVisible=false
                return@launch
            }catch (e: HttpException){
                Log.e(TAG,"HttpException: unexpected response")
                binding.progress.isVisible=false
                return@launch
            }

            if (response.isSuccessful && response.body()!= null){
                todoadapter.todos = response.body()!!
            }else{
                Log.e(TAG,"Respponse is not coming")
            }
            binding.progress.isVisible=false
        }

    }

    private fun setupRecyclerView() = binding.rvtodos.apply {
        todoadapter = TodoAdapter()
        adapter = todoadapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}