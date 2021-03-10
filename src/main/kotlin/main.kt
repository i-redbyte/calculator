fun main(args: Array<String>) {
    print(">>> ")
    var inputString = readLine()
    while (inputString.isNullOrEmpty().not()) {
        val operations = Input(inputString.orEmpty()).scanOperations()
        val result = Parser(operations).parse()
        println("Result: ${result.evaluate()}")
        print(">>> ")
        inputString = readLine()
    }
    println("Good bye!")
}