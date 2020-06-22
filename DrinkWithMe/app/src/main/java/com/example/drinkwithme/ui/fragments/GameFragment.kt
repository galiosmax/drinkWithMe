package com.example.drinkwithme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drinkwithme.R
import com.example.drinkwithme.databinding.GameFragmentBinding
import com.example.drinkwithme.handlers.IGameListener

class GameFragment : Fragment(), IGameListener {

    companion object {
        fun newInstance() = GameFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: GameFragmentBinding =
            GameFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.listener = this

        return binding.root
    }

    override fun onCircles(view: View) {
        val nextFragment = CircleGameFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onArithmetic(view: View) {
        val nextFragment = ArithmeticFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

    override fun onHistory(view: View) {
        val nextFragment = GameHistoryFragment.newInstance()

        parentFragmentManager.beginTransaction().replace(R.id.container, nextFragment)
            .addToBackStack(null).commit()
    }

}