package com.rafiul.photoalbumwithcache.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    positiveAction: (() -> Unit)? = null,
    negativeButtonText: String? = null,
    negativeAction: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText) { _, _ ->
            positiveAction?.invoke()
        }
        negativeButtonText?.let {
            setNegativeButton(it) { _, _ ->
                negativeAction?.invoke()
            }
        }
    }.create().show()
}


fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}






