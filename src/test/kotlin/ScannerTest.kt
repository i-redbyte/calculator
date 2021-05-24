import arrow.core.Either
import com.google.common.truth.Truth
import operation.Token
import org.junit.Test

class ScannerTest {

    @Test
    fun `general test`() {
        // given valid raw input
        val rawInput = "(1^2 + 3^3) / 4"

        // when creating Input
        val input = TokenScanner(rawInput)

        // assert object is created
        Truth.assertThat(input).isNotNull()
    }

    @Test
    fun `invalid symbol test`() {
        val invalidInput = "C++"

        val result: Either<Throwable, List<Token>> = try {
            val r: List<Token> = TokenScanner(invalidInput).scanOperations()
            Either.right(r)
        } catch (e: UnknownCharacterException) {
            Either.left(e)
        }
        Truth.assertThat(result is Either.Left)
    }

}