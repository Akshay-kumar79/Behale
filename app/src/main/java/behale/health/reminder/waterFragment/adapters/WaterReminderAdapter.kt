package behale.health.reminder.waterFragment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import behale.health.reminder.database.water.WaterReminderModel
import behale.health.reminder.databinding.ListItemForWaterReminderBinding


class WaterReminderAdapter(private val clickListener: WaterReminderClickListener, private val switchClickListener: WaterReminderSwitchClickListener) : RecyclerView.Adapter<WaterReminderAdapter.WaterReminderViewHolder>() {

    private var waterReminderList: List<WaterReminderModel> = ArrayList()

    fun setData(waterReminderList: List<WaterReminderModel>){
        this.waterReminderList = waterReminderList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterReminderViewHolder {
        return WaterReminderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WaterReminderViewHolder, position: Int) {
        holder.bind(waterReminderList[position], clickListener, switchClickListener)
    }

    override fun getItemCount(): Int {
        return waterReminderList.size
    }

    class WaterReminderViewHolder private constructor(private val binding: ListItemForWaterReminderBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup) : WaterReminderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForWaterReminderBinding.inflate(inflater, parent, false)
                return WaterReminderViewHolder(binding)
            }
        }

        fun bind(waterReminder: WaterReminderModel, clickListener: WaterReminderClickListener, switchClickListener: WaterReminderSwitchClickListener){
            binding.waterReminder = waterReminder
            binding.waterReminderClickListener = clickListener
            binding.waterReminderSwitchClickListener = switchClickListener
            binding.executePendingBindings()
        }

    }
}

class WaterReminderClickListener(val clickListener: (waterReminderId: Int) -> Unit){
    fun onClick(waterReminder: WaterReminderModel) = clickListener(waterReminder.id)
}

//class WaterReminderSwitchClickListener(val switchClickListener: (waterReminderId: Int, isChecked: Boolean) -> Unit){
//    fun onCheckChanged(waterReminder: WaterReminderModel, isChecked: Boolean) = switchClickListener(waterReminder.id, isChecked)
//}

class WaterReminderSwitchClickListener(val switchClickListener: (waterReminder: Int) -> Unit){
    fun onSwitchClick(waterReminder: WaterReminderModel) = switchClickListener(waterReminder.id)
}