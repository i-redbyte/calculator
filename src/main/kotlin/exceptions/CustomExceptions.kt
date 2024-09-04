package exceptions

class UnknownCharacterException(symbol: Char) :
    RuntimeException("Unknown symbol in input string: $symbol")

class ParseException(message: String) : RuntimeException(message)