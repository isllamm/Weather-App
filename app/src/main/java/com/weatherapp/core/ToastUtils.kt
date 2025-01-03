package com.weatherapp.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast

class ToastUtils{

    companion object{
        private var toastMessage: Toast? = null
        @SuppressLint("ShowToast", "InflateParams")
        fun showToast(context: Context, msg: String){
            Log.d("isllam", msg)
            if (msg != "null") {
                if (toastMessage != null) {
                    toastMessage!!.cancel()
                }
                toastMessage = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                toastMessage!!.setText(msg)
                toastMessage!!.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
                toastMessage!!.duration = Toast.LENGTH_SHORT
                toastMessage!!.show()
            }
        }
    }

}