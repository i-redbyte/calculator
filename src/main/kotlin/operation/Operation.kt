package operation

data class Operation(
    val type: OperandType,
    val lexeme: String,
    val value: Any?,
)