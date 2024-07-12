package com.capstone.bloomy.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.model.TodayNewsModel
import com.capstone.bloomy.data.model.TopNewsModel
import com.capstone.bloomy.data.remote.currentweather.CurrentWeatherConfig
import com.capstone.bloomy.data.response.ProfileData
import com.capstone.bloomy.databinding.FragmentDashboardBinding
import com.capstone.bloomy.ui.activity.NewsDetailActivity
import com.capstone.bloomy.ui.adapter.TodayNewsAdapter
import com.capstone.bloomy.ui.adapter.TopNewsAdapter
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.SailDecisionViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.SailDecisionViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class DashboardFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentDashboardBinding? = null
    private var outlookValue: Int = 0
    private var temperatureValue: Int = 0
    private var humidityValue: Int = 0
    private var windSpeedValue: Int = 0

    private val binding get() = _binding!!
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let { location ->
                updateLocationUI(location)
            }
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (binding.tvLocationSailDecision.text == "N/A") {
            binding.imgLocationSailDecision.visibility = View.GONE
            binding.tvLocationSailDecision.visibility = View.GONE
            binding.tvOutlookSailDecision.visibility = View.GONE
            binding.tvTemperatureSailDecision.visibility = View.GONE
            binding.imgOutlookSailDecision.visibility = View.GONE
            binding.cardViewHumiditySailDecision.visibility = View.GONE
            binding.cardViewWindSpeedSailDecision.visibility = View.GONE
            binding.cardViewWindDirectionSailDecision.visibility = View.GONE
            binding.cardViewDescriptionSailDecision.visibility = View.GONE
            binding.invalidSailDecision.visibility = View.VISIBLE
            binding.invalidDescriptionSailDecision.visibility = View.VISIBLE
        }

        if (hasLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                null
            )
        } else {
            requestLocationPermission()
        }

        val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(requireContext())
        val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

        profileViewModel.getProfile()
        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            setProfile(profile)
        }

        val sailDecisionViewModelFactory: SailDecisionViewModelFactory = SailDecisionViewModelFactory.getInstance(requireContext())
        val sailDecisionViewModel: SailDecisionViewModel by viewModels { sailDecisionViewModelFactory }

        sailDecisionViewModel.sailDecisionResponse.observe(viewLifecycleOwner) { response ->
            val code = response?.status?.code
            val sailDecisionData = response?.status?.sailDecisionData
            val message = response?.status?.message.toString()

            if (code != null) {
                if (code == 405) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    sailDecisionViewModel.defaultSailDecision()
                } else {
                    binding.tvDescriptionSailDecision.text = sailDecisionData?.decision
                    sailDecisionViewModel.defaultSailDecision()
                }
            }
        }

        val recyclerViewTopNews = view.findViewById<RecyclerView>(R.id.recycler_view_top_news)
        val recyclerViewTodayNews = view.findViewById<RecyclerView>(R.id.recycler_view_today_news)

        recyclerViewTopNews.setHasFixedSize(true)
        recyclerViewTodayNews.setHasFixedSize(true)

        val topNewsList = getListTopNews()
        val todayNewsList = getListTodayNews()

        showTopNewsList(recyclerViewTopNews, topNewsList)
        showTodayNewsList(recyclerViewTodayNews, todayNewsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        _binding = null
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (hasLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                null
            )
        } else {
            requestLocationPermission()
        }

        val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(requireContext())
        val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

        profileViewModel.getProfile()
        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            setProfile(profile)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)
            .setFastestInterval(5000)
    }

    @Suppress("DEPRECATION")
    private fun updateLocationUI(location: android.location.Location) {
        val geoCoder = Geocoder(requireContext(), Locale("id"))
        val currentLocation = geoCoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )
        val countryName = currentLocation?.first()?.countryName ?: "Indonesia"
        val fullCityName = currentLocation?.first()?.subAdminArea ?: "Jakarta"
        val latitude = currentLocation?.first()?.latitude
        val longitude = currentLocation?.first()?.longitude
        val cityName = extractCityName(fullCityName)
        val locationText = "$cityName, $countryName"

        binding.tvLocationSailDecision.text = locationText

        binding.imgLocationSailDecision.visibility = View.VISIBLE
        binding.tvLocationSailDecision.visibility = View.VISIBLE
        binding.tvOutlookSailDecision.visibility = View.VISIBLE
        binding.tvTemperatureSailDecision.visibility = View.VISIBLE
        binding.imgOutlookSailDecision.visibility = View.VISIBLE
        binding.cardViewHumiditySailDecision.visibility = View.VISIBLE
        binding.cardViewWindSpeedSailDecision.visibility = View.VISIBLE
        binding.cardViewWindDirectionSailDecision.visibility = View.VISIBLE
        binding.cardViewDescriptionSailDecision.visibility = View.VISIBLE
        binding.invalidSailDecision.visibility = View.GONE
        binding.invalidDescriptionSailDecision.visibility = View.GONE

        getCurrentWeather(latitude.toString(), longitude.toString())
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)

    private fun requestLocationPermission() = EasyPermissions.requestPermissions(this, getString(R.string.sail_decision_permission), PERMISSION_LOCATION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)

    private fun setProfile(profile: ProfileData) {
        with(binding) {
            Glide.with(this@DashboardFragment)
                .load(profile.photo)
                .into(imgProfileDashboard)

            val tvHelloUsernameDashboardText = getString(R.string.tv_hello_username, if (profile.nama.isNotEmpty()) profile.nama else profile.username)
            tvHelloUsernameDashboard.text = tvHelloUsernameDashboardText
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentWeather(lat: String, lon: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = CurrentWeatherConfig.api.getCurrentWeather(lat, lon, "metric", "29d730ce50152e2ad59f0da578b33a43")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val weatherMain = response.body()!!.weather?.get(0)?.main ?: "N/A"

                        val outlookInfo = getOutlookInfo(weatherMain)
                        binding.tvOutlookSailDecision.text = outlookInfo.first
                        binding.imgOutlookSailDecision.setImageResource(outlookInfo.second)

                        val temperature = response.body()!!.main?.temp as? Double ?: 0.0
                        val humidity = response.body()!!.main?.humidity ?: 0
                        val windSpeed = response.body()!!.wind?.speed as? Double ?: 0.0

                        outlookValue = getOutlookValue(weatherMain)
                        temperatureValue = getTemperatureValue(temperature)
                        humidityValue = getHumidityValue(humidity)
                        windSpeedValue = getWindSpeedValue(windSpeed)

                        val sailDecisionViewModelFactory: SailDecisionViewModelFactory = SailDecisionViewModelFactory.getInstance(requireContext())
                        val sailDecisionViewModel: SailDecisionViewModel by viewModels { sailDecisionViewModelFactory }

                        sailDecisionViewModel.sailDecision(outlookValue, temperatureValue, humidityValue, windSpeedValue)

                        val temperatureText = getString(R.string.temperature_format, response.body()!!.main?.temp)
                        val humidityText = getString(R.string.humidity_format, response.body()!!.main?.humidity)
                        val windSpeedText = getString(R.string.wind_speed_format, response.body()!!.wind?.speed)

                        binding.tvTemperatureSailDecision.text = temperatureText
                        binding.tvHumiditySailDecision.text = humidityText
                        binding.tvWindSpeedSailDecision.text = windSpeedText
                        binding.tvWindDirectionSailDecision.text = convertWindDirection(response.body()!!.wind?.deg ?: 0)
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "App Error ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Http Error ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Unexpected Error ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getOutlookInfo(weatherMain: String): Pair<String, Int> {
        return when (weatherMain) {
            in arrayOf("Thunderstorm", "Drizzle", "Rain", "Snow", "Squall", "Tornado") ->
                Pair("Rain", R.drawable.icon_outlook_rain)
            in arrayOf("Mist", "Smoke", "Haze", "Fog", "Ash", "Clouds") ->
                Pair("Overcast", R.drawable.icon_outlook_overcast)
            in arrayOf("Dust", "Sand", "Clear") ->
                Pair("Sunny", R.drawable.icon_outlook_sunny)
            else ->
                Pair("Unknown", R.drawable.icon_outlook_sunny)
        }
    }

    private fun convertWindDirection(degrees: Int): String {
        val directions = arrayOf(
            "N", "NNE", "NE", "ENE",
            "E", "ESE", "SE", "SSE",
            "S", "SSW", "SW", "WSW",
            "W", "WNW", "NW", "NNW", "N"
        )

        val index = ((degrees + 11.25) % 360 / 22.5).toInt()
        return directions[index]
    }

    private fun getOutlookValue(weatherMain: String): Int {
        return when (weatherMain) {
            "Sunny" -> 0
            "Overcast" -> 1
            "Rain" -> 2
            else -> 0
        }
    }

    private fun getTemperatureValue(temperature: Double): Int {
        return when {
            temperature > 28.0 -> 0
            temperature in 20.0..28.0 -> 1
            else -> 1
        }
    }

    private fun getHumidityValue(humidity: Int): Int {
        return if (humidity > 70) {
            0
        } else {
            1
        }
    }

    private fun getWindSpeedValue(windSpeed: Double): Int {
        return if (windSpeed > 7.3) {
            1
        } else {
            0
        }
    }

    private fun getListTopNews(): ArrayList<TopNewsModel> {
        val dataTitleTopNews = resources.getStringArray(R.array.data_title_top_news)
        val dataImageUrlTopNews = resources.getStringArray(R.array.data_image_url_top_news)
        val dataWebUrlTopNews = resources.getStringArray(R.array.data_web_url_top_news)
        val topNewsList = ArrayList<TopNewsModel>()

        val minLength = minOf(dataTitleTopNews.size, dataImageUrlTopNews.size, dataWebUrlTopNews.size)

        for (i in 0 until minLength) {
            val topNews = TopNewsModel(dataTitleTopNews[i], dataImageUrlTopNews[i], dataWebUrlTopNews[i])
            topNewsList.add(topNews)
        }

        return topNewsList
    }

    private fun getListTodayNews(): ArrayList<TodayNewsModel> {
        val dataTitleTodayNews = resources.getStringArray(R.array.data_title_today_news)
        val dataDateTodayNews = resources.getStringArray(R.array.data_date_today_news)
        val dataImageUrlTodayNews = resources.getStringArray(R.array.data_image_url_today_news)
        val dataWebUrlTodayNews = resources.getStringArray(R.array.data_web_url_today_news)
        val todayNewsList = ArrayList<TodayNewsModel>()

        val minLength = minOf(dataTitleTodayNews.size, dataDateTodayNews.size, dataImageUrlTodayNews.size, dataWebUrlTodayNews.size)

        for (i in 0 until minLength) {
            val todayNews = TodayNewsModel(dataTitleTodayNews[i], dataDateTodayNews[i], dataImageUrlTodayNews[i], dataWebUrlTodayNews[i])
            todayNewsList.add(todayNews)
        }

        return todayNewsList
    }

    private fun showTopNewsList(recyclerView: RecyclerView, topNewsList: ArrayList<TopNewsModel>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val topNewsAdapter = TopNewsAdapter(topNewsList)

        recyclerView.adapter = topNewsAdapter

        topNewsAdapter.setOnItemClickCallback(object : TopNewsAdapter.OnItemClickCallback {
            override fun onItemClicked(topNews: TopNewsModel) {
                val detailTopNewsIntent = Intent(requireActivity(), NewsDetailActivity::class.java)
                detailTopNewsIntent.putExtra("selected_top_news", topNews)
                startActivity(detailTopNewsIntent)
            }
        })
    }

    private fun showTodayNewsList(recyclerView: RecyclerView, todayNewsList: ArrayList<TodayNewsModel>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val todayNewsAdapter = TodayNewsAdapter(todayNewsList)

        recyclerView.adapter = todayNewsAdapter
        recyclerView.isNestedScrollingEnabled = false

        todayNewsAdapter.setOnItemClickCallback(object : TodayNewsAdapter.OnItemClickCallback {
            override fun onItemClicked(todayNews: TodayNewsModel) {
                val detailTodayNewsIntent = Intent(requireActivity(), NewsDetailActivity::class.java)
                detailTodayNewsIntent.putExtra("selected_today_news", todayNews)
                startActivity(detailTodayNewsIntent)
            }
        })
    }

    private fun extractCityName(fullCityName: String): String {
        return fullCityName.replaceFirst("(Kota|Kabupaten)\\s".toRegex(), "")
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }
}