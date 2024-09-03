package lexer

import exceptions.UnknownCharacterException

fun tokenize(input: String): List<Token> {

    fun scanToken(chars: List<Char>): Pair<Token, Int> {
        val first = chars.first()
        return when {
            first.isDigit() -> {
                val number = chars.takeWhile { it.isDigit() }.joinToString("")
                Token(TokenType.NUMBER, number, number.toInt()) to number.length
            }

            first == '+' -> Token(TokenType.PLUS, "+", null) to 1
            first == '-' -> Token(TokenType.MINUS, "-", null) to 1
            first == '*' -> Token(TokenType.STAR, "*", null) to 1
            first == '/' -> Token(TokenType.DIV, "/", null) to 1
            first == '^' -> Token(TokenType.POW, "^", null) to 1
            first == '(' -> Token(TokenType.LEFT_PARENTHESE, "(", null) to 1
            first == ')' -> Token(TokenType.RIGHT_PARENTHESE, ")", null) to 1
            else -> throw UnknownCharacterException(first)
        }
    }

    fun scanTokens(chars: List<Char>, start: Int = 0): List<Token> = when {
        chars.isEmpty() -> emptyList()
        chars.first().isWhitespace() -> scanTokens(chars.drop(1), start + 1)
        else -> {
            val (token, length) = scanToken(chars)
            listOf(token) + scanTokens(chars.drop(length), start + length)
        }
    }

    return scanTokens(input.toList())
}
