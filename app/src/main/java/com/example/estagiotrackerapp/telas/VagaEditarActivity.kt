package com.example.estagiotrackerapp.telas

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import com.example.estagiotrackerapp.models.Vaga
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class VagaEditarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaga_editar)

        val etEmpresa = findViewById<EditText>(R.id.etEditarEmpresa)
        val etVaga = findViewById<EditText>(R.id.etEditarVaga)
        val etSalario = findViewById<EditText>(R.id.etEditarSalario)
        val spKanban = findViewById<Spinner>(R.id.spEditarKanban)
        val spModelo = findViewById<Spinner>(R.id.spEditarModelo)
        val etDescricao = findViewById<EditText>(R.id.etEditarDescricao)
        val tvId = findViewById<TextView>(R.id.tvEditarId)

        val idVaga = intent.getLongExtra("ID_VAGA", 0L)
        val salarioOriginal = intent.getDoubleExtra("SALARIO_VAGA", 0.0)

        tvId.text = "id: $idVaga"
        etEmpresa.setText(intent.getStringExtra("NOME_EMPRESA"))
        etVaga.setText(intent.getStringExtra("NOME_VAGA"))
        etSalario.setText(salarioOriginal.toString())
        etDescricao.setText(intent.getStringExtra("DESCRICAO_VAGA"))

        setSpinnerValue(spKanban, intent.getStringExtra("STATUS_KANBAN"))
        setSpinnerValue(spModelo, intent.getStringExtra("MODELO_VAGA"))

        findViewById<ImageView>(R.id.ivVoltarEditar).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnSalvarAlteracoes).setOnClickListener {
            RetrofitClient.api.atualizarVaga(
                id = idVaga,
                nomeEmpresa = etEmpresa.text.toString(),
                nomeVaga = etVaga.text.toString(),
                salario = etSalario.text.toString(),
                statusKanban = spKanban.selectedItem.toString().uppercase(),
                modelo = spModelo.selectedItem.toString().uppercase(),
                descricao = etDescricao.text.toString()
            ).enqueue(object : Callback<Vaga> {
                override fun onResponse(call: Call<Vaga>, response: Response<Vaga>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VagaEditarActivity, "Vaga atualizada!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("DEBUG_FINAL", "Erro ${response.code()}: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<Vaga>, t: Throwable) {
                    Log.e("DEBUG_FINAL", "Falha: ${t.message}")
                    Toast.makeText(this@VagaEditarActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }

        findViewById<Button>(R.id.btnRemoverEditar).setOnClickListener {
            RetrofitClient.api.deletarVaga(idVaga).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VagaEditarActivity, "Removido!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API_DELETAR_VAGA", "Falha de conexão", t)
                    Toast.makeText(this@VagaEditarActivity, "Erro de conexão ao tentar deletar", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setSpinnerValue(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString().equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                break
            }
        }
    }
}