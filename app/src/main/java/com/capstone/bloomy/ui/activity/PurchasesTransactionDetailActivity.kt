package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProfileByUsernameData
import com.capstone.bloomy.data.response.TransactionByIdData
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivityPurchasesTransactionDetailBinding
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.TransactionViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.TransactionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class PurchasesTransactionDetailActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var tvValueQuantity = 1

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var savedDay = 0
    private var formattedDay = ""
    private var savedMonth = 0
    private var formattedMonth = ""
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    private val transactionId = TRANSACTION_ID
    private val sellerUsername = SELLER_USERNAME

    private val transactionViewModelFactory: TransactionViewModelFactory = TransactionViewModelFactory.getInstance(this@PurchasesTransactionDetailActivity)
    private val transactionViewModel: TransactionViewModel by viewModels { transactionViewModelFactory }

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@PurchasesTransactionDetailActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private lateinit var binding: ActivityPurchasesTransactionDetailBinding
    private lateinit var etPickupDate: TextInputEditText

    @SuppressLint("InflateParams", "MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasesTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarPurchasesTransactionDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        transactionViewModel.getTransactionById(transactionId)
        transactionViewModel.detailTransaction.observe(this) { transaction ->
            setDetailTransaction(transaction)

            binding.btnEditPurchasesTransactionDetail.setOnClickListener {
                if (transaction.status == "0") {
                    val bottomSheetDialog = BottomSheetDialog(this)
                    val view = layoutInflater.inflate(R.layout.edit_purchases_transaction_bottom_sheet_dialog, null)

                    val btnCancel = view.findViewById<ImageView>(R.id.img_cancel_edit_purchases_transaction_bottom_sheet_dialog)
                    val btnEdit = view.findViewById<MaterialButton>(R.id.btn_edit_purchases_transaction_bottom_sheet_dialog)

                    val deliveryMethod = resources.getStringArray(R.array.value_delivery_method)
                    val arrayAdapter = ArrayAdapter(this, R.layout.item_list_delivery_method, deliveryMethod)
                    val etDeliveryMethod = view.findViewById<AutoCompleteTextView>(R.id.et_delivery_method_edit_purchases_transaction_bottom_sheet_dialog)

                    val tvHintPickUpDate = view.findViewById<TextView>(R.id.tv_hint_pickup_date_edit_purchases_transaction_bottom_sheet_dialog)
                    val tvTotal = view.findViewById<TextView>(R.id.tv_value_total_edit_purchases_transaction_bottom_sheet_dialog)

                    val tvQuantity = view.findViewById<TextView>(R.id.tv_value_quantity_edit_purchases_transaction_bottom_sheet_dialog)
                    val quantitySubstract = view.findViewById<ImageView>(R.id.img_subtract_quantity_edit_purchases_transaction_bottom_sheet_dialog)
                    val quantityAdd = view.findViewById<ImageView>(R.id.img_add_quantity_edit_purchases_transaction_bottom_sheet_dialog)

                    tvValueQuantity = transaction.weight
                    tvQuantity.text = tvValueQuantity.toString()
                    tvTotal.text = formatCurrency(transaction.price)

                    etPickupDate = view.findViewById(R.id.et_pickup_date_edit_purchases_transaction_bottom_sheet_dialog)

                    if (transaction.type == "0") {
                        tvHintPickUpDate.visibility = View.GONE
                        etPickupDate.visibility = View.GONE
                    } else {
                        tvHintPickUpDate.visibility = View.VISIBLE
                        etPickupDate.visibility = View.VISIBLE
                    }

                    etPickupDate.text = if (transaction.datePickup.isNullOrEmpty()) {
                        Editable.Factory.getInstance().newEditable("")
                    } else {
                        Editable.Factory.getInstance().newEditable(formatDate(transaction.datePickup))
                    }

                    etDeliveryMethod.text = when (transaction.type) {
                        "0" -> Editable.Factory.getInstance().newEditable(getString(R.string.delivery))
                        "1" -> Editable.Factory.getInstance().newEditable(getString(R.string.self_pickup))
                        else -> Editable.Factory.getInstance().newEditable(getString(R.string.delivery))
                    }

                    etDeliveryMethod.setAdapter(arrayAdapter)

                    etDeliveryMethod.setOnItemClickListener { _, _, position, _ ->
                        val selectedDeliveryMethod = deliveryMethod[position]

                        if (selectedDeliveryMethod == getString(R.string.self_pickup)) {
                            tvHintPickUpDate.visibility = View.VISIBLE
                            etPickupDate.visibility = View.VISIBLE
                            etPickupDate.text = Editable.Factory.getInstance().newEditable(getString(R.string.tv_value_pickup_date_buy_bottom_sheet_dialog))
                        } else {
                            tvHintPickUpDate.visibility = View.GONE
                            etPickupDate.visibility = View.GONE
                            etPickupDate.text = Editable.Factory.getInstance().newEditable("")
                        }
                    }

                    quantitySubstract.setOnClickListener {
                        if (tvValueQuantity > 1) {
                            tvValueQuantity--
                            tvQuantity.text = tvValueQuantity.toString()
                            tvTotal.text = formatCurrency(transaction.productTransactionByIdData.pricePerKg.toInt() * tvValueQuantity)
                        } else {
                            Toast.makeText(this, getString(R.string.invalid_quantity), Toast.LENGTH_SHORT).show()
                        }
                    }

                    quantityAdd.setOnClickListener {
                        tvValueQuantity++
                        tvQuantity.text = tvValueQuantity.toString()
                        tvTotal.text = formatCurrency(transaction.productTransactionByIdData.pricePerKg.toInt() * tvValueQuantity)
                    }

                    etPickupDate.setOnClickListener {
                        getDateTimeCalendar()

                        DatePickerDialog(this, this, year, month, day).show()
                    }

                    btnCancel.setOnClickListener {
                        tvValueQuantity = transaction.weight

                        bottomSheetDialog.dismiss()
                    }

                    btnEdit.setOnClickListener {
                        val type = view.findViewById<AutoCompleteTextView>(R.id.et_delivery_method_edit_purchases_transaction_bottom_sheet_dialog)
                        val quantity = view.findViewById<TextView>(R.id.tv_value_quantity_edit_purchases_transaction_bottom_sheet_dialog)
                        val datePickup = view.findViewById<TextInputEditText>(R.id.et_pickup_date_edit_purchases_transaction_bottom_sheet_dialog)
                        val idTransactionValue = transactionId
                        val typeValue = when (type.text.toString()) {
                            getString(R.string.delivery_method_buy_bottom_sheet_dialog) -> "0"
                            getString(R.string.self_pickup_method_buy_bottom_sheet_dialog) -> "1"
                            else -> "0"
                        }
                        val quantityValue = quantity.text.toString().toInt()
                        val datePickupValue = datePickup.text.toString()

                        if (datePickupValue != getString(R.string.tv_value_pickup_date_buy_bottom_sheet_dialog)) {
                            transactionViewModel.editPurchasesTransaction(idTransactionValue, typeValue, quantityValue, datePickupValue)
                            transactionViewModel.editPurchasesTransactionResponse.observe(this@PurchasesTransactionDetailActivity) { response ->
                                val error = response?.error
                                val message = response?.message.toString()

                                if (error != null) {
                                    if (error == true) {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        transactionViewModel.defaultEditPurchasesTransaction()
                                    } else {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        transactionViewModel.defaultEditPurchasesTransaction()

                                        transactionViewModel.getTransactionById(transactionId)
                                        transactionViewModel.detailTransaction.observe(this) { transaction ->
                                            setDetailTransaction(transaction)
                                        }

                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
                        }
                    }

                    bottomSheetDialog.setCancelable(false)

                    bottomSheetDialog.setContentView(view)

                    bottomSheetDialog.show()
                } else {
                    Toast.makeText(this, getString(R.string.invalid_edit_transaction), Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnRemovePurchasesTransactionDetail.setOnClickListener {
                if (transaction.status == "0") {
                    showRemoveTransactionDialog()
                } else {
                    Toast.makeText(this, getString(R.string.invalid_remove_transaction), Toast.LENGTH_SHORT).show()
                }
            }
        }

        profileViewModel.getProfileByUsername(sellerUsername)
        profileViewModel.profileByUsername.observe(this) { profile ->
            setSellerAddress(profile)
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToTransactionActivity()
            }
        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1 // Adding 1 because Calendar months are zero-based
        savedYear = year

        formattedMonth = String.format("%02d", savedMonth)
        formattedDay = String.format("%02d", savedDay)

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        val formattedHour = String.format("%02d", savedHour)
        val formattedMinute = String.format("%02d", savedMinute)

        etPickupDate.text = Editable.Factory.getInstance().newEditable("$savedYear-$formattedMonth-$formattedDay $formattedHour:$formattedMinute:00")
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

    private fun getDateTimeCalendar() {
        val calendar = Calendar.getInstance()

        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
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
            Glide.with(this@PurchasesTransactionDetailActivity)
                .load(transaction.sellerTransactionByIdData.picture)
                .into(imgUserPurchasesTransactionDetail)

            Glide.with(this@PurchasesTransactionDetailActivity)
                .load(transaction.productTransactionByIdData.picture)
                .into(imgProductPurchasesTransactionDetail)

            tvUserPurchasesTransactionDetail.text = transaction.sellerTransactionByIdData.nama.ifEmpty { getString(R.string.tv_default_user_purchases_transaction_detail) }
            tvGradePurchasesTransactionDetail.text = transaction.productTransactionByIdData.grade
            tvTitlePurchasesTransactionDetail.text = transaction.productTransactionByIdData.nama
            tvPricePurchasesTransactionDetail.text = formatCurrency(transaction.productTransactionByIdData.pricePerKg.toInt()) + "/kg"
            tvDeliveryMethodPurchasesTransactionDetail.text = when (transaction.type) {
                "0" -> getString(R.string.delivery)
                "1" -> getString(R.string.self_pickup)
                else -> "Unknown"
            }
            tvQuantityPurchasesTransactionDetail.text = transaction.weight.toString()
            if (transaction.noResi.isNullOrEmpty()) {
                tvValueReceiptNumberPurchasesTransactionDetail.text = getString(R.string.tv_default_value_receipt_number_purchases_transaction_detail)
            } else {
                tvValueReceiptNumberPurchasesTransactionDetail.text = transaction.noResi
            }
            tvValueStatusPurchasesTransactionDetail.text = when (transaction.status) {
                "0" -> getString(R.string.not_confirmed)
                "1" -> getString(R.string.in_process)
                "2" -> getString(R.string.shipped)
                "3" -> getString(R.string.finished)
                "4" -> getString(R.string.canceled)
                else -> "Unknown"
            }
            if (transaction.datePickup.isNullOrEmpty()) {
                tvValuePickupDatePurchasesTransactionDetail.text = getString(R.string.tv_default_value_pickup_date_purchases_transaction_detail)
            } else {
                tvValuePickupDatePurchasesTransactionDetail.text = formatDate(transaction.datePickup)
            }
            tvValueSubtotalPurchasesTransactionDetail.text = formatCurrency(transaction.price).ifEmpty { getString(R.string.tv_default_value_subtotal_purchases_transaction_detail) }
            tvValueShippingCostPurchasesTransactionDetail.text = formatCurrency(transaction.ongkir).ifEmpty { getString(R.string.tv_default_value_shipping_cost_purchases_transaction_detail) }
            tvValueTotalPurchasesTransactionDetail.text = formatCurrency(transaction.totalPrice).ifEmpty { getString(R.string.tv_default_value_total_purchases_transaction_detail) }
        }
    }

    private fun setSellerAddress(profile: ProfileByUsernameData) {
        with(binding) {
            tvValueSellerAddressPurchasesTransactionDetail.text = profile.alamat.ifEmpty { getString(R.string.default_location) }
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

    private fun formatCurrency(amount: Int): String {
        val formattedAmount = String.format("Rp%,d", amount)
        return formattedAmount.replace(',', '.')
    }

    private fun navigateToTransactionActivity() {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showLoadingBuyProduct(buyProduct: Button, isLoading: Boolean) { buyProduct.text = if (!isLoading) getString(R.string.btn_buy_bottom_sheet_dialog) else getString(R.string.btn_loading) }

    companion object {
        var TRANSACTION_ID = "transaction_id"
        var SELLER_USERNAME = "seller_username"
    }
}