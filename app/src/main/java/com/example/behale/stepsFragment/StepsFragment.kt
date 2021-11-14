package com.example.behale.stepsFragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.behale.MainActivity
import com.example.behale.R
import com.example.behale.database.steps.StepsReminderModel
import com.example.behale.databinding.StepsFragmentBinding
import com.example.behale.stepsFragment.adapters.StepsReminderAdapter
import com.example.behale.utils.ConstantUtils
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StepsFragment : Fragment() {

    private lateinit var viewModel: StepsViewModel
    private lateinit var binding: StepsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = StepsFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(StepsViewModel::class.java)
        var sp = requireActivity().getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpStatusBarAndToolBar()

        binding.stepGoal.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter daily steps goal")

            val input = EditText(requireContext())

            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                if(input.text.isEmpty() || input.text == null) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                sp.edit().putInt(ConstantUtils.STEPS_GOAL_PREFERENCE, Integer.parseInt(input.text.toString())).apply()
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

            builder.show()
        }

        return binding.root
    }

    private fun setUpStatusBarAndToolBar() {
        val window: Window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        )

        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.categoryStatusBarColor)

        binding.toolBar.setupWithNavController(
            findNavController(),
            AppBarConfiguration(findNavController().graph)
        )
        (activity as MainActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}