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
import com.example.calculatorproject.R
import com.example.calculatorproject.data.state.RegisterMessageState
import com.example.calculatorproject.data.state.RegisterState
import com.example.calculatorproject.databinding.FragmentRegisterBinding
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
            if (confirm != password) {
                binding.btnRegister.isClickable = false
                val passiveColor = ContextCompat.getColor(requireContext(), R.color.loginpassive)
                binding.btnRegister.setBackgroundColor(passiveColor)
            } else{
                binding.btnRegister.isClickable = true
                val activeColor = ContextCompat.getColor(requireContext(), R.color.loginactive)
                binding.btnRegister.setBackgroundColor(activeColor)
            }
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
            checkMailAndPassword(binding.etName.text.toString(), binding.etSurname.text.toString(), binding.etEmail.text.toString(),binding.etPassword.text.toString(),binding.etPasswordConfirm.text.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            //checkMailAndPassword(binding.etName.text.toString(), binding.etSurname.text.toString(), binding.etEmail.text.toString(),binding.etPassword.text.toString(),binding.etPasswordConfirm.text.toString())
        }
    }

    private fun listeners() {
        binding.etPasswordConfirm.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etName.addTextChangedListener(textWatcher)
        binding.etSurname.addTextChangedListener(textWatcher)
    }


    private fun observeMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.message.collect{
                    when(it){
                        RegisterMessageState.Idle ->{}
                        RegisterMessageState.UserAlreadyExists->{
                            AlertDialog.Builder(requireContext()).setMessage("user_already_exists").create().show()
                        }
                        RegisterMessageState.Success ->{
                            AlertDialog.Builder(requireContext()).setMessage("login_success").create().show()
                        }
                        RegisterMessageState.Empty ->{
                            AlertDialog.Builder(requireContext()).setMessage("fill_in_the_blank").create().show()
                        }
                        RegisterMessageState.PasswordsNotEquals ->{
                            AlertDialog.Builder(requireContext()).setMessage("passwords_not_equals").create().show()
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
                            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                            activity?.showToast("oldu")
                        }
                        is RegisterState.Error ->{
                            AlertDialog.Builder(requireContext()).setTitle("somethings_wrong").setMessage(it.throwable.message)
                        }
                    }
                }
            }
        }
    }

}