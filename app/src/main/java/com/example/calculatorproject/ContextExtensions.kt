package com.example.calculatorproject

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.showSnackBar(view: View, message:String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}