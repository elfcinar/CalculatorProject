package com.example.calculatorproject.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.calculatorproject.R
import com.example.calculatorproject.data.state.RegisterMessageState
import com.example.calculatorproject.data.state.RegisterState
import com.example.calculatorproject.databinding.FragmentRegisterBinding
import com.example.calculatorproject.showAlert
import com.example.calculatorproject.showToast
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val viewModel: RegisterViewModel by activityViewModels()
    lateinit var binding: FragmentRegisterBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        observeUserAddState()
        observeMessage()
        listeners()
    }

    @SuppressLint("ResourceAsColor")
    fun checkMailAndPassword(confirm:String, password:String, email:String, name:String, surname:String) {
        if(email.isNotEmpty()&& surname.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()) {

                binding.btnRegister.isClickable = true
                val activeColor = ContextCompat.getColor(requireContext(), R.color.loginactive)
                binding.btnRegister.setBackgroundColor(activeColor)

        }
        else {
            binding.btnRegister.isClickable = false
            val passiveColor = ContextCompat.getColor(requireContext(), R.color.loginpassive)
            binding.btnRegister.setBackgroundColor(passiveColor)
        }

    }


    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           // checkMailAndPassword(binding.etName.text.toString(), binding.etSurname.text.toString(), binding.etEmail.text.toString(),binding.etPassword.text.toString(),binding.etConfirm.text.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            checkMailAndPassword(binding.etName.text.toString(), binding.etSurname.text.toString(), binding.etEmail.text.toString(),binding.etPassword.text.toString(),binding.etConfirm.text.toString())
        }
    }

    private fun listeners() {
        binding.etConfirm.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etName.addTextChangedListener(textWatcher)
        binding.etSurname.addTextChangedListener(textWatcher)
        binding.btnRegister.setOnClickListener{
          viewModel.insert(binding.etName.text.toString().trim(),
              binding.etSurname.text.toString().trim(),
              binding.etEmail.text.toString().trim(),
              binding.etPassword.text.toString().trim(),
              binding.etConfirm.text.toString().trim())
        }
    }


    private fun observeMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.message.collect{
                    when(it){
                        RegisterMessageState.Idle ->{}
                        RegisterMessageState.UserAlreadyExists->{
                            requireActivity().showAlert(R.string.user_already_exists)
                        }
                        RegisterMessageState.Success ->{
                            requireActivity().showToast(R.string.register_message)
                        }
                        RegisterMessageState.Empty ->{
                            requireActivity().showAlert(R.string.fill_in_the_blank)
                        }
                        RegisterMessageState.PasswordsNotEquals ->{
                            requireActivity().showAlert(R.string.not_equal)
                        }
                    }
                }
            }
        }
    }


    private fun observeUserAddState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.registerState.collect{
                    when(it){
                        RegisterState.Idle ->{}
                        RegisterState.Loading ->{

                        }
                        is RegisterState.Success ->{
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        is RegisterState.Error ->{
                            requireActivity().showAlert(R.string.somethings_wrong)
                        }
                    }
                }
            }
        }
    }

}