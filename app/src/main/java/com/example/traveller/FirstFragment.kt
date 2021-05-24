package com.example.traveller

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveller.adapter.EntryAdapter
import com.example.traveller.database.Entry
import com.example.traveller.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentFirstDisplayPhotosButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setAdapter(listOfEntries: List<Entry>, context: Context){
        val entryAdapter = EntryAdapter(context, this::onEditAction, this::onRemoveAction)
        entryAdapter.setAdapterList(listOfEntries)
        binding.fragmentFirstEntryListRecyclerView.apply {
            adapter = entryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun onEditAction(model: Entry) {

    }

    private fun onRemoveAction(model: Entry) : Boolean {
        return false
    }
}