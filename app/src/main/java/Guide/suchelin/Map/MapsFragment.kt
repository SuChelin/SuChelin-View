package Guide.suchelin.Map

import Guide.suchelin.ContactActivity
import Guide.suchelin.DataControl
import Guide.suchelin.R
import Guide.suchelin.config.BaseFragment
import Guide.suchelin.config.MyApplication
import Guide.suchelin.databinding.FragmentMapBinding
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    // viewPagerFlag
    private var finishFlag = false
    private var mapFlag = false
    private var dataControlFlag = false

    // 현재 위치 버튼 사용하기
    private var locationFlag = false
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //배너광고
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //고객센터
        binding.listIvContact.setOnClickListener {
            val intent = Intent(context, ContactActivity::class.java)
            startActivity(intent)
        }


        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        val mapDataList = DataControl().getStoreDataMap(requireContext())

        // dataControl 설정
        initDataControl()

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
            mapControl.setMarker(naverMap!!, mapDataList, binding, this)

            // 지도 viewpager 설정
            mapFlag = true
            setViewpager()

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

    private fun initDataControl() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (MyApplication.dataControl.initFlag()) {
                dataControlFlag = true
                // adapter 설정
                setViewpager()
            } else {
                Log.d("dataControl", "mapFragment init 실행됨")
                if (!finishFlag) initDataControl()
            }
        }, 300)
    }

    // 지도 설정
    private fun setViewpager() {
        if (mapFlag && dataControlFlag) {
            naverMap ?: return

            // 여기 viewpager 설정하는 곳 점수 정보는 아래!!
            // 점수 정보 : MyApplication.dataControl.allScores
            Log.d("dataControl", "data = ${MyApplication.dataControl.allScores}")

            mapControl.jobArrayLit.add(
                CoroutineScope(Dispatchers.Main).launch {
                    mapControl.setViewpager(naverMap!!, binding, MyApplication.dataControl.allScores)
                }
            )

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

    override fun onDestroy() {
        mapControl.finishJob()
        finishFlag = true
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
