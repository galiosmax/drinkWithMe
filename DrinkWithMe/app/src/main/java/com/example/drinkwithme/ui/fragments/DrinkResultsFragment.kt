package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.DrinkResultsFragmentBinding
import com.example.drinkwithme.factory.DrinkResultsViewModelFactory
import com.example.drinkwithme.handlers.IDrinkResultsListener
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.viewModel.DrinkResultsViewModel

class DrinkResultsFragment : Fragment(), IDrinkResultsListener {

    companion object {
        fun newInstance() =
            DrinkResultsFragment()
    }

    private lateinit var viewModel: DrinkResultsViewModel
    private var isSaving: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val drink = arguments?.getSerializable("drink") as Drink
        isSaving = arguments?.getBoolean("isSaving") ?: false
        val volume = arguments?.getInt("volume")

        // initialize binding
        val drinkResultsViewModel: DrinkResultsViewModel by viewModels {
            DrinkResultsViewModelFactory(
                requireActivity().application, drink, volume ?: 0
            )
        }
        viewModel = drinkResultsViewModel

        viewModel.getMostRecentDrinkResult()
            .observe(
                viewLifecycleOwner,
                Observer { drinkResult ->
                    viewModel.mPreviousDrinkResult = drinkResult
                })

        val binding = DrinkResultsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.data = drinkResultsViewModel
        binding.listener = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onContinue(view: View) {
        if (isSaving) {
            viewModel.saveResult()
        }
        parentFragmentManager.popBackStack()
        parentFragmentManager.popBackStack()
        parentFragmentManager.popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }

}