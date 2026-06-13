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

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val etNome = findViewById<EditText>(R.id.etNome)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etSenha = findViewById<EditText>(R.id.etSenha)
        val etComfirmaSenha = findViewById<EditText>(R.id.etConfirmarSenha)
        val btnGmail = findViewById<Button>(R.id.btnGmail)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()
            val comfirmaSenha = etComfirmaSenha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || comfirmaSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != comfirmaSenha) {
                Toast.makeText(this, "As senhas tem que ser iguais", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.api.cadastrarUsuario(nome, email, senha).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CadastroActivity, "Conta cadastrada", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CadastroActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@CadastroActivity, "Erro ao cadastrar conta", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    android.util.Log.e("API_CADASTRADO", "Erro", t)
                    Toast.makeText(this@CadastroActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnGmail.setOnClickListener {
            val intentGoogle = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://accounts.google.com/"))
            startActivity(intentGoogle)
        }
    }
}