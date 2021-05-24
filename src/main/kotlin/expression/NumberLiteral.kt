package expression

import operation.Token

class NumberLiteral(private val token: Token) : Expression {

    override fun evaluate(): Int = token.value as Int

    override fun toString(): String {
        return "${(token.value as Int)}"
    }
}