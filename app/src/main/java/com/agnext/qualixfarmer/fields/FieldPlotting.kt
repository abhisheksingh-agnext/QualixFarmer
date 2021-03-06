package com.agnext.qualixfarmer.fields

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.agnext.qualixfarmer.base.Constant
import com.gisnext.lib.ui.boundary.MarkBoundaryActivity
import com.gisnext.lib.ui.boundary.TrackBoundaryActivity
import com.gisnext.lib.util.Constants

open class FieldPlotting {

    /**Boundary Marking*/
    open fun showBoundaryOptionDialog(activity: Activity, accentColor: Int) {
        val options = arrayOf<CharSequence>("Geo Plotting", "Geo Fencing")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Option")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            when {
                options[item] == "Geo Plotting" -> {
                    dialog.dismiss()
                    intentGeoPlotting(activity, accentColor)
                }
                options[item] == "Geo Fencing" -> {
                    dialog.dismiss()
                    intentGeoFencing(activity, accentColor)
                }
//                options[item] == "Cancel" -> dialog.dismiss()
            }
        })
        builder.show()
    }

    /**Intent Geo Plotting*/
    private fun intentGeoPlotting(activity: Activity, accentColor: Int) {
        val markIntent = Intent(activity, MarkBoundaryActivity::class.java)
        markIntent.putExtra(Constants.KEY_COLOR, accentColor) // Optional
        activity.startActivityForResult(markIntent, Constant.REQUEST_BOUNDARY_GEO_PLOTING)
    }

    /**Intent Geo Fencing*/
    private fun intentGeoFencing(activity: Activity, accentColor: Int) {
        val trackIntent = Intent(activity, TrackBoundaryActivity::class.java)
        trackIntent.putExtra(Constants.KEY_COLOR, accentColor) // Optional
        activity.startActivityForResult(trackIntent, Constant.REQUEST_BOUNDARY_GEO_FENCING)
    }

}