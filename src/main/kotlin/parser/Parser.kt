package parser

import lexer.*
import eval.*
import exceptions.ParseException

// Функция для преобразования типа токена в оператор
fun TokenType.toOp(): Op = when (this) {
    TokenType.PLUS -> Op.Plus
    TokenType.MINUS -> Op.Minus
    TokenType.STAR -> Op.Multiply
    TokenType.DIV -> Op.Divide
    TokenType.POW -> Op.Power
    else -> throw ParseException("Unknown operator: $this")
}

// Функция для обработки бинарных операций
fun parseBinary(
    nextPrecedence: (List<Token>) -> Pair<Expr, List<Token>>,
    tokens: List<Token>,
    ops: List<TokenType>
): Pair<Expr, List<Token>> {
    var (expr, remainingTokens) = nextPrecedence(tokens)

    while (remainingTokens.isNotEmpty() && remainingTokens.first().type in ops) {
        val op = remainingTokens.first().type.toOp()
        remainingTokens = remainingTokens.drop(1)
        val (right, newRemainingTokens) = nextPrecedence(remainingTokens)
        expr = Expr.Binary(expr, right, op)
        remainingTokens = newRemainingTokens
    }
    return expr to remainingTokens
}

// Функция для обработки чисел и скобок
fun primary(tokens: List<Token>): Pair<Expr, List<Token>> = when (val token = tokens.firstOrNull()) {
    is Token -> when (token.type) {
        TokenType.NUMBER -> Expr.Literal(token.value as Int) to tokens.drop(1)
        TokenType.LEFT_PARENTHESE -> {
            val (expr, remainingTokens) = parse(tokens.drop(1))
            if (remainingTokens.firstOrNull()?.type != TokenType.RIGHT_PARENTHESE) {
                throw ParseException("Expected ')' after expression.")
            }
            Expr.Grouping(expr) to remainingTokens.drop(1)
        }
        else -> throw ParseException("Unexpected token: $token")
    }
    else -> throw ParseException("Unexpected end of input")
}

// Функция для обработки возведения в степень
fun pow(tokens: List<Token>): Pair<Expr, List<Token>> = parseBinary(::primary, tokens, listOf(TokenType.POW))

// Функция для обработки умножения и деления
fun mul(tokens: List<Token>): Pair<Expr, List<Token>> = parseBinary(::pow, tokens, listOf(TokenType.STAR, TokenType.DIV))

// Функция для обработки сложения и вычитания
fun add(tokens: List<Token>): Pair<Expr, List<Token>> = parseBinary(::mul, tokens, listOf(TokenType.PLUS, TokenType.MINUS))

// Основная функция парсинга
fun parse(tokens: List<Token>): Pair<Expr, List<Token>> {
    return add(tokens)
}

// Вызов в основном коде:
// val (expression, remainingTokens) = parse(tokens)
