package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProfileByUsernameData
import com.capstone.bloomy.data.response.TransactionByIdData
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivitySalesTransactionDetailBinding
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.TransactionViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.TransactionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class SalesTransactionDetailActivity : AppCompatActivity() {

    private val transactionId = TRANSACTION_ID
    private val buyerUsername = BUYER_USERNAME

    private val transactionViewModelFactory: TransactionViewModelFactory = TransactionViewModelFactory.getInstance(this@SalesTransactionDetailActivity)
    private val transactionViewModel: TransactionViewModel by viewModels { transactionViewModelFactory }

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@SalesTransactionDetailActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private lateinit var binding: ActivitySalesTransactionDetailBinding

    @SuppressLint("InflateParams", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarSalesTransactionDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        transactionViewModel.getTransactionById(transactionId)
        transactionViewModel.detailTransaction.observe(this) { transaction ->
            setDetailTransaction(transaction)

            binding.btnEditSalesTransactionDetail.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.edit_sales_transaction_bottom_sheet_dialog, null)

                val btnCancel = view.findViewById<ImageView>(R.id.img_cancel_edit_sales_transaction_bottom_sheet_dialog)
                val btnEdit = view.findViewById<MaterialButton>(R.id.btn_edit_sales_transaction_bottom_sheet_dialog)

                val status = resources.getStringArray(R.array.value_status)
                val arrayAdapter = ArrayAdapter(this, R.layout.item_list_delivery_method, status)
                val etStatus = view.findViewById<AutoCompleteTextView>(R.id.et_status_edit_sales_transaction_bottom_sheet_dialog)
                val tilShippingCost = view.findViewById<TextInputLayout>(R.id.til_shipping_cost_edit_sales_transaction_bottom_sheet_dialog)
                val etShippingCost = view.findViewById<TextInputEditText>(R.id.et_shipping_cost_edit_sales_transaction_bottom_sheet_dialog)
                val tilReceiptNumber = view.findViewById<TextInputLayout>(R.id.til_receipt_number_edit_sales_transaction_bottom_sheet_dialog)
                val etReceiptNumber = view.findViewById<TextInputEditText>(R.id.et_receipt_number_edit_sales_transaction_bottom_sheet_dialog)

                etStatus.text = when (transaction.status) {
                    "0" -> Editable.Factory.getInstance().newEditable(getString(R.string.not_confirmed))
                    "1" -> Editable.Factory.getInstance().newEditable(getString(R.string.in_process))
                    "3" -> Editable.Factory.getInstance().newEditable(getString(R.string.shipped))
                    "4" -> Editable.Factory.getInstance().newEditable(getString(R.string.finished))
                    "5" -> Editable.Factory.getInstance().newEditable(getString(R.string.canceled))
                    else -> Editable.Factory.getInstance().newEditable(getString(R.string.not_confirmed))
                }

                if (transaction.type == "1") {
                    tilShippingCost.isEnabled = false
                    etShippingCost.setTextColor(resources.getColor(R.color.gray_light))
                    tilReceiptNumber.isEnabled = false
                    etReceiptNumber.setTextColor(resources.getColor(R.color.gray_light))
                }

                etShippingCost.text = if (transaction.ongkir.toString().isNullOrEmpty()) {
                    Editable.Factory.getInstance().newEditable("")
                } else {
                    Editable.Factory.getInstance().newEditable(transaction.ongkir.toString())
                }

                etReceiptNumber.text = if (transaction.noResi.isNullOrEmpty()) {
                    Editable.Factory.getInstance().newEditable("")
                } else {
                    Editable.Factory.getInstance().newEditable(transaction.noResi)
                }

                etStatus.setAdapter(arrayAdapter)

                btnCancel.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                btnEdit.setOnClickListener {
                    val statusTransaction = view.findViewById<AutoCompleteTextView>(R.id.et_status_edit_sales_transaction_bottom_sheet_dialog)
                    val receiptNumber = view.findViewById<TextView>(R.id.et_receipt_number_edit_sales_transaction_bottom_sheet_dialog)
                    val shippingCost = view.findViewById<TextInputEditText>(R.id.et_shipping_cost_edit_sales_transaction_bottom_sheet_dialog)
                    val idTransactionValue = transactionId
                    val statusValue = when (statusTransaction.text.toString()) {
                        getString(R.string.not_confirmed) -> "0"
                        getString(R.string.in_process) -> "1"
                        getString(R.string.shipped) -> "2"
                        getString(R.string.finished) -> "3"
                        getString(R.string.canceled) -> "5"
                        else -> "0"
                    }
                    val receiptNumberValue = receiptNumber.text.toString()
                    val shippingCostValue = shippingCost.text.toString().toInt()

                    transactionViewModel.editSalesTransaction(idTransactionValue, statusValue, receiptNumberValue, shippingCostValue)
                    transactionViewModel.editSalesTransactionResponse.observe(this@SalesTransactionDetailActivity) { response ->
                        val error = response?.error
                        val message = response?.message.toString()

                        if (error != null) {
                            if (error == true) {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                transactionViewModel.defaultEditSalesTransaction()
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                transactionViewModel.defaultEditSalesTransaction()

                                transactionViewModel.getTransactionById(transactionId)
                                transactionViewModel.detailTransaction.observe(this) { transaction ->
                                    setDetailTransaction(transaction)
                                }

                                bottomSheetDialog.dismiss()
                            }
                        }
                    }
                }

                bottomSheetDialog.setCancelable(false)

                bottomSheetDialog.setContentView(view)

                bottomSheetDialog.show()
            }

            binding.btnRemoveSalesTransactionDetail.setOnClickListener {
                if (transaction.status == "0") {
                    showRemoveTransactionDialog()
                } else {
                    Toast.makeText(this, getString(R.string.invalid_remove_transaction), Toast.LENGTH_SHORT).show()
                }
            }
        }

        profileViewModel.getProfileByUsername(buyerUsername)
        profileViewModel.profileByUsername.observe(this) { profile ->
            setBuyerAddress(profile)
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToTransactionActivity()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToTransactionActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRemoveTransactionDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.remove_transaction_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnNoRemoveTransaction: Button = dialog.findViewById(R.id.btn_no_remove_transaction_dialog)
        val btnYesRemoveTransaction: Button = dialog.findViewById(R.id.btn_yes_remove_transaction_dialog)

        btnYesRemoveTransaction.setOnClickListener {
            transactionViewModel.deleteTransaction(transactionId).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> { }

                        is ResultState.Success -> {
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                            navigateToTransactionActivity()
                        }

                        is ResultState.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        btnNoRemoveTransaction.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailTransaction(transaction: TransactionByIdData) {
        with(binding) {
            Glide.with(this@SalesTransactionDetailActivity)
                .load(transaction.buyerTransactionByIdData.picture)
                .into(imgUserSalesTransactionDetail)

            Glide.with(this@SalesTransactionDetailActivity)
                .load(transaction.productTransactionByIdData.picture)
                .into(imgProductSalesTransactionDetail)

            tvUserSalesTransactionDetail.text = transaction.buyerTransactionByIdData.namaBuyer.ifEmpty { getString(R.string.tv_default_user_sales_transaction_detail) }
            tvGradeSalesTransactionDetail.text = transaction.productTransactionByIdData.grade
            tvTitleSalesTransactionDetail.text = transaction.productTransactionByIdData.nama
            tvPriceSalesTransactionDetail.text = formatCurrency(transaction.productTransactionByIdData.pricePerKg.toInt()) + "/kg"
            tvDeliveryMethodSalesTransactionDetail.text = when (transaction.type) {
                "0" -> getString(R.string.delivery)
                "1" -> getString(R.string.self_pickup)
                else -> "Unknown"
            }
            tvQuantitySalesTransactionDetail.text = transaction.weight.toString()
            if (transaction.noResi.isNullOrEmpty()) {
                tvValueReceiptNumberSalesTransactionDetail.text = getString(R.string.tv_default_value_receipt_number_purchases_transaction_detail)
            } else {
                tvValueReceiptNumberSalesTransactionDetail.text = transaction.noResi
            }
            tvValueStatusSalesTransactionDetail.text = when (transaction.status) {
                "0" -> getString(R.string.not_confirmed)
                "1" -> getString(R.string.in_process)
                "2" -> getString(R.string.shipped)
                "3" -> getString(R.string.finished)
                "4" -> getString(R.string.canceled)
                else -> "Unknown"
            }
            if (transaction.datePickup.isNullOrEmpty()) {
                tvValuePickupDateSalesTransactionDetail.text = getString(R.string.tv_default_value_pickup_date_sales_transaction_detail)
            } else {
                tvValuePickupDateSalesTransactionDetail.text = formatDate(transaction.datePickup)
            }
            tvValueSubtotalSalesTransactionDetail.text = formatCurrency(transaction.price).ifEmpty { getString(R.string.tv_default_value_subtotal_sales_transaction_detail) }
            tvValueShippingCostSalesTransactionDetail.text = formatCurrency(transaction.ongkir).ifEmpty { getString(R.string.tv_default_value_shipping_cost_sales_transaction_detail) }
            tvValueTotalSalesTransactionDetail.text = formatCurrency(transaction.totalPrice).ifEmpty { getString(R.string.tv_default_value_total_sales_transaction_detail) }
        }
    }

    private fun setBuyerAddress(profile: ProfileByUsernameData) {
        with(binding) {
            tvValueBuyerAddressSalesTransactionDetail.text = profile.alamat.ifEmpty { getString(R.string.default_location) }
        }
    }

    private fun formatDate(dateString: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("id", "ID"))
        format.timeZone = TimeZone.getTimeZone("GMT+7")

        val date = format.parse(dateString) ?: return ""

        val outputFormat = SimpleDateFormat("yyyy/MM/dd H:mm", Locale("id", "ID"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        return outputFormat.format(Date(date.time))
    }

    fun formatCurrency(amount: Int): String {
        val formattedAmount = String.format("Rp%,d", amount)
        return formattedAmount.replace(',', '.')
    }

    private fun navigateToTransactionActivity() {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        var TRANSACTION_ID = "transaction_id"
        var BUYER_USERNAME = "buyer_username"
    }
}