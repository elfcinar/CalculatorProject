package com.example.calculatorproject.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.calculatorproject.Constants
import com.example.calculatorproject.Constants.LOGGED_USER_ID
import com.example.calculatorproject.Constants.SHARED_PREF_NAME
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

    private fun listeners() {
        binding.btnRegister.setOnClickListener {
            viewModel.insert(binding.etName.text.toString().trim(), binding.etSurname.text.toString().trim(), binding.etEmail.text.toString().trim(),binding.etPassword.text.toString().trim(),binding.etPasswordConfirm.text.toString().trim())

        }
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
                viewModel.userAddState.collect{
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