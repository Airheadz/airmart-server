package xyz.notarealtree.airmart.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CharacterResponse(
        val CharacterID: Long,
        val CharacterName: String,
        val ExpiresOn: String,
        val Scopes: String,
        val TokenType: String,
        val CharacterOwnerHash: String
)