package parser

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import eval.*
import exceptions.ParseException
import lexer.*

fun TokenType.toOp(): Either<ParseException, Op> = when (this) {
    TokenType.PLUS -> Op.Plus.right()
    TokenType.MINUS -> Op.Minus.right()
    TokenType.STAR -> Op.Multiply.right()
    TokenType.DIV -> Op.Divide.right()
    TokenType.POW -> Op.Power.right()
    else -> ParseException("Unknown operator: $this").left()
}

fun parseBinary(
    nextPrecedence: (List<Token>) -> Either<ParseException, Pair<Expr, List<Token>>>,
    tokens: List<Token>,
    ops: List<TokenType>
): Either<ParseException, Pair<Expr, List<Token>>> {
    return nextPrecedence(tokens).flatMap { (initialExpr, remainingTokens) ->
        processBinaryExpression(initialExpr, remainingTokens, nextPrecedence, ops)
    }
}

fun processBinaryExpression(
    expr: Expr,
    remainingTokens: List<Token>,
    nextPrecedence: (List<Token>) -> Either<ParseException, Pair<Expr, List<Token>>>,
    ops: List<TokenType>
): Either<ParseException, Pair<Expr, List<Token>>> {
    if (remainingTokens.isEmpty() || remainingTokens.first().type !in ops) {
        return Either.Right(expr to remainingTokens)
    }

    val opEither = remainingTokens.first().type.toOp()
    return opEither.flatMap { operator ->
        val newRemainingTokens = remainingTokens.drop(1)
        nextPrecedence(newRemainingTokens).flatMap { (rightExpr, newTokensAfterRight) ->
            processBinaryExpression(
                Expr.Binary(expr, rightExpr, operator),
                newTokensAfterRight,
                nextPrecedence,
                ops
            )
        }
    }
}

fun primary(tokens: List<Token>): Either<ParseException, Pair<Expr, List<Token>>> {
    return tokens.firstOrNull()?.let { token ->
        when (token.type) {
            TokenType.NUMBER -> {
                Either.Right(Expr.Literal(token.value as Int) to tokens.drop(1))
            }
            TokenType.LEFT_PARENTHESE -> {
                parse(tokens.drop(1)).flatMap { (expr, remainingTokens) ->
                    remainingTokens.firstOrNull()?.takeIf { it.type == TokenType.RIGHT_PARENTHESE }
                        ?.let {
                            Either.Right(Expr.Grouping(expr) to remainingTokens.drop(1))
                        }
                        ?: Either.Left(ParseException("Expected ')' after expression."))
                }
            }
            TokenType.MINUS -> {
                primary(tokens.drop(1)).map { (rightExpr, remainingTokens) ->
                    Expr.Unary(Op.Negate, rightExpr) to remainingTokens
                }
            }
            else -> Either.Left(ParseException("Unexpected token: $token"))
        }
    } ?: Either.Left(ParseException("Unexpected end of input"))
}

fun pow(tokens: List<Token>): Either<ParseException, Pair<Expr, List<Token>>> =
    parseBinary(::primary, tokens, listOf(TokenType.POW))

fun mul(tokens: List<Token>): Either<ParseException, Pair<Expr, List<Token>>> =
    parseBinary(::pow, tokens, listOf(TokenType.STAR, TokenType.DIV))

fun add(tokens: List<Token>): Either<ParseException, Pair<Expr, List<Token>>> =
    parseBinary(::mul, tokens, listOf(TokenType.PLUS, TokenType.MINUS))

fun parse(tokens: List<Token>): Either<ParseException, Pair<Expr, List<Token>>> {
    return add(tokens)
}
