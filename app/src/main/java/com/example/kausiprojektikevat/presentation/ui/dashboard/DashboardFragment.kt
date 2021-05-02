package com.example.kausiprojektikevat.presentation.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kausiprojektikevat.R
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.databinding.FragmentDashboardBinding
import com.example.kausiprojektikevat.presentation.UserPreferences
import com.example.kausiprojektikevat.presentation.ui.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: HomeViewModel by viewModels()
    private var map: GoogleMap? = null
    private var locationName: String? = null
    private var imgRef: String? = null

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
            it.setOnMarkerClickListener(markerCallBack)
            it.setOnInfoWindowClickListener(infoWindowClickListener)
            viewModel.getLocations()
            subscribeToObservers()
        }
    }

    private fun subscribeToObservers() {
        viewModel.locations.observe(viewLifecycleOwner, { location ->
            location?.let {
                addMarkerPoints(it)
            }
        })
    }

    private var markerCallBack = GoogleMap.OnMarkerClickListener {
        locationName = it.title.substringBefore(" ")
        imgRef = it.title.substringAfterLast(" ")
        it.title = it.title.substringBefore(" ")
        it.showInfoWindow()
        true
    }

    private var infoWindowClickListener = GoogleMap.OnInfoWindowClickListener {
        val args = Bundle().also {
            it.putString("locationName", locationName)
            it.putString("picUrl", imgRef)
        }
        findNavController().navigate(R.id.action_navigation_dashboard_to_nav_location, args)
    }

    private fun addMarkerPoints(it: List<Location>?) {
        val bounds = LatLngBounds.Builder()
        runBlocking {
            map?.addMarker(
                MarkerOptions().position(userPreferences.getUserLocation()).title("Minä")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_person_pin_circle_black_24dp))
            )
        }
        it?.let {
            for (loc in it) {
                map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(loc.lat!!, loc.lon!!))
                        .title(loc.name.capitalize(Locale.ROOT))
                        .snippet("Näytä tarkemmin")
                )
                bounds.include(LatLng(loc.lat!!, loc.lon!!))
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}