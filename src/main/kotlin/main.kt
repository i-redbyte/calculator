fun main(args: Array<String>) {
    val test = "8 - (6 + 6) * 9"
    val operations = Input(test).scanOperations() // TODO: 10.03.2021 change to input from keyboard
    val result = Parser(operations).parse()
    println("Result: ${result.evaluate()}")
}