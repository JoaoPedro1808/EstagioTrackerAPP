package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import com.example.estagiotrackerapp.models.Vaga
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class NovaVagaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_vaga)

        val ivVoltar = findViewById<ImageView>(R.id.ivVoltar)
        val etNomeEmpresa = findViewById<EditText>(R.id.etNomeEmpresa)
        val etNomeVaga = findViewById<EditText>(R.id.etNomeVaga)
        val etSalario = findViewById<EditText>(R.id.etSalario)
        val spKanban = findViewById<Spinner>(R.id.spKanban)
        val spModelo = findViewById<Spinner>(R.id.spModelo)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val btnSalvarVaga = findViewById<Button>(R.id.btnSalvarVaga)
        val btnCancelarVaga =  findViewById<Button>(R.id.btnCancelarVaga)

        val idUsuarioLogado = intent.getLongExtra("ID_USUARIO", 0L)

        ivVoltar.setOnClickListener {
            finish()
        }

        btnCancelarVaga.setOnClickListener {
            finish()
        }

        btnSalvarVaga.setOnClickListener {
            val nomeEmpresa = etNomeEmpresa.text.toString().trim()
            val nomeVaga = etNomeVaga.text.toString().trim()
            val salarioString = etSalario.text.toString().trim()
            val descricao = etDescricao.text.toString().trim()

            if (nomeEmpresa.isEmpty() || nomeVaga.isEmpty()) {
                Toast.makeText(this@NovaVagaActivity, "Erro ao carregar dados do banco", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val salario = salarioString.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val statusKanban = spKanban.selectedItem.toString().uppercase()
            val modelo = spModelo.selectedItem.toString().uppercase()

            if (idUsuarioLogado != 0L) {
                RetrofitClient.api.cadastrarVaga(
                    nomeEmpresa = nomeEmpresa,
                    nomeVaga = nomeVaga,
                    salario = salario,
                    statusKanban = statusKanban,
                    modelo = modelo,
                    descricao = descricao,
                    usuarioId = idUsuarioLogado
                ).enqueue(object : Callback<Vaga> {
                    override fun onResponse(call: Call<Vaga>, response: Response<Vaga>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@NovaVagaActivity, "Vaga Cadastrada", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val erroBody = response.errorBody()?.string()
                            Log.e("API_NOVA_VAGA", "Erro: ${response.code()} - $erroBody")
                            Toast.makeText(this@NovaVagaActivity, "Erro ao salvar a vaga", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Vaga>, t: Throwable) {
                        Log.e("API_NOVA_VAGA", "Erro de conexão", t)
                        Toast.makeText(this@NovaVagaActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Erro: Usuário não identificado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}