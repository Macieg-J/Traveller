package com.example.traveller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ItemEntryBinding
import java.util.*
import java.util.function.UnaryOperator

class EntryAdapter(
    private val context: Context,
    private val editListener: (Entry) -> Unit,
    private val deleteListener: (Entry) -> Boolean
) : RecyclerView.Adapter<EntryViewHolder>() {

    private val entryList = mutableListOf<Entry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding, context).also { holder ->
            binding.root.setOnClickListener {
                editListener(entryList[holder.layoutPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entryList[position])
    }

    override fun getItemCount(): Int = entryList.size

    fun setAdapterList(listOfEntries: List<Entry>) {
        entryList.clear()
        Collections.addAll(listOfEntries.toMutableList())
    }

}