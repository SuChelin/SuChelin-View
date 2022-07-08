package com.Guide.suchelin.MapStore

import android.os.Bundle
import android.util.Log
import com.Guide.suchelin.config.BaseActivity
import com.Guide.suchelin.databinding.ActivityMapStoreBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import kotlinx.coroutines.Job

class MapStoreActivity : BaseActivity<ActivityMapStoreBinding>(
    ActivityMapStoreBinding::inflate
) {

    private lateinit var mapView: MapView
    private var naverMap: NaverMap? = null
    private lateinit var mapControl : MapStoreControl
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        // intent 가져오기
        val storeName = intent.getStringExtra("StoreName") ?: ""
        val latitude = intent.getDoubleExtra("latitude", 37.214185)
        val longitude = intent.getDoubleExtra("longitude", 126.978792)

        mapControl = MapStoreControl(this, latitude, longitude, storeName)

        // 지도 가져오기
        mapView.getMapAsync { map ->
            naverMap = map
            naverMap ?: return@getMapAsync

            Log.d("naverData", "naverMap : ${naverMap}")
            // 지도 설정
            mapControl.setMapUI(naverMap!!)

        }

        binding.storeMapDetailBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        job?.cancel()
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}