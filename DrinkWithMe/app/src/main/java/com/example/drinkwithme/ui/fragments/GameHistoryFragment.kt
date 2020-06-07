package com.example.drinkwithme.ui.fragments

import com.example.drinkwithme.ui.adapters.GameHistoryListAdapter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkwithme.R
import com.example.drinkwithme.model.TestResult
import com.example.drinkwithme.viewModel.GameHistoryViewModel

class GameHistoryFragment : Fragment() {

    companion object {
        fun newInstance() =
            GameHistoryFragment()
    }

    private lateinit var viewModel: GameHistoryViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: GameHistoryListAdapter
    private lateinit var liveData: LiveData<List<TestResult>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.game_history_fragment, container, false)

        val gameHistoryViewModel: GameHistoryViewModel by viewModels()
        viewModel = gameHistoryViewModel

        mAdapter = GameHistoryListAdapter(requireContext())

        mRecyclerView = view.findViewById(R.id.gameHistory_recyclerview)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        setHasOptionsMenu(true)

        liveData = viewModel.getAllTestResults()
        liveData.observe(viewLifecycleOwner, Observer { testResults ->
            mAdapter.mTestResultList = testResults
        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }
}