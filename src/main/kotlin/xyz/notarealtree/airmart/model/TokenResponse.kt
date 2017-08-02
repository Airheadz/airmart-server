package xyz.notarealtree.airmart.model

data class TokenResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: Long,
        val refresh_token: String)