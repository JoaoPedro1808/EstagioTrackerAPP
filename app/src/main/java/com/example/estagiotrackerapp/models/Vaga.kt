package com.example.estagiotrackerapp.models

import android.icu.util.LocaleData
import java.math.BigDecimal
import java.time.LocalDate

data class Vaga(
    val id: Long,
    val nomeEmpresa: String,
    val nomeVaga: String,
    val salario: BigDecimal,
    val statusKanban: String,
    val modelo: String,
    val dataAplicacao: LocalDate,
    val descricao: String,
    val usuarioId: Long
)