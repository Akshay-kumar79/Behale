package com.example.behale.homeFragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*

import androidx.core.content.ContextCompat
import com.example.behale.R
import com.example.behale.databinding.HomeFragmentBinding

import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.behale.database.ReminderDataBase
import com.example.behale.waterFragment.WaterViewModel
import com.example.behale.waterFragment.WaterViewModelFactory
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = HomeFragmentBinding.inflate(inflater, container, false)

        val application: Application = requireNotNull(this.activity).application
        val waterHistoryDatasource = ReminderDataBase.getInstance(application).waterHistoryDao
        val viewModelFactory = HomeViewModelFactory(waterHistoryDatasource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpStatusBar()

        binding.waterCardViewLayout.setOnClickListener {
            lifecycleScope.launch {
                delay(32)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWaterFragment())
                }
            }
        }

        binding.dietCardViewLayout.setOnClickListener {
            lifecycleScope.launch {
                delay(32)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDietFragment())
                }
            }
        }

        binding.scheduleButtonOfDiet.setOnClickListener {
            lifecycleScope.launch {
                delay(32)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDietFragment())
                }
            }
        }

        binding.stepsCardViewLayout.setOnClickListener {
            lifecycleScope.launch {
                delay(32)
                withContext(Dispatchers.Main) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStepsFragment())
                }
            }
        }

        binding.pillsCardViewLayout.setOnClickListener{
            lifecycleScope.launch{
                delay(32)
                withContext(Dispatchers.Main){
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPillsFragment())
                }
            }
        }

        binding.scheduleButtonOfPills.setOnClickListener {
            lifecycleScope.launch{
                delay(32)
                withContext(Dispatchers.Main){
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPillsFragment())
                }
            }
        }

        return binding.root
    }

    private fun setUpStatusBar() {
        val window: Window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        )

        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.homeStatusBarColor)
    }

}