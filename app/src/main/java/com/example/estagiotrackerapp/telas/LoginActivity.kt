package com.example.estagiotrackerapp.telas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estagiotrackerapp.R
import com.example.estagiotrackerapp.api.RetrofitClient
import com.example.estagiotrackerapp.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etSenha = findViewById<EditText>(R.id.etSenha)
        val btnGmail = findViewById<Button>(R.id.btnGmail)
        val tvCadastreSe = findViewById<TextView>(R.id.tvCadastreSe)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val email = etUsuario.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Preencha todos o campo de email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha.isEmpty()) {
                Toast.makeText(this, "Preencha o campo de senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.api.listarTodos().enqueue(object : Callback<List<Usuario>> {
                override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                    if (response.isSuccessful) {
                        val listaUsuarios = response.body()
                        val usuarioEncontrado = listaUsuarios?.find { it.email == email && it.senha == senha }

                        if (usuarioEncontrado != null) {
                            Toast.makeText(this@LoginActivity, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }

        tvCadastreSe.setOnClickListener {
            val intent = Intent(this@LoginActivity, CadastroActivity::class.java)
            startActivity(intent);
        }

        btnGmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://accounts.google.com/"))
            startActivity(intent)
        }
    }
}