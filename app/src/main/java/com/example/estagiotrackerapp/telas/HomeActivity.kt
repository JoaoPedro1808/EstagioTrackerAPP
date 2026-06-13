package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import com.example.estagiotrackerapp.models.Vaga
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val ivSair = findViewById<ImageView>(R.id.ivSair)
        val btnNovaVaga = findViewById<MaterialCardView>(R.id.btnNovaVaga)
        val btnBuscarNome = findViewById<MaterialButton>(R.id.btnBuscarNome)
        val btnBuscarId = findViewById<MaterialButton>(R.id.btnBuscarId)
        val navKanban = findViewById<LinearLayout>(R.id.navKanban)
        val navConfig = findViewById<LinearLayout>(R.id.navConfig)

        val idUsuarioLogado = intent.getLongExtra("ID_USUARIO", 0L)

        if (idUsuarioLogado != 0L) {
            carregarDadosDoUsuario(idUsuarioLogado)
        }

        ivSair.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnNovaVaga.setOnClickListener {
            val intent = Intent(this, NovaVagaActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        btnBuscarNome.setOnClickListener {
            val intent = Intent(this, VagaDetalhesActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        btnBuscarId.setOnClickListener {
            val intent = Intent(this, VagaDetalhesActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        navKanban.setOnClickListener {
            val intent = Intent(this, KanbanActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        navConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }
    }

    private fun carregarDadosDoUsuario(idDoUsuario: Long) {
        val tvTotalVagas = findViewById<TextView>(R.id.tvTotalVagas)
        val tvInteresses = findViewById<TextView>(R.id.tvInteresses)
        val tvInscrito = findViewById<TextView>(R.id.tvInscrito)
        val tvAprovado = findViewById<TextView>(R.id.tvAprovado)
        val tvDesaprovado = findViewById<TextView>(R.id.tvDesaprovado)

        val rvVagas = findViewById<RecyclerView>(R.id.rvVagas)

        RetrofitClient.api.buscarVagasDoUsuario(idDoUsuario).enqueue(object : Callback<List<Vaga>> {
            override fun onResponse(call: Call<List<Vaga>>, response: Response<List<Vaga>>) {
                if (response.isSuccessful) {
                    val todasAsVagas = response.body() ?: emptyList()

                    tvTotalVagas.text = todasAsVagas.size.toString()
                    tvInteresses.text = todasAsVagas.count { it.statusKanban.equals("Interesse", ignoreCase = true) }.toString()
                    tvInscrito.text = todasAsVagas.count { it.statusKanban.equals("Inscrito", ignoreCase = true) }.toString()
                    tvAprovado.text = todasAsVagas.count { it.statusKanban.equals("Aprovado", ignoreCase = true) }.toString()
                    tvDesaprovado.text = todasAsVagas.count { it.statusKanban.equals("Desaprovado", ignoreCase = true) }.toString()

                    val tresVagasMaisRecentes = todasAsVagas.takeLast(3).reversed()

                    rvVagas.layoutManager = LinearLayoutManager(this@HomeActivity)
                    rvVagas.adapter = VagaAdapter(tresVagasMaisRecentes)

                } else {
                    Log.e("API_HOME", "Erro na resposta do servidor: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Vaga>>, t: Throwable) {
                Log.e("API_HOME", "Erro de conexão", t)
                Toast.makeText(this@HomeActivity, "Erro ao carregar dados do banco", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class VagaAdapter(private val vagas: List<Vaga>) : RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {
        class VagaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNomeVaga: TextView = view.findViewById(R.id.tvNomeVagaItem)
            val tvNomeEmpresa: TextView = view.findViewById(R.id.tvNomeEmpresaItem)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaga, parent, false)
            return VagaViewHolder(view)
        }

        override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
            val vagaAtual = vagas[position]
            holder.tvNomeVaga.text = vagaAtual.nomeVaga
            holder.tvNomeEmpresa.text = vagaAtual.nomeEmpresa
        }

        override fun getItemCount(): Int = vagas.size
    }
}