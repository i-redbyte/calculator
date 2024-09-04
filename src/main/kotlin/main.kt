import eval.evaluate
import lexer.tokenize
import parser.parse
import arrow.core.flatMap

fun main() {
    println("Enter an empty line to exit")

    generateSequence {
        print(">>> ")
        readlnOrNull()
    }
        .takeWhile { it.isNotBlank() }
        .map { input -> tokenize(input) }
        .map { tokens -> parse(tokens) }
        .map { eitherExpr ->
            eitherExpr.flatMap { (expr, _) -> evaluate(expr) }
        }
        .forEach { result ->
            result.fold(
                ifLeft = { error -> println("Error: ${error.message}") },
                ifRight = { value -> println("Result: $value") }
            )
        }

    println("Goodbye!")
}
