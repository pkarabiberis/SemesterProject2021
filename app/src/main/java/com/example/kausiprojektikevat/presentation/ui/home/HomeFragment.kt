package com.example.kausiprojektikevat.presentation.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kausiprojektikevat.*
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.databinding.FragmentHomeBinding
import com.example.kausiprojektikevat.domain.model.distance.Distance
import com.example.kausiprojektikevat.presentation.UserPreferences
import com.example.kausiprojektikevat.presentation.ui.hasLocationPermissions
import com.example.kausiprojektikevat.presentation.ui.home.recyclerview.LocationAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var myLocation: LatLng? = null
    private var locations: MutableList<Location> = mutableListOf()

    private lateinit var searchIcon: MenuItem

    @Inject
    lateinit var userPreferences: UserPreferences

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        requestPermissions()

        setHasOptionsMenu(true)
        setupLocationClient()
        setupRecyclerView()
        viewModel.getLocations()
        subscribeToObservers()
    }

    @SuppressLint("VisibleForTests")
    private fun setupLocationClient() {
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        startForeGroundService()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclerView() = binding.rvAllLocations.apply {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).also {
                it.setDrawable(resources.getDrawable(R.drawable.divider))
            })

        locationAdapter = LocationAdapter(
            onItemClicked = { location, _ ->
                val args = Bundle().also {
                    it.putString("locationName", location.name)
                    it.putString("picUrl", location.imgRef)
                }

                findNavController().navigate(R.id.action_navigation_home_to_locationFragment, args)
            },
            onLongClick = { location ->
                Glide
                    .with(this)
                    .load(location.imgRef)
                    .into(binding.locationPictureBig)
                binding.locationPictureBig.apply {
                    visibility = View.VISIBLE
                    z = 1f
                }
            },
            onReleaseLongClick = {
                binding.locationPictureBig.apply {
                    visibility = View.GONE
                    z = 0f
                }
            }
        )
        adapter = locationAdapter
    }

    @SuppressLint("MissingPermission")
    private fun subscribeToObservers() {

        viewModel.locations.observe(viewLifecycleOwner, { location ->
            locations.addAll(location)
            location?.let {
                locationAdapter.submitItems(it)
                if (locations.isNotEmpty()) {
                    for (loc in locations) {
                        runBlocking {
                            viewModel.getDistances(
                                myLocation?.toUrlForm() ?: userPreferences.getUserLocation()
                                    .toUrlForm(),
                                LatLng(loc.lat!!, loc.lon!!).toUrlForm(),
                                resources.getString(R.string.google_maps_key)
                            )
                        }
                    }
                }
            }
        })

        viewModel.distances.observe(viewLifecycleOwner, { distances ->
            locationAdapter.submitDistances(distances)
        })
    }

    private fun LatLng.toUrlForm(): String {
        val lat = latitude.toString()
        val lon = longitude.toString()
        return "$lat,$lon"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        searchIcon = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchIcon.actionView as SearchView
        searchView.apply {
            queryHint = "Etsi kohteita..."
            findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).apply {
                this.setColorFilter(resources.getColor(R.color.colorAccent))
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    locationAdapter.filter.filter(newText)
                    return false
                }

            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun requestPermissions() {
        if (hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startForeGroundService() {
        val request = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                for (location in locations) {
                    myLocation = LatLng(location.latitude, location.longitude)
                    runBlocking {
                        userPreferences.apply {
                            saveUserLat(location.latitude)
                            saveUserLon(location.longitude)
                        }
                    }

                }
            }
        }
    }
}