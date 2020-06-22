package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.drinkwithme.viewModel.DrinkDetailsViewModel
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.DrinkDetailsFragmentBinding
import com.example.drinkwithme.factory.DrinkDetailsViewModelFactory
import com.example.drinkwithme.model.Drink
import com.example.drinkwithme.handlers.IDrinkDetailsListener
import com.example.drinkwithme.main.MainActivity

class DrinkDetailsFragment : Fragment(), IDrinkDetailsListener {

    companion object {
        fun newInstance() =
            DrinkDetailsFragment()
    }

    private lateinit var viewModel: DrinkDetailsViewModel
    private lateinit var drink: Drink

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drink = arguments?.getSerializable("drink") as Drink

        // initialize binding
        val drinkDetailsViewModel: DrinkDetailsViewModel by viewModels {
            DrinkDetailsViewModelFactory(
                drink
            )
        }
        viewModel = drinkDetailsViewModel
        val binding: DrinkDetailsFragmentBinding =
            DrinkDetailsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.data = drinkDetailsViewModel
        binding.listener = this

        val view = binding.root

        // initialize image
        val imageView = view.findViewById<ImageView>(R.id.drinkImage_view)
        Glide.with(this).load(drink.drinkImageUrl).into(imageView)

        setHasOptionsMenu(true)

        return view
    }

    override fun onDrink(view: View) {
        val nextFragment = DrinkResultsFragment.newInstance()

        val bundle = Bundle()
        bundle.putSerializable("drink", drink)
        bundle.putBoolean("isSaving", arguments?.getBoolean("isSaving") ?: false)

        var volume = 0
        if (viewModel.mVolume.get() != null && viewModel.mVolume.get()!!.isNotBlank()) {
            volume = viewModel.mVolume.get()!!.toInt()
        }
        bundle.putInt("volume", volume)

        nextFragment.arguments = bundle
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, nextFragment).addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }

}
