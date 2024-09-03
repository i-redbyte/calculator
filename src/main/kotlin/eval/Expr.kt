package eval

import kotlin.math.pow

sealed class Expr {
    data class Binary(val left: Expr, val right: Expr, val op: Op) : Expr()
    data class Grouping(val expr: Expr) : Expr()
    data class Literal(val value: Int) : Expr()
}

sealed class Op {
    object Plus : Op()
    object Minus : Op()
    object Multiply : Op()
    object Divide : Op()
    object Power : Op()
}

fun evaluate(expr: Expr): Int = when (expr) {
    is Expr.Literal -> expr.value
    is Expr.Grouping -> evaluate(expr.expr)
    is Expr.Binary -> evaluateBinary(expr.left, expr.right, expr.op)
}

fun evaluateBinary(left: Expr, right: Expr, op: Op): Int {
    val leftValue = evaluate(left)
    val rightValue = evaluate(right)
    return when (op) {
        Op.Plus -> leftValue + rightValue
        Op.Minus -> leftValue - rightValue
        Op.Multiply -> leftValue * rightValue
        Op.Divide -> leftValue / rightValue
        Op.Power -> leftValue.toDouble().pow(rightValue.toDouble()).toInt()
    }
}
