package com.agnext.qualixfarmer.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import com.agnext.qualixfarmer.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class utils {
    companion object{
     fun fetchAccentColor(activity: Activity): Int {
        val typedValue = TypedValue()
        val a = activity.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)

        a.recycle()
        return color
    }

        fun fetchPrimaryColor(activity: Activity): Int {
            val typedValue = TypedValue()
            val a = activity.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
            val color = a.getColor(0, 0)

            a.recycle()
            return color
        }

         fun datePickerDialog(textView: TextView,context: Context) {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpDialog = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { view, year: Int, monthOfYear: Int, dayOfMonth: Int ->


                    val fmt = SimpleDateFormat("dd/MM/yyyy")
                    val month = monthOfYear + 1
                    val date = fmt.parse("$dayOfMonth/$month/$year")

                    val fmtOut = SimpleDateFormat("dd/MM/yyyy")
                    textView.setText(fmtOut.format(date))

                }, year, month, day
            )

            var datePicker = dpDialog.datePicker
            var calendar = Calendar.getInstance()
            datePicker.maxDate = calendar.timeInMillis

            dpDialog.show()

        }
        fun getTimeFromEpoch(timestamp: Long): String {
            val itemLong = (timestamp / 1000)
            val d = Date(itemLong * 1000L)
            return SimpleDateFormat("dd/MMM/yyyy HH:mm").format(d)
        }

    }
}