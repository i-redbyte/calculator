package expression

class GroupingExpression(private val expression: Expression) : Expression {

    override fun evaluate(): Int = expression.evaluate()

}