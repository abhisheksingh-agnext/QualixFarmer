package com.agnext.qualixfarmer.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.zxing.WriterException


object AlertUtil {

    fun showToast(context: Context, msg: CharSequence) {
        showCustomToast(context, msg, Toast.LENGTH_LONG)
    }

    fun showCustomToast(context: Context, msg: CharSequence, duration: Int) {
        val toast = Toast.makeText(context, msg, duration)
        val rootView = toast.view

        val color = ContextCompat.getColor(context, android.R.color.darker_gray)
        rootView.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        val textView = rootView.findViewById<TextView>(android.R.id.message)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))

        toast.show()
    }

    fun showErrorDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle("$title")
            .setMessage("$message")

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.ok, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    fun showDialogSingleMessage(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("No Devices Found")
            .setMessage("There are no unallocated devices present at the moment. Please try later")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.cancel()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    fun imageDialog(context: Context, imageBitmap: Bitmap) {


        var builder = Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        builder.setOnDismissListener {
        }


        var imageView = ImageView(context);
        imageView.setImageBitmap(imageBitmap)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        );
        builder.show();

    }

    /**Generate the QR and display*/
    fun generateQR(input: String, context: Context): Bitmap {
        var bitmap: Bitmap? = null

        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4
        val qrgEncoder = QRGEncoder(input, null, QRGContents.Type.TEXT, smallerDimension)

        try {
            bitmap = qrgEncoder.encodeAsBitmap()
            // ivQR.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.v("Error", e.toString())
        }

        return bitmap!!

    }

}// end upLoad2Server
