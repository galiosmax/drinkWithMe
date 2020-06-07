package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.MainFragmentBinding
import com.example.drinkwithme.handlers.IMainListener
import com.example.drinkwithme.viewModel.MainViewModel

class MainFragment : Fragment(), IMainListener, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mainViewModel: MainViewModel by viewModels()
        viewModel = mainViewModel
        // initialize binding
        onRefresh()

        val binding: MainFragmentBinding =
            MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.data = viewModel
        binding.listener = this

        val view = binding.root

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener(this)

        setHasOptionsMenu(true)

        return view
    }

    override fun onRefresh() {
        viewModel.getMostRecentDrinkResult()
            .observe(
                viewLifecycleOwner,
                Observer { drinkResult ->
                    viewModel.mPreviousDrinkResult = drinkResult
                    swipeRefreshLayout.isRefreshing = false
                })
    }

    override fun onDrink(view: View) {
        val nextFragment = DrinkFragment.newInstance()

        val bundle = Bundle()
        bundle.putBoolean("isSaving", true)
        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onAdvise(view: View) {
        val nextFragment = DrinkFragment.newInstance()

        val bundle = Bundle()
        bundle.putBoolean("isSaving", false)
        nextFragment.arguments = bundle

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onTest(view: View) {
        val nextFragment = GameFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    private fun onReset() {
        viewModel.reset()
        onRefresh()
    }

    private fun showHistory() {
        val nextFragment = HistoryFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reset_item -> {
                onReset()
                true
            }
            R.id.history_item -> {
                showHistory()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
