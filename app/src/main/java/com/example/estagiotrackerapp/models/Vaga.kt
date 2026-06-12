package com.example.estagiotrackerapp.models

import android.icu.util.LocaleData
import java.math.BigDecimal
import java.time.LocalDate

data class Vaga(
    val id: Long,
    val nome_empresa: String,
    val nome_vaga: String,
    val salario: BigDecimal,
    val status_kanban: String,
    val modelo: String,
    val data_aplicacao: LocalDate,
    val descricao: String,
    val usuario_id: Long
)