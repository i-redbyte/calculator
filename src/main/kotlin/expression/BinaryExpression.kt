package expression

import operation.OperandType
import operation.OperandType.*
import kotlin.jvm.Throws
import kotlin.math.pow

class BinaryExpression(
    private val left: Expression,
    private val right: Expression,
    private val operand: OperandType,
) : Expression {

    @Throws(ParseException::class)
    override fun evaluate(): Int {
        val leftValue = left.evaluate()
        val rightValue = right.evaluate()
        return when (operand) {
            PLUS -> leftValue + rightValue
            MINUS -> leftValue - rightValue
            STAR -> leftValue * rightValue
            SLASH -> leftValue / rightValue
            EXP -> leftValue.toDouble().pow(rightValue.toDouble()).toInt()
            else -> throw ParseException("Unknown binary operator: $operand")
        }
    }

}