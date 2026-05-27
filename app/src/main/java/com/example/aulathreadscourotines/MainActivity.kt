package com.example.aulathreadscourotines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aulathreadscourotines.api.EnderecoAPI
import com.example.aulathreadscourotines.api.PostagemAPI
import com.example.aulathreadscourotines.api.RetrofitHelper
import com.example.aulathreadscourotines.api.RetrofitHelper.Companion.retrofit
import com.example.aulathreadscourotines.databinding.ActivityMainBinding
import com.example.aulathreadscourotines.model.Comentario
import com.example.aulathreadscourotines.model.Endereco
import com.example.aulathreadscourotines.model.Postagem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val bindind by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val retrofit by lazy {
        RetrofitHelper.retrofit
    }

    private var pararThread = false
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindind.root)


        bindind.btnAbrir.setOnClickListener {
            startActivity(
                Intent(this, MainActivity2::class.java)
            )
        }

        bindind.btnParar.setOnClickListener {
            //pararThread = true
            job?.cancel()
            bindind.btnIniciar.text = "Reiniciar execussao"
            bindind.btnIniciar.isEnabled = true
        }

        bindind.btnIniciar.setOnClickListener {

            /* repeat(30){indice ->
                 Log.i("info_thread", "Executando: $indice T: ${Thread.currentThread().name}")
                 Thread.sleep(1000)//millis 1000 = 1s
             }*//*
            //MinhaThread().start()
            //Thread(MinhaRunnable()).start()
            *//* Thread{
                 repeat(30){indice ->
                     Log.i("info_thread", "Executando: $indice T: ${Thread.currentThread().name}")
                     runOnUiThread {
                         bindind.btnIniciar.text = "Executando: $indice T: ${Thread.currentThread().name}"
                         bindind.btnIniciar.isEnabled = false

                         if (indice == 29){
                             bindind.btnIniciar.text = "Reiniciar execussao"
                             bindind.btnIniciar.isEnabled = true
                         }
                     }
                     Thread.sleep(1000)//millis 1000 = 1s
                 }
             }.start()*//*

            job = CoroutineScope(Dispatchers.IO).launch {

              *//*  repeat(15) { indice ->
                    Log.i(
                        "info_coroutines",
                        "Executando: $indice T: ${Thread.currentThread().name}"
                    )
                    withContext(Dispatchers.Main) {
                        bindind.btnIniciar.text =
                            "Executando: $indice T: ${Thread.currentThread().name}"
                    }
                    delay(1000)//millis 1000 = 1s
                }*//*
                *//*withTimeout(7000L){
                    execultar()
                }*//*

                val tempo = measureTimeMillis {

                    val resultado1 = async { tarefa1() }
                    val resultado2 = async { tarefa2() }

                    Log.i("info_coroutines","resultado1: ${resultado1.await()}")
                    Log.i("info_coroutines","resultado2: ${resultado2.await()}")

                    withContext(Dispatchers.Main) {
                        bindind.btnIniciar.text = "${resultado1.await()}"
                        bindind.btnParar.text = "${resultado2.await()}"
                    }

                    *//* var resultado1: String? = null
                    var resultado2: String? = null

                   val job1 =  launch {
                        resultado1 = tarefa1()
                    }

                   val job2 = launch {
                        resultado2 = tarefa2()
                    }

                    job1.join()
                    job2.join()*//*

                    *//*Log.i("info_coroutines","resultado1: $resultado1")
                    Log.i("info_coroutines","resultado2: $resultado2")*//*
                }
                Log.i("info_coroutine","Tempo: $tempo")

            }*/

            CoroutineScope(Dispatchers.IO).launch {
                //recuperarEndereco()
                //recuperarPostagens()
                //recuperarPostagemUnica()
               // recuperarComentariosParaPostagem()
                salvarPostagem()
            }
        }


    }

    private suspend fun salvarPostagem() {
        var retorno: Response<Postagem> ? = null

        val postagem = Postagem(
            "Corpo da postagem",
            -1,
            "Titulo da postagem",
            1090
        )

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            //retorno = postagemAPI.recuperarComentariosParaPostagem(1) //Path
            //retorno = postagemAPI.recuperarComentariosParaPostagemQurery(1) //Query
            retorno = postagemAPI.salvarPostagem(postagem)
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_jsonplace", "erro ao recuperar")
        }

        if (retorno != null){
            if (retorno.isSuccessful){
                val postagem = retorno.body()

                val id = postagem?.id
                val titulo = postagem?.title
                val idUsuario = postagem?.userId

                var resultado = "[${retorno.code()}]id:$id - T:$titulo - U:$idUsuario"

                withContext(Dispatchers.Main){
                    bindind.textResultado.text = resultado
                }
            }else{
                withContext(Dispatchers.Main){
                    bindind.textResultado.text = "ERRO CODE: ${retorno.code()}"
            }
        }
    }

    suspend fun recuperarComentariosParaPostagem() {
        var retorno: Response<List<Comentario>> ? = null


        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
           //retorno = postagemAPI.recuperarComentariosParaPostagem(1) //Path
            retorno = postagemAPI.recuperarComentariosParaPostagemQurery(1) //Query
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_jsonplace", "erro ao recuperar")
        }

        if (retorno != null){
            if (retorno.isSuccessful){
                val listaComentarios = retorno.body()

                var resultado = ""
                listaComentarios?.forEach { comentario ->
                    val idComentario = comentario.id
                    val email = comentario.email

                    val comentarioResultado = "$idComentario - $email \n"

                    resultado += comentarioResultado
                }
                withContext(Dispatchers.Main){
                    bindind.textResultado.text = resultado
                }
            }
        }
    }



    suspend fun recuperarPostagemUnica() {
        var retorno: Response<Postagem> ? = null


        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            retorno = postagemAPI.recuperarPostagemUnica(3 )
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_jsonplace", "erro ao recuperar")
        }

        if (retorno != null){
            if (retorno.isSuccessful){
                val postagem = retorno.body()
                val resultado = "${postagem?.id} - ${postagem?.title}"

                withContext(Dispatchers.Main){
                    bindind.textResultado.text =  resultado
                }
                Log.i("info_jsonplace", resultado)

                }
            }
        }

    suspend fun recuperarPostagens() {
        var retorno: Response<List<Postagem>> ? = null


        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            retorno = postagemAPI.recuperarPostagens()
        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_jsonplace", "erro ao recuperar")
        }

        if (retorno != null){
            if (retorno.isSuccessful){
                val lisaPostagens = retorno.body()
                lisaPostagens?.forEach { postagem ->
                val id = postagem.id
                val title = postagem.title
                    Log.i("info_jsonplace", "$id - $title")
                }
            }
        }
    }

    suspend fun recuperarEndereco(){

        var retorno: Response<Endereco>? = null
        val cepDigitadoUsuario = "68650000"

       try {
           val enderecoAPI = retrofit.create(EnderecoAPI::class.java)
           retorno = enderecoAPI.recuperarEndereco(cepDigitadoUsuario)
       }catch (e: Exception){
           e.printStackTrace()
           Log.i("info_endereco", "erro ao recuperar")
       }

        if (retorno != null){
            if (retorno.isSuccessful){
                val endereco = retorno.body()
                val rua = endereco?.logradouro
                val cidade = endereco?.localidade
                val cep = endereco?.cep
                Log.i("info_endereco", "endereco: $rua, $cidade, $cep")
            }
        }
    }

            suspend fun tarefa1(): String {
        repeat(3) { indice ->
            Log.i("info_coroutines", "Tarefa1: $indice T: ${Thread.currentThread().name}")
            delay(1000L)//millis 1000 = 1s
        }
        return "Execultou tarefa 1"
    }

    suspend fun tarefa2(): String {
        repeat(5) { indice ->
            Log.i("info_coroutines", "Tarefa2: $indice T: ${Thread.currentThread().name}")
            delay(1000L)//millis 1000 = 1s
        }
        return "Execultou tarefa 2"
    }

    suspend fun execultar() {
        repeat(15) { indice ->
            Log.i(
                "info_coroutines",
                "Executando: $indice T: ${Thread.currentThread().name}"
            )
            withContext(Dispatchers.Main) {
               // binding.btnIniciar.text = "Executando: $indice T: ${Thread.currentThread().name}"
                //bindind.btnIniciar.isEnabled = false
            }
            delay(1000L)//millis 1000 = 1s
        }
    }



    suspend fun recuperarPostagensPeloId(idUsuario: Int): List<String> {
        delay(2000)//2s
        return listOf(
            "Viagem Nordeste",
            "Estudando Android",
            "jantando restaurante"
        )
    }

    suspend fun recuperarUsuarioLogado(): Usuario {
        delay(2000)//2s
        return Usuario(1020, "Cleo")
    }}}

   /* inner class MinhaRunnable : Runnable {
        override fun run() {
            repeat(30) { indice ->

                if (pararThread) {
                    pararThread = false
                    return
                }


                Log.i("info_thread", "Executando: $indice T: ${Thread.currentThread().name}")
                runOnUiThread {
                    bindind.btnIniciar.text =
                        "Executando: $indice T: ${Thread.currentThread().name}"
                    bindind.btnIniciar.isEnabled = false

                    if (indice == 29) {
                        bindind.btnIniciar.text = "Reiniciar execussao"
                        bindind.btnIniciar.isEnabled = true
                    }
                }
                Thread.sleep(1000)//millis 1000 = 1s
            }
        }

    }*/

   /* inner class MinhaThread : Thread() {
        override fun run() {
            super.run()

            repeat(30) { indice ->
                Log.i("info_thread", "Executando: $indice T: ${currentThread().name}")
                runOnUiThread {
                    bindind.btnIniciar.text = "Executando: $indice T: ${currentThread().name}"
                    bindind.btnIniciar.isEnabled = false

                    if (indice == 29) {
                        bindind.btnIniciar.text = "Reiniciar execussao"
                        bindind.btnIniciar.isEnabled = true
                    }
                }
                sleep(1000)//millis 1000 = 1s
            }
        }
    }
}*/