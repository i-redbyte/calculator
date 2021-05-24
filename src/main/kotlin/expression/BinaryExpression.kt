package expression

import operation.TokenType.*
import operation.Token
import kotlin.jvm.Throws
import kotlin.math.pow

class BinaryExpression(
    private val left: Expression,
    private val right: Expression,
    private val token: Token,
) : Expression {

    @Throws(ParseException::class)
    override fun evaluate(): Int {
        val leftValue = left.evaluate()
        val rightValue = right.evaluate()
        return when (token.type) {
            PLUS -> leftValue + rightValue
            MINUS -> leftValue - rightValue
            STAR -> leftValue * rightValue
            DIV -> leftValue / rightValue
            POW -> leftValue.toDouble().pow(rightValue.toDouble()).toInt()
            else -> throw ParseException("Unknown binary operator: $token")
        }
    }

    override fun toString(): String {
        return "$left ${token.type} $right"
    }
}