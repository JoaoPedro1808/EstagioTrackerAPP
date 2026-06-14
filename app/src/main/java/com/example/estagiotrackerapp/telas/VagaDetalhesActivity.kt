package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class VagaDetalhesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaga_detalhes)

        val ivVoltar = findViewById<ImageView>(R.id.ivVoltar)
        val tvDetalhesId = findViewById<TextView>(R.id.tvDetalhesId)
        val tvDetalhesVaga = findViewById<TextView>(R.id.tvDetalhesVaga)
        val tvDetalhesEmpresa = findViewById<TextView>(R.id.tvDetalhesEmpresa)
        val tvDetalhesData = findViewById<TextView>(R.id.tvDetalhesData)
        val tvDetalhesModelo = findViewById<TextView>(R.id.tvDetalhesModelo)
        val tvDetalhesSalario = findViewById<TextView>(R.id.tvDetalhesSalario)
        val tvDetalhesDescricao = findViewById<TextView>(R.id.tvDetalhesDescricao)
        val btnEditarVaga = findViewById<Button>(R.id.btnEditarVaga)
        val btnRemoverVaga = findViewById<Button>(R.id.btnRemoverVaga)

        val idUsuarioLogado = intent.getLongExtra("ID_USUARIO", 0L)
        val idVaga = intent.getLongExtra("ID_VAGA", 0L)
        val nomeVaga = intent.getStringExtra("NOME_VAGA") ?: ""
        val nomeEmpresa = intent.getStringExtra("NOME_EMPRESA") ?: ""
        val salarioDouble = intent.getDoubleExtra("SALARIO_VAGA", 0.0)
        val modeloVaga = intent.getStringExtra("MODELO_VAGA") ?: "N/A"
        val statusKanban = intent.getStringExtra("STATUS_KANBAN") ?: ""
        val descricaoVaga = intent.getStringExtra("DESCRICAO_VAGA") ?: "Sem descrição disponível."

        tvDetalhesId.text = "id: $idVaga"
        tvDetalhesVaga.text = nomeVaga
        tvDetalhesEmpresa.text = nomeEmpresa
        tvDetalhesModelo.text = modeloVaga.lowercase().replaceFirstChar { it.uppercase() }
        tvDetalhesDescricao.text = descricaoVaga
        tvDetalhesData.text = "Pendente"

        val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        tvDetalhesSalario.text = formatadorMoeda.format(salarioDouble)

        ivVoltar.setOnClickListener {
            finish()
        }

        btnEditarVaga.setOnClickListener {
            val intent = Intent(this, VagaEditarActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            intent.putExtra("ID_VAGA", idVaga)
            intent.putExtra("NOME_VAGA", nomeVaga)
            intent.putExtra("NOME_EMPRESA", nomeEmpresa)
            intent.putExtra("SALARIO_VAGA", salarioDouble)
            intent.putExtra("MODELO_VAGA", modeloVaga)
            intent.putExtra("STATUS_KANBAN", statusKanban)
            intent.putExtra("DESCRICAO_VAGA", descricaoVaga)
            startActivity(intent)
        }

        btnRemoverVaga.setOnClickListener {
            if (idVaga != 0L) {
                RetrofitClient.api.deletarVaga(idVaga).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@VagaDetalhesActivity, "Vaga removida com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Log.e("API_DELETAR_VAGA", "Erro ao deletar: ${response.code()}")
                            Toast.makeText(this@VagaDetalhesActivity, "Erro ao deletar vaga do servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("API_DELETAR_VAGA", "Falha de conexão", t)
                        Toast.makeText(this@VagaDetalhesActivity, "Erro de conexão ao tentar deletar", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Erro: Vaga não identificada.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}