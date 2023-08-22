package com.example.calculatorproject

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message:Int){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.showAlert(message:String){
    AlertDialog.Builder(this).setMessage(message).create().show()

fun Context.showAlert(message:Int){
    AlertDialog.Builder(this).setMessage(message).create().show()
}

fun Context.showSnackBar(view: View, message:String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}