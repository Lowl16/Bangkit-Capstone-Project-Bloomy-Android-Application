package com.capstone.bloomy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.capstone.bloomy.R
import com.capstone.bloomy.databinding.ActivityResetPasswordBinding
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@ResetPasswordActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarResetPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnResetPassword.setOnClickListener {
            val resetPassword = binding.btnResetPassword
            val oldPassword = binding.etOldPasswordReset.text.toString()
            val newPassword = binding.etNewPasswordReset.text.toString()
            val confirmNewPassword = binding.etConfirmNewPasswordReset.text.toString()

            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()) {
                showLoading(resetPassword, true)

                profileViewModel.resetPassword(oldPassword, newPassword, confirmNewPassword)
                profileViewModel.resetPasswordResponse.observe(this@ResetPasswordActivity) { response ->
                    val error = response?.error
                    val message = response?.message.toString()

                    if (error != null) {
                        showLoading(resetPassword, false)
                        if (error == true) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            profileViewModel.defaultResetPassword()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            profileViewModel.defaultResetPassword()

                            val profileIntent = Intent(this, MainActivity::class.java)
                            profileIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            profileIntent.putExtra("navigateToProfileFragment", true)
                            startActivity(profileIntent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToMainActivity()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToMainActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateToProfileFragment", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showLoading(resetPassword: Button, isLoading: Boolean) { resetPassword.text = if (!isLoading) getString(R.string.btn_reset_password) else getString(R.string.btn_loading) }
}