package mx.itson.luna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import mx.itson.luna.R

class informacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion)

        // Obtenieron los valores de la vista anterior
        val valores = intent.extras

        // Se asignaron los valores a los textView correspondientes
        val txtTemperatura = findViewById<TextView>(R.id.txtTemperatura)
        txtTemperatura.text = valores?.getFloat("temperature").toString()
        val txtelevacion = findViewById<TextView>(R.id.txtclima)
        txtelevacion.text = valores?.getFloat("elevacion").toString()
        val txtclima = findViewById<TextView>(R.id.txtclima)
        val txtVientoV = findViewById<TextView>(R.id.txtVientoV)
        txtVientoV.text = valores?.getFloat("txtVientoV").toString()
        val txtVientoD  = findViewById<TextView>(R.id.txtVientoD)
        txtVientoD.text = valores?.getFloat("txtVientoD").toString()

    }
}