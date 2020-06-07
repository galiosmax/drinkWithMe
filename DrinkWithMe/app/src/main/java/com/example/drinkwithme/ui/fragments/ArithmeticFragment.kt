package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.ArithmeticFragmentBinding
import com.example.drinkwithme.factory.ArithmeticViewModelFactory
import com.example.drinkwithme.handlers.IArithmeticListener
import com.example.drinkwithme.viewModel.ArithmeticViewModel

class ArithmeticFragment : Fragment(), IArithmeticListener {

    companion object {
        fun newInstance() =
            ArithmeticFragment()
    }

    private lateinit var viewModel: ArithmeticViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arithmeticViewModel: ArithmeticViewModel by viewModels {
            ArithmeticViewModelFactory(
                requireActivity().application
            ) {
                parentFragmentManager.popBackStack()
                parentFragmentManager.popBackStack()
            }
        }
        viewModel = arithmeticViewModel

        val binding: ArithmeticFragmentBinding =
            ArithmeticFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.data = viewModel
        binding.listener = this

        return binding.root
    }

    override fun onClickOptionA(view: View) {
        viewModel.onClickedOption(ArithmeticViewModel.Option.A)
    }

    override fun onClickOptionB(view: View) {
        viewModel.onClickedOption(ArithmeticViewModel.Option.B)
    }

    override fun onClickOptionC(view: View) {
        viewModel.onClickedOption(ArithmeticViewModel.Option.C)
    }

    override fun onClickOptionD(view: View) {
        viewModel.onClickedOption(ArithmeticViewModel.Option.D)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.default_menu, menu)
    }

}
