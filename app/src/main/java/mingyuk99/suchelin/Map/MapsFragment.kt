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
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.ScaleBarView
import com.naver.maps.map.widget.ZoomControlView
import mingyuk99.suchelin.R

class MapsFragment : Fragment(){

    private lateinit var naverMapFragment: MapFragment
    private var naverMap : NaverMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        naverMapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                requireActivity().supportFragmentManager.beginTransaction().add(
                    R.id.map,
                    it
                ).commit()
            }

        naverMapFragment.getMapAsync { map ->
            Toast.makeText(requireContext(), "NaverMap 객체 반환 성공", Toast.LENGTH_SHORT).show()
            view.findViewById<LocationButtonView>(R.id.zoomButton).map = map
            view.findViewById<ScaleBarView>(R.id.scaleBar).map = map
            naverMap = map
            naverMap ?: return@getMapAsync

            MapControl().setMapUI(naverMap!!)

        }
    }

}
