package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkwithme.R
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.ui.adapters.DrinkListAdapter
import com.example.drinkwithme.viewModel.DrinkViewModel

const val ALCOHOLIC = "Alcoholic"

class DrinkFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = DrinkFragment()
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: DrinkListAdapter
    private lateinit var viewModel: DrinkViewModel
    private lateinit var liveData: LiveData<List<Drink>>

    private var currentFilter = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.drink_fragment, container, false)

        val drinkViewModel: DrinkViewModel by viewModels()
        viewModel = drinkViewModel

        mAdapter = DrinkListAdapter(requireContext())

        mRecyclerView = view.findViewById(R.id.drink_recyclerview)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdapter.isSaving = arguments?.getBoolean("isSaving") ?: false

        setHasOptionsMenu(true)

        liveData = viewModel.getAllDrinks()
        liveData.observe(viewLifecycleOwner, Observer { drinks ->
            mAdapter.mDrinkListCopy = drinks
            mAdapter.filter(currentFilter)
        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = "Search for a drink"
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        currentFilter = newText ?: ""
        liveData = viewModel.getFilteredDrinks(newText ?: "")
        return mAdapter.mDrinkList.isNotEmpty()
    }

}
