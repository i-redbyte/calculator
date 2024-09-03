import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import exceptions.UnknownCharacterException
import lexer.Token
import lexer.TokenType
import lexer.tokenize

class ScannerTest {

    @Test
    fun `general test`() {
        val rawInput = "(1^2 + 3^3) / 4"
        val tokens = tokenize(rawInput)
        assertThat(tokens).isNotNull()
        assertThat(tokens).isNotEmpty()

        val expectedTokens = listOf(
            Token(TokenType.LEFT_PARENTHESE, "(", null),
            Token(TokenType.NUMBER, "1", 1),
            Token(TokenType.POW, "^", null),
            Token(TokenType.NUMBER, "2", 2),
            Token(TokenType.PLUS, "+", null),
            Token(TokenType.NUMBER, "3", 3),
            Token(TokenType.POW, "^", null),
            Token(TokenType.NUMBER, "3", 3),
            Token(TokenType.RIGHT_PARENTHESE, ")", null),
            Token(TokenType.DIV, "/", null),
            Token(TokenType.NUMBER, "4", 4)
        )
        assertThat(tokens).isEqualTo(expectedTokens)
    }

    @Test
    fun `invalid symbol test`() {
        val invalidInput = "C++"

        val exception = assertThrows<UnknownCharacterException> {
            tokenize(invalidInput)
        }

        assertThat(exception).hasMessageThat().isEqualTo("Unknown symbol in input string: C")
    }
}

