package com.example.kausiprojektikevat.presentation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences
@Inject
constructor(
    @ApplicationContext private val app: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPreferences")

    suspend fun saveUserLat(lat: Double) {
        app.dataStore.edit { preferences ->
            preferences[USER_LAST_LAT] = lat
        }
    }

    suspend fun saveUserLon(lon: Double) {
        app.dataStore.edit { preferences ->
            preferences[USER_LAST_LON] = lon
        }
    }

    suspend fun getUserLocation(): LatLng {
        val lat = app.dataStore.data.map { it[USER_LAST_LAT] ?: 0.0 }.first()
        val lon = app.dataStore.data.map { it[USER_LAST_LON] ?: 0.0 }.first()
        return LatLng(lat, lon)
    }

    companion object {
        private val USER_LAST_LAT = doublePreferencesKey("key_user_lat")
        private val USER_LAST_LON = doublePreferencesKey("key_user_lon")
    }
}