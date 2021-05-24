package operation

data class Token(
    val type: TokenType,
    val lexeme: String,
    val value: Any?,
)