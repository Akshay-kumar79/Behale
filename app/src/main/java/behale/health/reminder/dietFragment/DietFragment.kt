package behale.health.reminder.dietFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import behale.health.reminder.MainActivity
import behale.health.reminder.R
import behale.health.reminder.databinding.DietFragmentBinding
import behale.health.reminder.dietFragment.adapters.DietReminderAdapter
import behale.health.reminder.dietFragment.adapters.DietReminderClickListener
import behale.health.reminder.utils.ConstantUtils
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

            input.filters = arrayOf(InputFilter.LengthFilter(5))
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