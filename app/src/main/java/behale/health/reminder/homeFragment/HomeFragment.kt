package behale.health.reminder.homeFragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*

import androidx.core.content.ContextCompat

import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import behale.health.reminder.R
import behale.health.reminder.database.ReminderDataBase
import behale.health.reminder.databinding.HomeFragmentBinding
import kotlinx.coroutines.*

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