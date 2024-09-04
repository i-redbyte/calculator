package eval

import arrow.core.Either
import arrow.core.right
import arrow.core.flatMap
import exceptions.ParseException
import kotlin.math.pow

sealed class Expr {
    data class Binary(val left: Expr, val right: Expr, val op: Op) : Expr()
    data class Grouping(val expr: Expr) : Expr()
    data class Literal(val value: Int) : Expr()
    data class Unary(val op: Op, val right: Expr) : Expr()
}

sealed class Op {
    object Plus : Op()
    object Minus : Op()
    object Multiply : Op()
    object Divide : Op()
    object Power : Op()
    object Negate : Op()
}

fun evaluate(expr: Expr): Either<ParseException, Int> = when (expr) {
    is Expr.Literal -> expr.value.right()
    is Expr.Grouping -> evaluate(expr.expr)
    is Expr.Binary -> evaluateBinary(expr.left, expr.right, expr.op)
    is Expr.Unary -> evaluateUnary(expr.op, expr.right)
}

fun evaluateBinary(left: Expr, right: Expr, op: Op): Either<ParseException, Int> {
    return evaluate(left).flatMap { leftValue ->
        evaluate(right).flatMap { rightValue ->
            when (op) {
                Op.Plus -> Either.Right(leftValue + rightValue)
                Op.Minus -> Either.Right(leftValue - rightValue)
                Op.Multiply -> Either.Right(leftValue * rightValue)
                Op.Divide -> Either.Right(leftValue / rightValue)
                Op.Power -> Either.Right(leftValue.toDouble().pow(rightValue.toDouble()).toInt())
                else -> Either.Left(ParseException("Unknown binary operator: $op"))
            }
        }
    }
}

fun evaluateUnary(op: Op, right: Expr): Either<ParseException, Int> {
    return evaluate(right).flatMap { rightValue ->
        when (op) {
            Op.Negate -> Either.Right(-rightValue)
            else -> Either.Left(ParseException("Unknown unary operator: $op"))
        }
    }
}