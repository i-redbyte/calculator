package expression

import operation.OperandType.*
import operation.Operation
import kotlin.jvm.Throws
import kotlin.math.pow

class BinaryExpression(
    private val left: Expression,
    private val right: Expression,
    private val operation: Operation,
) : Expression {

    @Throws(ParseException::class)
    override fun evaluate(): Int {
        val leftValue = left.evaluate()
        val rightValue = right.evaluate()
        return when (operation.type) {
            PLUS -> leftValue + rightValue
            MINUS -> leftValue - rightValue
            STAR -> leftValue * rightValue
            DIV -> leftValue / rightValue
            POW -> leftValue.toDouble().pow(rightValue.toDouble()).toInt()
            else -> throw ParseException("Unknown binary operator: $operation")
        }
    }

    override fun toString(): String {
        return "$left ${operation.type} $right"
    }
}