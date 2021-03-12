import operation.OperandType
import operation.OperandType.*
import operation.Operation

// FIXME: 11.03.2021 Rename class
class Input(
    private val input: String
) {
    private var start = 0
    private var current = 0
    private val scannedTokens = mutableListOf<Operation>()
    private val currentSubstring: String
        get() = input.substring(start, current)
    private val isAtEnd: Boolean
        get() = current >= input.length

    // FIXME: при невалидном инпуте кидает ошибку. Тут либо @Throws, либо оборачивать.
    fun scanOperations(): List<Operation> {
        while (isAtEnd.not()) {
            start = current
            scanOperation()
        }
        return scannedTokens
    }

    private fun scanOperation() {
        when (val symbol = advance()) {
            ' ', '\t', '\r', '\n' -> Unit
            '(' -> addOperation(LEFT_PARENTHESE)
            ')' -> addOperation(RIGHT_PARENTHESE)
            '+' -> addOperation(PLUS)
            '-' -> addOperation(MINUS)
            '*' -> addOperation(STAR)
            '/' -> addOperation(DIV)
            '^' -> addOperation(POW)
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
        addOperation(NUMBER, currentSubstring.toInt())
    }

    private fun addOperation(type: OperandType) {
        addOperation(type, null)
    }

    private fun addOperation(type: OperandType, value: Any?) {
        scannedTokens.add(newToken(type, value))
    }

    private fun newToken(type: OperandType, value: Any?): Operation {
        return Operation(type, currentSubstring, value)
    }

}

class UnknownCharacterException(symbol: Char) :
    RuntimeException("Unknown symbol in input string: $symbol")