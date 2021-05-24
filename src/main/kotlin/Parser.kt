import expression.*
import operation.TokenType
import operation.TokenType.*
import operation.Token
import kotlin.jvm.Throws

class Parser(private val tokens: List<Token>) {
    private var current = 0

    fun parse(): Expression {
        return add()
    }

    private fun add(): Expression {
        return parseBinary({ mul() }, { mul() }, PLUS, MINUS)
    }

    private fun mul(): Expression {
        return parseBinary({ pow() }, { pow() }, STAR, DIV)
    }

    private fun pow(): Expression {
        return parseBinary({ group() }, { pow() }, POW)
    }

    @Throws(ParseException::class)
    private fun parseBinary(
        startExpression: () -> Expression,
        repeatingExpression: () -> Expression,
        vararg types: TokenType
    ): Expression {
        var expression: Expression = startExpression()
        while (match(*types)) {
            if (isEnd()) {
                throw ParseException("Missing right-hand side(RHS) of binary expression.")
            }
            val p = previous()
            expression = BinaryExpression(expression, repeatingExpression(), p)
        }
        return expression
    }

    @Throws(ParseException::class)
    private fun group(): Expression {
        if (match(NUMBER)) return NumberLiteral(previous())
        if (match(LEFT_PARENTHESE)) {
            val expr: Expression = parse()
            if (match(RIGHT_PARENTHESE).not()) throw ParseException(" ) not found.")
            return GroupingExpression(expr)
        }
        throw ParseException("Number or parenthesis expected " + peek().lexeme)
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean =
        if (isEnd()) false
        else peek().type == type

    private fun isEnd(): Boolean = current >= tokens.size

    private fun advance(): Token = tokens[current++]

    private fun peek(): Token = tokens[current]

    private fun previous(): Token = tokens[current - 1]
}