import arrow.core.Either
import com.google.common.truth.Truth
import operation.Operation
import org.junit.Test

class InputTest {

    /** Тупой тест, просто, чтобы показать подход given/when/then */
    @Test
    fun `general test`() {
        // given valid raw input
        val rawInput = "(1^2 + 3^3) / 4"

        // when creating Input
        val input = Input(rawInput)

        // assert object is created
        Truth.assertThat(input).isNotNull()
    }

    @Test
    fun `invalid symbol test`() {
        val invalidInput = "clear"

        val result: Either<Throwable, List<Operation>> = try {
            val r: List<Operation> = Input(invalidInput).scanOperations()
            Either.right(r)
        } catch (e: UnknownCharacterException) {
            Either.left(e)
        }

        Truth.assertThat(result is Either.Left)
    }

    // TODO: докинуть тестов, чтобы покрыть весь [Input]
}