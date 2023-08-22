package com.example.calculatorproject.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.calculatorproject.Constants.REMEMBER_ME_KEY
import com.example.calculatorproject.Constants.SHARED_PREF_NAME
import com.example.calculatorproject.R
import com.example.calculatorproject.data.state.LoginState
import com.example.calculatorproject.databinding.FragmentLoginBinding
import com.example.calculatorproject.showToast
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel:LoginViewModel by activityViewModels()
    lateinit var binding: FragmentLoginBinding

    var rememberMe: Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        listeners()
        observeLogin()
        rememberMeControl()

        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                checkMailAndPassword(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
            }

        })

        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                checkMailAndPassword(binding.editTextPassword.text.toString(), binding.editTextPassword.text.toString())
            }

        })
    }

    @SuppressLint("ResourceAsColor")
    fun checkMailAndPassword(email:String, password:String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            binding.btnLogin.isClickable = false
            val passiveColor = ContextCompat.getColor(requireContext(), R.color.loginpassive)
            binding.btnLogin.setBackgroundColor(passiveColor)
        } else {
            binding.btnLogin.isClickable = true
            val activeColor = ContextCompat.getColor(requireContext(), R.color.loginactive)
            binding.btnLogin.setBackgroundColor(activeColor)
        }
    }

    private fun rememberMeControl() {
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        rememberMe = sharedPreferences.getBoolean(REMEMBER_ME_KEY, false)
        if (rememberMe) {
           findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    private fun observeLogin() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.loginState.collect {
                    when (it) {
                        is LoginState.Idle -> {}
                        is LoginState.Loading -> {}
                        is LoginState.Result -> {
                            sharedPreferences.edit().putBoolean(REMEMBER_ME_KEY,it.rememberMe).apply()
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                        is LoginState.Error -> {
                           activity?.showToast(getString(R.string.upps_something_went_wrong))
                        }
                    }
                }
            }
        }
    }

    private fun listeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.editTextEmail.text.toString().trim(),
                binding.editTextPassword.text.toString().trim(),
                rememberMe

            )

        }

        binding.rememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            rememberMe = isChecked
        }
    }

}