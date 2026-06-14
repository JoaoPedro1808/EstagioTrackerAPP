package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnDeletarConta = findViewById<Button>(R.id.btnDeletarConta)
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navKanban = findViewById<LinearLayout>(R.id.navKanban)

        val idUsuarioLogado = intent.getLongExtra("ID_USUARIO", 0L)

        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        navKanban.setOnClickListener {
            val intent = Intent(this, KanbanActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuarioLogado)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnDeletarConta.setOnClickListener {
            if (idUsuarioLogado != 0L) {
                RetrofitClient.api.deletarUsuario(idUsuarioLogado).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ConfigActivity, "Conta deletada", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ConfigActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ConfigActivity, "Erro ao deletar conta", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ConfigActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@ConfigActivity, "Usuário não identificado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}