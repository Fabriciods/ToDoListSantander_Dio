package com.example.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private var locale  = Locale("pt","BR")

fun Date.format(): String{
    return SimpleDateFormat("dd/MM/yyyy").format(this)
}
var TextInputLayout.text : String
    get() = editText?.text?.toString() ?:""
    set(value){
        editText?.setText(value)
    }