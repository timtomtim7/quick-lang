import quick.lexer.ColorizedPrinter
import quick.lexer.Lexer

fun main(args: Array<String>) {
	val fileName = "test.qik"
	val code = ClassLoader.getSystemResource(fileName)
			.readText()
			.replace("\t", " ".repeat(3))

	val tokens = Lexer.lex(code)
	ColorizedPrinter.printError(fileName, code, tokens, tokens[8], "Unexpected identifier")
	ColorizedPrinter.printError(fileName, code, tokens, tokens[9], "Unexpected identifier")
	ColorizedPrinter.printError(fileName, code, tokens, tokens[10], "Unexpected identifier")
	ColorizedPrinter.printError(fileName, code, tokens, tokens[11], "Unexpected identifier")
	ColorizedPrinter.printError(fileName, code, tokens, tokens[53], "Unexpected boolean literal")

//	tokens.filter { it.type != TokenType.NewLine }.forEach(::println)
//	ColorizedPrinter.print(code, tokens)
}