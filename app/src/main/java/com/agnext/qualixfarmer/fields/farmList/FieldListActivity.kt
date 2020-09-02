package com.agnext.qualixfarmer.fields.farmList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_FARM
import com.agnext.qualixfarmer.fields.addField.AddFarmActivity
import com.agnext.qualixfarmer.fields.farmDetail.FarmDetailActivity
import com.agnext.qualixfarmer.fields.updateField.UpdateFarmActivity
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_field_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class FieldListActivity : BaseActivity(), View.OnClickListener, FarmListCallback {


    var viewModel: FieldListViewModel? = null
    var fieldListAdapter: FieldListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_list)
        initView()
    }

    fun initView() {

        //setting toolbar
        toolbar.title = getString(R.string.farmer_field)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        //VM
        viewModel = ViewModelProvider(
            this, FieldListViewModelFactory(
                FieldInteractor()
            )
        )[FieldListViewModel::class.java]
        viewModel!!.fieldListState.observe(::getLifecycle, ::updateUI)

        //click register
        fbAdd.setOnClickListener(this)
        fbAdd.visibility = View.VISIBLE


        progress.visibility = View.VISIBLE
        //action
        viewModel!!.getAllFarmVM(userToken(this))

    }

    private fun updateUI(screenState: ScreenState<FieldListState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> displayData(screenState.renderState)
        }
    }

    private fun displayData(renderState: FieldListState) {
        progress.visibility = View.GONE
        when (renderState) {
            FieldListState.FarmListFailure -> {
                AlertUtil.showToast(this, "Error to get farm")
                tvNoData.visibility = View.VISIBLE
            }

            FieldListState.FarmListSuccess -> {
                tvNoData.visibility = View.GONE
                updateList()
            }

            FieldListState.FarmNoRecord -> {
                tvNoData.visibility = View.VISIBLE
                AlertUtil.showToast(this, "No farm")
            }

            FieldListState.FarmDeleteFailure -> {
                AlertUtil.showToast(this, "Unable to delete the farm")
            }
            FieldListState.FarmDeleteSuccess -> {
                viewModel!!.getAllFarmVM(userToken(this))
            }

        }
    }

    fun updateList() {
        rvField.layoutManager = LinearLayoutManager(this)
        rvField.adapter =
            FieldListAdapter(this@FieldListActivity, viewModel!!.fieldList.value!!, this)
    }

    /**Override*/
    override fun onClick(view: View?) {
        when (view) {
            fbAdd -> {
                //  IntentUtil.moveNextScreen(this, AddFarmActivity::class.java)
                val intent = Intent(this, AddFarmActivity::class.java)
                startActivityForResult(intent, REQUEST_FARM)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FARM) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel!!.getAllFarmVM(userToken(this))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**Callback*/
    override fun onClickItem(position: Int) {
        val fieldData: ResAllFarms = viewModel!!.fieldList.value!![position]
        val gson = Gson()
        val type = object : TypeToken<ResAllFarms>() {}.type
        val json = gson.toJson(fieldData, type)
        var intentWithData = Intent(this, FarmDetailActivity::class.java)
        intentWithData.putExtra("jsonObject", json)
        startActivity(intentWithData)

    }

    override fun editFarmCallback(pos: Int) {
        var intentWithData = Intent(this, UpdateFarmActivity::class.java)
        intentWithData.putExtra("farmId", viewModel!!.fieldList.value!![pos].farmId!!)
        startActivity(intentWithData)
    }

    override fun deleteFarmCallback(position: Int) {
        dialogConformDelete("Delete Farm", "Are you sure want to delete the farm", this, position)
    }

    /**  Alert Dialog*/

    private fun dialogConformDelete(
        title: String?,
        message: String?,
        context: Context,
        pos: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle("$title")
            .setMessage("$message")

            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok) { _, _ ->

                viewModel!!.deleteFarm(userToken(this), viewModel!!.fieldList.value!![pos].farmId!!)
            }
            .show()
    }


}

interface FarmListCallback {
    fun onClickItem(position: Int)
    fun editFarmCallback(position: Int)
    fun deleteFarmCallback(position: Int)
}
