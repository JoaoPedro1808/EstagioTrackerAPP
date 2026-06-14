package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KanbanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanban)

        val ivSair = findViewById<ImageView>(R.id.ivSair)
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navConfig = findViewById<LinearLayout>(R.id.navConfig)

        val idUsuarioLogado = intent.getLongExtra("ID_USUARIO", 0L)

        ivSair.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        navConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        if (idUsuarioLogado != 0L) {
            carregarKanban(idUsuarioLogado)
        } else {
            Toast.makeText(this@KanbanActivity, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarKanban(idUsuario: Long) {
        val rvInteresse = findViewById<RecyclerView>(R.id.rvInteresse)
        val rvInscricao = findViewById<RecyclerView>(R.id.rvInscricao)
        val rvAprovado = findViewById<RecyclerView>(R.id.rvAprovado)
        val rvDesaprovado = findViewById<RecyclerView>(R.id.rvDesaprovado)

        rvInteresse.layoutManager = LinearLayoutManager(this)
        rvInscricao.layoutManager = LinearLayoutManager(this)
        rvAprovado.layoutManager = LinearLayoutManager(this)
        rvDesaprovado.layoutManager = LinearLayoutManager(this)

        RetrofitClient.api.buscarVagasDoUsuario(idUsuario).enqueue(object : Callback<List<Vaga>> {
            override fun onResponse(call: Call<List<Vaga>>, response: Response<List<Vaga>>) {
                if (response.isSuccessful) {
                    val vagas = response.body() ?: emptyList()

                    val vagaInteresse = vagas.filter {
                        it.statusKanban.equals("INTERESSE", ignoreCase = true)
                    }

                    val vagasInscricao = vagas.filter {
                        it.statusKanban.equals("INSCRITO", ignoreCase = true)
                    }

                    val vagasAprovado = vagas.filter {
                        it.statusKanban.equals("APROVADO", ignoreCase = true)
                    }

                    val vagasDesaprovado = vagas.filter {
                        it.statusKanban.equals("DESAPROVADO", ignoreCase = true)
                    }

                    rvInteresse.adapter = VagasKanbanAdapter(vagaInteresse)
                    rvInscricao.adapter = VagasKanbanAdapter(vagasInscricao)
                    rvAprovado.adapter = VagasKanbanAdapter(vagasAprovado)
                    rvDesaprovado.adapter = VagasKanbanAdapter(vagasDesaprovado)

                } else {
                    Log.e("API_KANBAN", "Erro na resposta do servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Vaga>?>, t: Throwable) {
                Log.e("API_KANBAN", "Erro de conexão")
            }
        })
    }

    class VagasKanbanAdapter(private val vagas: List<Vaga>) : RecyclerView.Adapter<VagasKanbanAdapter.VagaViewHolder>() {
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