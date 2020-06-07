package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.PersonalDataFragmentBinding
import com.example.drinkwithme.handlers.IPersonalDataListener
import com.example.drinkwithme.viewModel.PersonalDataViewModel

class PersonalDataFragment : Fragment(), IPersonalDataListener {

    companion object {
        fun newInstance() =
            PersonalDataFragment()
    }

    lateinit var viewModel: PersonalDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val personalDataViewModel: PersonalDataViewModel by viewModels()
        viewModel = personalDataViewModel

        // initialize binding
        val binding: PersonalDataFragmentBinding =
            PersonalDataFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.data = viewModel
        binding.listener = this

        val view = binding.root

        // initialize spinner
        val spinner = view.findViewById<Spinner>(R.id.gender_spinner)
        spinner.onItemSelectedListener = viewModel
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            activity?.baseContext!!,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        setHasOptionsMenu(true)

        return view
    }

    override fun onDone(view: View) {
        viewModel.onDone()

        val nextFragment = MainFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }
}
