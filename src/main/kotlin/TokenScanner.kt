import operation.TokenType
import operation.TokenType.*
import operation.Token
import kotlin.jvm.Throws

class TokenScanner(
    private val input: String
) {
    private var start = 0
    private var current = 0
    private val scannedTokens = mutableListOf<Token>()
    private val currentSubstring: String
        get() = input.substring(start, current)
    private val isAtEnd: Boolean
        get() = current >= input.length

    @Throws
    fun scanOperations(): List<Token> {
        while (isAtEnd.not()) {
            start = current
            scan()
        }
        return scannedTokens
    }

    private fun scan() {
        when (val symbol = advance()) {
            ' ', '\t', '\r', '\n' -> Unit
            '(' -> addToken(LEFT_PARENTHESE)
            ')' -> addToken(RIGHT_PARENTHESE)
            '+' -> addToken(PLUS)
            '-' -> addToken(MINUS)
            '*' -> addToken(STAR)
            '/' -> addToken(DIV)
            '^' -> addToken(POW)
            else ->
                if (Character.isDigit(symbol)) {
                    addNumber()
                } else {
                    throw UnknownCharacterException(peek())
                }
        }
    }

    private fun advance(): Char {
        return input[current++]
    }

    private fun peek(): Char {
        return input[current]
    }

    private fun addNumber() {
        while (isAtEnd.not() && Character.isDigit(peek())) {
            advance()
        }
        addToken(NUMBER, currentSubstring.toInt())
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, value: Any?) {
        scannedTokens.add(newToken(type, value))
    }

    private fun newToken(type: TokenType, value: Any?): Token {
        return Token(type, currentSubstring, value)
    }

}

class UnknownCharacterException(symbol: Char) :
    RuntimeException("Unknown symbol in input string: $symbol")