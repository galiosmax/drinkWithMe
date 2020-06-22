package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkwithme.R
import com.example.drinkwithme.model.DrinkResult
import com.example.drinkwithme.ui.adapters.HistoryListAdapter
import com.example.drinkwithme.viewModel.HistoryViewModel

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() =
            HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: HistoryListAdapter
    private lateinit var liveData: LiveData<List<DrinkResult>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.history_fragment, container, false)

        val historyViewModel: HistoryViewModel by viewModels()
        viewModel = historyViewModel

        mAdapter = HistoryListAdapter(requireContext())

        mRecyclerView = view.findViewById(R.id.history_recyclerview)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        setHasOptionsMenu(true)

        liveData = viewModel.getAllDrinkResults()
        liveData.observe(viewLifecycleOwner, Observer { drinkResults ->
            mAdapter.mDrinkResultList = drinkResults
        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }

}