package Guide.suchelin.Map

import Guide.suchelin.DataControl
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import Guide.suchelin.R
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.databinding.FragmentMapBinding

class MapsFragment : BaseFragment<FragmentMapBinding>(
    FragmentMapBinding::bind,
    R.layout.fragment_map
) {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10002
    }

    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private var naverMap: NaverMap? = null
    private var mapControl = MapControl()

    // 현재 위치 버튼 사용하기
    private var locationFlag = false
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        val mapDataList = DataControl().getStoreDataMap(requireContext())

        // 권한 설정하기
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // 위치 권한 설정하기
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        // 지도 가져오기
        mapView.getMapAsync { map ->
            naverMap = map
            naverMap ?: return@getMapAsync

            // 현재 위치 활성화
            naverMap!!.locationSource = locationSource

            // 지도 설정
            mapControl.setMapUI(naverMap!!)
            mapControl.setMarkerAndViewpager(naverMap!!, mapDataList, binding, this)

            // 위치 추적 설정
            binding.mapLocationButton.setOnClickListener {
                naverMap ?: return@setOnClickListener

                // 현재 위치 기능 활성화 상태 확인
                if (!locationManager.isLocationEnabled) {
                    Toast.makeText(requireContext(), "현재 위치 기능을 활성화해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
                locationFlag = if (!locationFlag) {
                    naverMap?.locationTrackingMode = LocationTrackingMode.Follow
                    true
                } else {
                    naverMap?.locationTrackingMode = LocationTrackingMode.None
                    // 홈으로 이동
                    mapControl.goHome(naverMap!!)
                    false
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap?.locationTrackingMode = LocationTrackingMode.None
            }
            return
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

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
