package com.example.behale.dietFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withCreated
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.behale.MainActivity
import com.example.behale.R
import com.example.behale.database.diet.DietReminderModel
import com.example.behale.databinding.DietFragmentBinding
import com.example.behale.dietFragment.adapters.DietReminderAdapter
import com.example.behale.dietFragment.adapters.DietReminderClickListener
import com.example.behale.utils.ConstantUtils
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.*

class DietFragment : Fragment() {

    private lateinit var viewModel: DietViewModel
    private lateinit var binding: DietFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(findNavController().getViewModelStoreOwner(R.id.diet_nav_graph)).get(DietViewModel::class.java)
        binding = DietFragmentBinding.inflate(inflater, container, false)
        var sp = requireActivity().getSharedPreferences(ConstantUtils.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        setUpStatusBarAndToolBar()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = DietReminderAdapter(DietReminderClickListener {
            viewModel.onEditDietReminderClicked(it)
            findNavController().navigate(DietFragmentDirections.actionDietFragmentToEditDietDialog())
        })
        binding.recyclerView.adapter = adapter

        binding.addDietCardView.setOnClickListener {
            findNavController().navigate(DietFragmentDirections.actionDietFragmentToAddDietDialog())
        }

        binding.dietGoal.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter daily calorie intake")

            val input = EditText(requireContext())

            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                if(input.text.isEmpty() || input.text == null) {
                    Toast.makeText(context, "Enter some value", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                sp.edit().putInt(ConstantUtils.DAILY_BURNOUT_PREFERENCE, Integer.parseInt(input.text.toString())).apply()
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