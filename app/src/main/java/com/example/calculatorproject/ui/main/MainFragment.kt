package com.example.calculatorproject.ui.main

import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.calculatorproject.showAlert
import com.example.calculatorproject.showToast
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()

    private var score:Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        timer()
        viewModel.fetchRandomTransaction()
        observeRandomTransactionState()
        observeUserAnswerState()
        observeScoreState()
        listeners()
    }

    private fun timer(){
        val timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                binding.btnCheckAnswer.isEnabled = false
                binding.etAnswer.isEnabled = false
                binding.etAnswer.text?.clear()
                requireContext().showAlert("${getString(R.string.game_over)} $score")
                viewModel.gameOver()
            }
        }
        timer.start()
    }

    private fun observeScoreState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.scoreState.collect{
                    score = it
                    binding.tvScore.text = score.toString()
                }
            }
        }
    }

    private fun observeUserAnswerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userAnswer.collect{
                    if(it){
                        requireContext().showToast(R.string.result_true)
                        viewModel.fetchRandomTransaction()
                        binding.etAnswer.text?.clear()
                    }
                    else {
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
            if(binding.tvTimer.text.equals("0")) timer()
            binding.etAnswer.isEnabled = true
            viewModel.fetchRandomTransaction()
            binding.etAnswer.text?.clear()
        }
        binding.btnCheckAnswer.setOnClickListener {
            viewModel.checkUserAnswer(binding.etAnswer.text.toString())
            binding.etAnswer.text?.clear()
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