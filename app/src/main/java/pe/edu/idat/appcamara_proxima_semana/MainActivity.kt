package pe.edu.idat.appcamara_proxima_semana

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import pe.edu.idat.appcamara_proxima_semana.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var file: File;
    private var rutaFotoActual = ""
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
        if(rutaFotoActual != ""){
            val fotoUri = obtenerContenidoUri(File(rutaFotoActual))
            val intentImagen = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, fotoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/jpeg"
            }
            val chooser = Intent.createChooser(intentImagen,
                "Compartir Foto")
            if(intentImagen.resolveActivity(packageManager) != null){
                startActivity(chooser)
            }
        }
    }

    private fun tomarFoto() {
        //abrirCamara.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            it.resolveActivity(packageManager).also {
                componente ->
                crearArchivoFoto()
                val fotoUri: Uri = obtenerContenidoUri(file);
                it.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
            }
        }
        abrirCamara.launch(intent)
    }

    private val abrirCamara = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == RESULT_OK){
            //val data = result.data!!
            //val imagenBitMap = data.extras!!.get("data") as Bitmap
            binding.ivCamara.setImageBitmap(obtenerImagenBitmap())
        }
    }

    private fun crearArchivoFoto(){
        val directorioImg = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        file = File.createTempFile("IMG_${System.currentTimeMillis()}_"
            ,".jpg"
            , directorioImg)
        rutaFotoActual = file.absolutePath
    }

    private fun obtenerImagenBitmap(): Bitmap{
        return BitmapFactory.decodeFile(file.toString())
    }

    private fun obtenerContenidoUri(archivoFoto: File): Uri{
        return FileProvider.getUriForFile(
            applicationContext,
            "pe.edu.idat.appcamara_proxima_semana.fileprovider",
            archivoFoto
        )
    }
}