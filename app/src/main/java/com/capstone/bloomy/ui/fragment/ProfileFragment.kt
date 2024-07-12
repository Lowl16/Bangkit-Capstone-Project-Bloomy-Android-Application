package com.capstone.bloomy.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.response.ProfileData
import com.capstone.bloomy.databinding.FragmentProfileBinding
import com.capstone.bloomy.ui.activity.EditProfileActivity
import com.capstone.bloomy.ui.activity.FavoriteActivity
import com.capstone.bloomy.ui.activity.ResetPasswordActivity
import com.capstone.bloomy.ui.activity.ShopActivity
import com.capstone.bloomy.ui.activity.SignInActivity
import com.capstone.bloomy.ui.activity.TransactionActivity
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.UserPreferencesViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.UserPreferencesViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(requireContext())
        val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

        profileViewModel.getProfile()

        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            setProfile(profile)
        }

        binding.actionFavoriteProfile.setOnClickListener {
            val favoriteIntent = Intent(requireActivity(), FavoriteActivity::class.java)
            startActivity(favoriteIntent)
        }

        binding.actionTransactionProfile.setOnClickListener {
            val transactionIntent = Intent(requireActivity(), TransactionActivity::class.java)
            startActivity(transactionIntent)
        }

        binding.cardViewMyShopProfile.setOnClickListener {
            val shopIntent = Intent(requireActivity(), ShopActivity::class.java)
            startActivity(shopIntent)
        }

        binding.cardViewEditProfile.setOnClickListener {
            val editProfileIntent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(editProfileIntent)
        }

        binding.cardViewResetPassword.setOnClickListener {
            val resetPasswordIntent = Intent(requireActivity(), ResetPasswordActivity::class.java)
            startActivity(resetPasswordIntent)
        }

        binding.cardViewChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.cardViewSignOut.setOnClickListener {
            showSignOutDialog()
        }
    }

    private fun setProfile(profile: ProfileData) {
        with(binding) {
            Glide.with(this@ProfileFragment)
                .load(profile.photo)
                .into(imgProfile)

            tvNameProfile.text = profile.nama.ifEmpty { getString(R.string.default_name) }
            tvPhoneNumberProfile.text = profile.nohp.ifEmpty { getString(R.string.default_phone_number) }
            tvEmailProfile.text = profile.email.ifEmpty { getString(R.string.default_email) }
        }
    }

    private fun showSignOutDialog() {
        val activity = activity ?: return

        val dialog = Dialog(activity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.sign_out_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userPreferences: UserPreferences = UserPreferences.getInstance(requireActivity().application.dataStore)
        val userPreferencesViewModelFactory: UserPreferencesViewModelFactory = UserPreferencesViewModelFactory.getInstance(userPreferences)
        val userPreferencesViewModel: UserPreferencesViewModel by viewModels { userPreferencesViewModelFactory }

        val btnNoSignOut: Button = dialog.findViewById(R.id.btn_no_sign_out_dialog)
        val btnYesSignOut: Button = dialog.findViewById(R.id.btn_yes_sign_out_dialog)

        btnYesSignOut.setOnClickListener {
            userPreferencesViewModel.clearSession()

            dialog.dismiss()

            val signInIntent = Intent(requireContext(), SignInActivity::class.java)
            signInIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(signInIntent)
        }

        btnNoSignOut.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}