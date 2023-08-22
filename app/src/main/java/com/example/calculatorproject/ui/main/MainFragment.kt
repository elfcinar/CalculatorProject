package com.example.calculatorproject.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculatorproject.R
import com.example.calculatorproject.data.state.RandomTransactionState
import com.example.calculatorproject.databinding.FragmentMainBinding
import com.example.calculatorproject.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        viewModel.fetchRandomTransaction()
        observeRandomTransactionState()
        observeUserAnswerState()
        listeners()
    }

    private fun observeUserAnswerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userAnswer.collect{
                    if(it){
                        binding.tvScore.text = viewModel.score.toString()
                        requireContext().showToast(R.string.result_true)
                        viewModel.fetchRandomTransaction()
                        binding.etAnswer.text.clear()
                    }
                    else {
                        binding.tvScore.text = viewModel.score.toString()
                        requireContext().showToast(R.string.result_false)
                    }
                }
            }
        }
    }

    private fun observeRandomTransactionState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.randomTransactionState.collect{
                    when(it){
                        RandomTransactionState.Idle ->{}
                        is RandomTransactionState.Success ->{
                            binding.tvFirstNumber.text = it.firstNumber.toString()
                            binding.tvSecondNumber.text = it.secondNumber.toString()
                            binding.tvOperator.text = it.operator
                        }
                        is RandomTransactionState.Error ->{
                            requireContext().showToast(it.throwable.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun listeners() {
        binding.btnNewGame.setOnClickListener {
            viewModel.fetchRandomTransaction()
        }
        binding.btnCheckAnswer.setOnClickListener {
            viewModel.checkUserAnswer(binding.etAnswer.text.toString())
        }
        binding.etAnswer.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnCheckAnswer.isEnabled = count > 0
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}