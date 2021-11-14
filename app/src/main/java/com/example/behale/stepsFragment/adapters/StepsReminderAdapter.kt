package com.example.behale.stepsFragment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.behale.database.steps.StepsReminderModel
import com.example.behale.databinding.ListItemForStepsReminderBinding

class StepsReminderAdapter(private val stepsReminderList: List<StepsReminderModel>) : RecyclerView.Adapter<StepsReminderAdapter.StepsReminderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsReminderViewHolder {
        return StepsReminderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepsReminderViewHolder, position: Int) {
        holder.bind(stepsReminderList[position])
    }

    override fun getItemCount(): Int {
        return stepsReminderList.size
    }

    class StepsReminderViewHolder private constructor(val binding: ListItemForStepsReminderBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): StepsReminderViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForStepsReminderBinding.inflate(inflater, parent, false)
                return StepsReminderViewHolder(binding)
            }
        }

        fun bind(stepsReminder: StepsReminderModel){
            binding.timeTextView.text = stepsReminder.time
        }
    }


}