package com.example.traveller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.settings.PreferencesModel
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ItemEntryBinding
import java.util.function.Consumer

class EntryAdapter(
    private val context: Context,
    private val displayDetailsListener: (Entry) -> Unit,
    private val preferencesModel: PreferencesModel
//    private val deleteListener: (Entry) -> Boolean
) : RecyclerView.Adapter<EntryViewHolder>() {

    private val entryList = mutableListOf<Entry>()
    lateinit var filledListOfEntries: List<Entry>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding, context, preferencesModel).also { holder ->
            binding.root.setOnClickListener {
                displayDetailsListener(entryList[holder.layoutPosition])
            }

//            binding.root.setOnLongClickListener { _ ->
//                deleteListener(entryList[holder.layoutPosition])
//            }
        }
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        if (entryList.size != 0) {
            holder.bind(entryList[position])
        }
    }

    override fun getItemCount(): Int = entryList.size

    fun setAdapterList(listOfEntries: List<Entry>) {
        entryList.clear()
        listOfEntries.toMutableList().forEach(Consumer { entry ->
            if (entry.id != "Entry_ed8f6d86-03ba-45e8-80f3-6d3108a311cd")
            entryList.add(entry)
        })
        notifyDataSetChanged()
        filledListOfEntries = ArrayList(entryList)
    }

    fun removeItem(entry: Entry){
        entryList.remove(entry)
        notifyDataSetChanged()
    }

}