package xyz.notarealtree.airmart.model

data class CharacterResponse(
        val CharacterID: Long,
        val CharacterName: String,
        val ExpiresOn: String,
        val Scopes: String,
        val TokenType: String,
        val CharacterOwnerHash: String
)