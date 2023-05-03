package mx.itson.luna

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.luna.entidades.Ubicacion
import mx.itson.luna.utileria.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    var mapa: GoogleMap? = null
    var elevacion :Float? = null
    var temperature : Float? = null
    var windspeed : Float?= null
    var winddirection : Float? = null
    var weathercode : Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    // obteneruUbicacion()
    }

    fun obteneruUbicacion(lat:String , lon:String): Ubicacion? {
        val llamada :Call<Ubicacion> = RetrofitUtil.getApi().getClima(lat,lon,true)
        val clima : Ubicacion? =null
        llamada.enqueue(object: Callback<Ubicacion>{
            override fun onResponse(call: Call<Ubicacion>, response: Response<Ubicacion>) {
            val ubicacion :Ubicacion?= response.body()
                elevacion = ubicacion?.elevation
                temperature = ubicacion?.clima?.temperature
                windspeed = ubicacion?.clima?.temperature
                winddirection = ubicacion?.clima?.temperature
                weathercode = ubicacion?.clima?.temperature

                //Mostrar en la ventana
                val intent = Intent(applicationContext,informacion::class.java)
                intent.putExtra("elevacion",elevacion)
                intent.putExtra("temperature", temperature)
                intent.putExtra("windspeed", windspeed)
                intent.putExtra("winddirection", winddirection)
                intent.putExtra("weathercode", weathercode)

                startActivity(intent)

    }

    override fun onFailure(call: Call<Ubicacion>, t: Throwable) {
        TODO("Not yet implemented")
    }
})
        return clima

    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {

            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            val estaOtorgado = ActivityCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            if(estaOtorgado){
                googleMap.isMyLocationEnabled = true
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),1)

            }

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(locationManager.getBestProvider(
                Criteria(), true)!!)

            //if (location != null){
              //  onLocationChanged(location)
           // }

            location?.let { onLocationChanged(it) }


        } catch (ex: Exception){
            Log.e("Ocurrio un error al cargar el mapa",ex.toString())
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitud : Double = location.latitude
        val longitud  : Double = location.longitude

        val latLng = LatLng(latitud, longitud)

        //Limpia los marker que pudiesen existir en el mapa
        mapa?.clear()
        //Agregar un marker a la posiscion de latitud y longitud
        mapa?.addMarker(MarkerOptions().position(latLng).draggable(true))
        // Mueve la camara o vista del mapa hacia la ubicación del marker
        mapa?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        // Aplica un zoom al mapa en la ubicación
        mapa?.animateCamera(CameraUpdateFactory.zoomTo(12f))

        mapa?.setOnMarkerDragListener(object : OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position



                obteneruUbicacion(latLng.latitude.toString(),latLng.longitude.toString())


            }

            override fun onMarkerDragStart(p0: Marker) {
               
            }

        })
    }

}