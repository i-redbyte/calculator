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

    @Test
    fun `negative numbers test`() {
        val rawInput = "-1 + (-2) * -3"
        val tokens = tokenize(rawInput)
        assertThat(tokens).isNotNull()
        assertThat(tokens).isNotEmpty()

        val expectedTokens = listOf(
            Token(TokenType.MINUS, "-", null),
            Token(TokenType.NUMBER, "1", 1),
            Token(TokenType.PLUS, "+", null),
            Token(TokenType.LEFT_PARENTHESE, "(", null),
            Token(TokenType.MINUS, "-", null),
            Token(TokenType.NUMBER, "2", 2),
            Token(TokenType.RIGHT_PARENTHESE, ")", null),
            Token(TokenType.STAR, "*", null),
            Token(TokenType.MINUS, "-", null),
            Token(TokenType.NUMBER, "3", 3)
        )
        assertThat(tokens).isEqualTo(expectedTokens)
    }

    @Test
    fun `complex expression test`() {
        val rawInput = "((3 + 5) * 2 - 1) / 4"
        val tokens = tokenize(rawInput)
        assertThat(tokens).isNotNull()
        assertThat(tokens).isNotEmpty()

        val expectedTokens = listOf(
            Token(TokenType.LEFT_PARENTHESE, "(", null),
            Token(TokenType.LEFT_PARENTHESE, "(", null),
            Token(TokenType.NUMBER, "3", 3),
            Token(TokenType.PLUS, "+", null),
            Token(TokenType.NUMBER, "5", 5),
            Token(TokenType.RIGHT_PARENTHESE, ")", null),
            Token(TokenType.STAR, "*", null),
            Token(TokenType.NUMBER, "2", 2),
            Token(TokenType.MINUS, "-", null),
            Token(TokenType.NUMBER, "1", 1),
            Token(TokenType.RIGHT_PARENTHESE, ")", null),
            Token(TokenType.DIV, "/", null),
            Token(TokenType.NUMBER, "4", 4)
        )
        assertThat(tokens).isEqualTo(expectedTokens)
    }

    @Test
    fun `empty input test`() {
        val rawInput = ""
        val tokens = tokenize(rawInput)
        assertThat(tokens).isNotNull()
        assertThat(tokens).isEmpty()
    }

    @Test
    fun `large numbers test`() {
        val rawInput = "1234567890 + 987654321"
        val tokens = tokenize(rawInput)
        assertThat(tokens).isNotNull()
        assertThat(tokens).isNotEmpty()

        val expectedTokens = listOf(
            Token(TokenType.NUMBER, "1234567890", 1234567890),
            Token(TokenType.PLUS, "+", null),
            Token(TokenType.NUMBER, "987654321", 987654321)
        )
        assertThat(tokens).isEqualTo(expectedTokens)
    }
}
