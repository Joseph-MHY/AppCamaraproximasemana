package pe.edu.idat.appcamara_proxima_semana

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import pe.edu.idat.appcamara_proxima_semana.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCompartitfoto.setOnClickListener(this)
        binding.btnTomarfoto.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnTomarfoto -> tomarFoto()
            R.id.btnCompartitfoto -> compartirFoto()
        }
    }

    private fun compartirFoto() {
        TODO("Not yet implemented")
    }

    private fun tomarFoto() {
        abrirCamara.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private val abrirCamara = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == RESULT_OK){
            val data = result.data!!
            val imagenBitMap = data.extras!!.get("data") as Bitmap
            binding.ivCamara.setImageBitmap(imagenBitMap)
        }
    }

}