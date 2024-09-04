import eval.evaluate
import lexer.tokenize
import parser.parse

fun main() {
    println("Enter an empty line to exit")
    generateSequence {
        print(">>> ")
        readLine()
    }.takeWhile { it.isNotBlank() }
        .map { input -> tokenize(input) }
        .map { tokens -> parse(tokens).first }
        .map { expr -> evaluate(expr) }
        .forEach { result -> println("Result: $result") }
    println("Goodbye!")
}