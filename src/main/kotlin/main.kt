import java.lang.Exception

fun main(args: Array<String>) {
    println("Enter an empty line to exit")
    print(">>> ")
    var inputString = readLine()
    while (inputString.isNullOrEmpty().not()) {
        try {
            val operations = Input(inputString.orEmpty()).scanOperations()
            val result = Parser(operations).parse()
            println("Result: ${result.evaluate()}")
        } catch (exception: Exception) {
            println("Ooh! Fail: ${exception.message}")
        }
        print(">>> ")
        inputString = readLine()
    }
    println("Good bye!")
}