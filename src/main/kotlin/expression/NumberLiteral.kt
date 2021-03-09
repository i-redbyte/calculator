package expression

import operation.Operation

class NumberLiteral(private val operation: Operation) : Expression {
    override fun evaluate(): Int = operation.value as Int
    override fun toString(): String {
        return "${(operation.value as Int)}"
    }
}