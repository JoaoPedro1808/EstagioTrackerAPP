package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        val etBuscaNome = findViewById<EditText>(R.id.etBuscaNome)
        val etBuscaId = findViewById<EditText>(R.id.etBuscaId)
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
            val nome = etBuscaNome.text.toString()
            if (nome.isNotEmpty()) {
                RetrofitClient.api.buscarVagaPorNome(nome).enqueue(object : Callback<List<Vaga>> {
                    override fun onResponse(call: Call<List<Vaga>>, response: Response<List<Vaga>>) {
                        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                            abrirDetalhesVaga(response.body()!![0], idUsuarioLogado)
                        } else {
                            Toast.makeText(this@HomeActivity, "Vaga não encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<List<Vaga>>, t: Throwable) {
                        Toast.makeText(this@HomeActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        btnBuscarId.setOnClickListener {
            val idTexto = etBuscaId.text.toString()
            if (idTexto.isNotEmpty()) {
                RetrofitClient.api.buscarVagaPorId(idTexto.toLong()).enqueue(object : Callback<Vaga> {
                    override fun onResponse(call: Call<Vaga>, response: Response<Vaga>) {
                        if (response.isSuccessful && response.body() != null) {
                            abrirDetalhesVaga(response.body()!!, idUsuarioLogado)
                        } else {
                            Toast.makeText(this@HomeActivity, "Vaga não encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Vaga>, t: Throwable) {
                        Toast.makeText(this@HomeActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            }
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

    private fun abrirDetalhesVaga(vaga: Vaga, idUsuario: Long) {
        val intent = Intent(this, VagaDetalhesActivity::class.java)
        intent.putExtra("ID_USUARIO", idUsuario)
        intent.putExtra("ID_VAGA", vaga.id)
        intent.putExtra("NOME_VAGA", vaga.nomeVaga)
        intent.putExtra("NOME_EMPRESA", vaga.nomeEmpresa)
        intent.putExtra("SALARIO_VAGA", vaga.salario.toDouble())
        intent.putExtra("MODELO_VAGA", vaga.modelo)
        intent.putExtra("STATUS_KANBAN", vaga.statusKanban)
        intent.putExtra("DESCRICAO_VAGA", vaga.descricao)
        startActivity(intent)
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
                    rvVagas.adapter = VagaAdapter(tresVagasMaisRecentes, idDoUsuario)
                }
            }
            override fun onFailure(call: Call<List<Vaga>>, t: Throwable) {}
        })
    }

    class VagaAdapter(private val vagas: List<Vaga>, private val idDoUsuario: Long) : RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {
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
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, VagaDetalhesActivity::class.java)
                intent.putExtra("ID_USUARIO", idDoUsuario)
                intent.putExtra("ID_VAGA", vagaAtual.id)
                intent.putExtra("NOME_VAGA", vagaAtual.nomeVaga)
                intent.putExtra("NOME_EMPRESA", vagaAtual.nomeEmpresa)
                intent.putExtra("SALARIO_VAGA", vagaAtual.salario.toDouble())
                intent.putExtra("MODELO_VAGA", vagaAtual.modelo)
                intent.putExtra("STATUS_KANBAN", vagaAtual.statusKanban)
                intent.putExtra("DESCRICAO_VAGA", vagaAtual.descricao)
                context.startActivity(intent)
            }
        }
        override fun getItemCount(): Int = vagas.size
    }
}