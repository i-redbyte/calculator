package lexer

data class Token(
    val type: TokenType,
    val lexeme: String,
    val value: Any?,
)

enum class TokenType {
    NUMBER,
    LEFT_PARENTHESE,
    RIGHT_PARENTHESE,
    PLUS,
    MINUS,
    STAR,
    DIV,
    POW,
}