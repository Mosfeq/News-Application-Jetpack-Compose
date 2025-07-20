package com.example.newsapplicationjetpackcompose.domain.model

import java.util.UUID

data class Source(
    val id: String? = null,
    val name: String? = null,
    val uuid: String = UUID.randomUUID().toString()
)