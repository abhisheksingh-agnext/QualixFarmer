package com.agnext.qualixfarmer.warehouse.allWarehouseList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.fields.addField.AddFarmActivity
import com.agnext.qualixfarmer.warehouse.addWarehouse.AddWarehouseActivity
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import kotlinx.android.synthetic.main.activity_field_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class AllWarehouseListActivity : BaseActivity(), WarehouseDetailCallback, View.OnClickListener {


    private lateinit var viewModel: WareListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_warehouse_list)
        initView()
    }

    fun initView() {

        //setting toolbar
        toolbar.title = "Warehouse"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //Registry click7
//        fbAdd.setOnClickListener(this)
//        fbAdd.visibility = View.VISIBLE

        //VM
        viewModel = ViewModelProvider(
            this,
            WareListViewModelFactory(WareListInteractor(this))
        )[WareListViewModel::class.java]
        viewModel!!.wareListState.observe(::getLifecycle, ::updateUI)

        //Basic data
        viewModel.getWarehouseVM()
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<WareListState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }


    private fun processLoginState(renderState: WareListState) {
        when (renderState) {
            WareListState.wareListFailure -> {

            }
            WareListState.wareListSuccess -> {
                updateList()
            }
        }

    }


    fun updateList() {
        rvField.layoutManager = LinearLayoutManager(this)
        rvField.adapter = WarehouseListAdapter(this, viewModel!!.warehouseList.value!!, this)
    }


    /**System Override methods*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        when (view) {
            fbAdd->{
                val intent = Intent(this, AddWarehouseActivity::class.java)
                startActivityForResult(intent, Constant.REQUEST_FARM)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClickItem(position: Int) {
    }
}
