package mx.itson.luna.entidades
import com.google.gson.annotations.SerializedName
class Clima {

    @SerializedName("temperature")
    var temperature : Float? = null

    var windspeed : Float? = null

    var weathercode : Int? = null

    var winddirection : Float? = null
}