package mingyuk99.suchelin.Map

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.ScaleBarView
import com.naver.maps.map.widget.ZoomControlView
import mingyuk99.suchelin.R

class MapsFragment : Fragment(){

    private lateinit var mapView: MapView
    private var naverMap : NaverMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)


        mapView.getMapAsync { map ->
            Toast.makeText(requireContext(), "NaverMap 객체 반환 성공", Toast.LENGTH_SHORT).show()
            view.findViewById<LocationButtonView>(R.id.zoomButton).map = map
            view.findViewById<ScaleBarView>(R.id.scaleBar).map = map
            naverMap = map
            naverMap ?: return@getMapAsync

            MapControl().setMapUI(naverMap!!)
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
