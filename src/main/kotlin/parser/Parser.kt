package parser

import lexer.*
import eval.*
import exceptions.ParseException

fun TokenType.toOp(): Op = when (this) {
    TokenType.PLUS -> Op.Plus
    TokenType.MINUS -> Op.Minus
    TokenType.STAR -> Op.Multiply
    TokenType.DIV -> Op.Divide
    TokenType.POW -> Op.Power
    else -> throw ParseException("Unknown operator: $this")
}

fun parseBinary(
    nextPrecedence: (List<Token>) -> Pair<Expr, List<Token>>,
    tokens: List<Token>,
    ops: List<TokenType>
): Pair<Expr, List<Token>> {
    tailrec fun processBinaryExpression(
        expr: Expr,
        remainingTokens: List<Token>
    ): Pair<Expr, List<Token>> {
        if (remainingTokens.isEmpty() || remainingTokens.first().type !in ops) {
            return expr to remainingTokens
        }
        val op = remainingTokens.first().type.toOp()
        val newRemainingTokens = remainingTokens.drop(1)
        val (rightExpr, newTokensAfterRight) = nextPrecedence(newRemainingTokens)
        val newExpr = Expr.Binary(expr, rightExpr, op)
        return processBinaryExpression(newExpr, newTokensAfterRight)
    }

    val (initialExpr, remainingTokens) = nextPrecedence(tokens)
    return processBinaryExpression(initialExpr, remainingTokens)
}

fun primary(tokens: List<Token>): Pair<Expr, List<Token>> =
    tokens.firstOrNull()?.let { token ->
        when (token.type) {
            TokenType.NUMBER -> Expr.Literal(token.value as Int) to tokens.drop(1)
            TokenType.LEFT_PARENTHESE -> parse(tokens.drop(1))
                .let { (expr, remainingTokens) ->
                    remainingTokens.firstOrNull()?.takeIf { it.type == TokenType.RIGHT_PARENTHESE }
                        ?.let { Expr.Grouping(expr) to remainingTokens.drop(1) }
                        ?: throw ParseException("Expected ')' after expression.")
                }
            TokenType.MINUS -> {
                val (rightExpr, remainingTokens) = primary(tokens.drop(1))
                Expr.Unary(Op.Negate, rightExpr) to remainingTokens
            }
            else -> throw ParseException("Unexpected token: $token")
        }
    } ?: throw ParseException("Unexpected end of input")

fun pow(tokens: List<Token>): Pair<Expr, List<Token>> = parseBinary(::primary, tokens, listOf(TokenType.POW))

fun mul(tokens: List<Token>): Pair<Expr, List<Token>> =
    parseBinary(::pow, tokens, listOf(TokenType.STAR, TokenType.DIV))

fun add(tokens: List<Token>): Pair<Expr, List<Token>> =
    parseBinary(::mul, tokens, listOf(TokenType.PLUS, TokenType.MINUS))

fun parse(tokens: List<Token>): Pair<Expr, List<Token>> {
    return add(tokens)
}
