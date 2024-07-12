package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.FavoriteData
import com.capstone.bloomy.data.response.ProductByIdData
import com.capstone.bloomy.data.response.ProfileByUsernameData
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivityMarketProductDetailBinding
import com.capstone.bloomy.ui.viewmodel.FavoriteViewModel
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.TransactionViewModel
import com.capstone.bloomy.ui.viewmodelfactory.FavoriteViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.TransactionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class MarketProductDetailActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMarketProductDetailBinding
    private lateinit var etPickupDate: TextInputEditText

    private var tvValueTotal = ""
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

    private val productId = PRODUCT_ID
    private val sellerUsername = SELLER_USERNAME

    private val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@MarketProductDetailActivity)
    private val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@MarketProductDetailActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private val favoriteViewModelFactory: FavoriteViewModelFactory = FavoriteViewModelFactory.getInstance(this@MarketProductDetailActivity)
    private val favoriteViewModel: FavoriteViewModel by viewModels { favoriteViewModelFactory }

    private val transactionViewModelFactory: TransactionViewModelFactory = TransactionViewModelFactory.getInstance(this@MarketProductDetailActivity)
    private val transactionViewModel: TransactionViewModel by viewModels { transactionViewModelFactory }

    @SuppressLint("InflateParams", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarMarketProductDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        productViewModel.getProductById(productId)
        productViewModel.detailProduct.observe(this) { detailProduct ->
            setDetailProduct(detailProduct)
        }

        profileViewModel.getProfileByUsername(sellerUsername)
        profileViewModel.profileByUsername.observe(this) { profile ->
            setSellerProfile(profile)
        }

        favoriteViewModel.getFavorite()
        favoriteViewModel.favorite.observe(this) { favorite ->
            checkFavorite(favorite)
        }

        binding.tvNameSellerMarketProductDetail.setOnClickListener {
            val sellerDetailIntent = Intent(this, MarketSellerDetailActivity::class.java)
            sellerDetailIntent.putExtra("seller_username", sellerUsername)
            startActivity(sellerDetailIntent)
        }

        binding.imgFavoriteContainerMarketProductDetail.setOnClickListener {
            val favoriteItem = favoriteViewModel.favorite.value?.find { it.idProduct == productId }

            if (favoriteItem != null) {
                favoriteViewModel.deleteFavorite(favoriteItem.idFavorite).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> { }

                            is ResultState.Success -> {
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                                binding.imgFavoriteMarketProductDetail.setImageResource(R.drawable.icon_favorite_inactive)
                            }

                            is ResultState.Error -> {
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                favoriteViewModel.addFavorite(productId)
                favoriteViewModel.addFavoriteResponse.observe(this@MarketProductDetailActivity) { response ->
                    val error = response?.error
                    val message = response?.message.toString()

                    if (error != null) {
                        if (error == true) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            favoriteViewModel.defaultAddFavorite()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            favoriteViewModel.defaultAddFavorite()

                            binding.imgFavoriteMarketProductDetail.setImageResource(R.drawable.icon_favorite_active)
                        }
                    }
                }
            }
        }

        binding.btnBuy.setOnClickListener {
            profileViewModel.getProfile()
            profileViewModel.profile.observe(this) { profile ->
                if (profile.nama.isNullOrEmpty() && profile.nohp.isNullOrEmpty() && profile.provinsi.isNullOrEmpty() && profile.kota.isNullOrEmpty() && profile.alamat.isNullOrEmpty() && profile.description.isNullOrEmpty()) {
                    val editProfileIntent = Intent(this, EditProfileActivity::class.java)
                    startActivity(editProfileIntent)
                } else {
                    val bottomSheetDialog = BottomSheetDialog(this)
                    val view = layoutInflater.inflate(R.layout.buy_bottom_sheet_dialog, null)

                    val btnCancel = view.findViewById<ImageView>(R.id.img_cancel_buy_bottom_sheet_dialog)
                    val btnBuy = view.findViewById<MaterialButton>(R.id.btn_buy_bottom_sheet_dialog)

                    val deliveryMethod = resources.getStringArray(R.array.value_delivery_method)
                    val arrayAdapter = ArrayAdapter(this, R.layout.item_list_delivery_method, deliveryMethod)
                    val etDeliveryMethod = view.findViewById<AutoCompleteTextView>(R.id.et_delivery_method_buy_bottom_sheet_dialog)

                    val tvHintPickUpDate = view.findViewById<TextView>(R.id.tv_hint_pickup_date_buy_bottom_sheet_dialog)
                    val tvTotal = view.findViewById<TextView>(R.id.tv_value_total_buy_bottom_sheet_dialog)

                    val tvQuantity = view.findViewById<TextView>(R.id.tv_value_quantity_buy_bottom_sheet_dialog)
                    val quantitySubstract = view.findViewById<ImageView>(R.id.img_subtract_quantity_buy_bottom_sheet_dialog)
                    val quantityAdd = view.findViewById<ImageView>(R.id.img_add_quantity_buy_bottom_sheet_dialog)

                    tvQuantity.text = tvValueQuantity.toString()
                    tvTotal.text = formatCurrency(tvValueTotal.toInt())

                    etPickupDate = view.findViewById(R.id.et_pickup_date_buy_bottom_sheet_dialog)

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
                            tvTotal.text = formatCurrency(tvValueTotal.toInt() * tvValueQuantity)
                        } else {
                            Toast.makeText(this, getString(R.string.invalid_quantity), Toast.LENGTH_SHORT).show()
                        }
                    }

                    quantityAdd.setOnClickListener {
                        tvValueQuantity++
                        tvQuantity.text = tvValueQuantity.toString()
                        tvTotal.text = formatCurrency(tvValueTotal.toInt() * tvValueQuantity)
                    }

                    etPickupDate.setOnClickListener {
                        getDateTimeCalendar()

                        DatePickerDialog(this, this, year, month, day).show()
                    }

                    btnCancel.setOnClickListener {
                        tvValueQuantity = 1

                        bottomSheetDialog.dismiss()
                    }

                    btnBuy.setOnClickListener {
                        val buyProduct = view.findViewById<Button>(R.id.btn_buy_bottom_sheet_dialog)
                        val type = view.findViewById<AutoCompleteTextView>(R.id.et_delivery_method_buy_bottom_sheet_dialog)
                        val quantity = view.findViewById<TextView>(R.id.tv_value_quantity_buy_bottom_sheet_dialog)
                        val datePickup = view.findViewById<TextInputEditText>(R.id.et_pickup_date_buy_bottom_sheet_dialog)
                        val idProductValue = productId
                        val typeValue = when (type.text.toString()) {
                            getString(R.string.delivery_method_buy_bottom_sheet_dialog) -> "0"
                            getString(R.string.self_pickup_method_buy_bottom_sheet_dialog) -> "1"
                            else -> "0"
                        }
                        val quantityValue = quantity.text.toString().toInt()
                        val datePickupValue = datePickup.text.toString()

                        if (datePickupValue != getString(R.string.tv_value_pickup_date_buy_bottom_sheet_dialog)) {
                            showLoadingBuyProduct(buyProduct, true)

                            transactionViewModel.buyProduct(idProductValue, typeValue, quantityValue, datePickupValue)
                            transactionViewModel.buyProductResponse.observe(this@MarketProductDetailActivity) { response ->
                                val error = response?.error
                                val message = response?.message.toString()

                                if (error != null) {
                                    showLoadingBuyProduct(buyProduct, false)
                                    if (error == true) {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        transactionViewModel.defaultBuyProduct()
                                    } else {
                                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                        transactionViewModel.defaultBuyProduct()

                                        productViewModel.getProductById(productId)
                                        productViewModel.detailProduct.observe(this) { detailProduct ->
                                            setDetailProduct(detailProduct)
                                        }

                                        bottomSheetDialog.dismiss()

                                        val transactionIntent = Intent(this, TransactionActivity::class.java)
                                        transactionIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(transactionIntent)
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
                }
            }
        }
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
                finish()
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

    private fun setDetailProduct(detailProduct: ProductByIdData) {
        with(binding) {
            Glide.with(this@MarketProductDetailActivity)
                .load(detailProduct.picture)
                .into(imgMarketProductDetail)

            tvTitleMarketProductDetail.text = detailProduct.nama
            tvGradeMarketProductDetail.text = formatGrade(detailProduct.grade)
            tvPriceMarketProductDetail.text = formatCurrency(detailProduct.price) + "/kg"
            tvQuantityMarketProductDetail.text = formatWeight(detailProduct.weight)
            tvValueDescriptionMarketProductDetail.text = detailProduct.description

            tvValueTotal = (detailProduct.price).toString()
            tvValueQuantity = 1
        }
    }

    private fun setSellerProfile(profile: ProfileByUsernameData) {
        with(binding) {
            Glide.with(this@MarketProductDetailActivity)
                .load(profile.photo)
                .into(imgSellerMarketProductDetail)

            tvNameSellerMarketProductDetail.text = profile.nama.ifEmpty { getString(R.string.default_name) }
            tvLocationSellerMarketProductDetail.text = profile.kota.ifEmpty { getString(R.string.default_location) }
        }
    }

    private fun checkFavorite(favorite: List<FavoriteData>){
        val isProductInFavorites = favorite.any { it.idProduct == productId }

        if (isProductInFavorites) {
            binding.imgFavoriteMarketProductDetail.setImageResource(R.drawable.icon_favorite_active)
        } else {
            binding.imgFavoriteMarketProductDetail.setImageResource(R.drawable.icon_favorite_inactive)
        }
    }

    private fun formatGrade(grade: String): String {
        return "Grade \"$grade\""
    }

    private fun formatCurrency(amount: Int): String {
        val formattedAmount = String.format("Rp%,d", amount)
        return formattedAmount.replace(',', '.')
    }

    private fun formatWeight(weight: Int): String {
        return "$weight kg left"
    }

    private fun showLoadingBuyProduct(buyProduct: Button, isLoading: Boolean) { buyProduct.text = if (!isLoading) getString(R.string.btn_buy_bottom_sheet_dialog) else getString(R.string.btn_loading) }

    companion object {
        var PRODUCT_ID = "product_id"
        var SELLER_USERNAME = "seller_username"
    }
}