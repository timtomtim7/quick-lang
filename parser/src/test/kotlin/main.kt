import quick.lexer.*

fun main(args: Array<String>) {
	val code = """
		main {
			String name = "Tom1024"
			infer test = true
			if(test == true) {
				println("Hey!")
			}
			more()
		}

		Rectangle(public Int x, y, width, height)

		Rectangle more {
			return new Rectangle()
		}

    """.trimIndent()

	val tokens = Lexer.lex(code)
	tokens.filter { it.type != TokenType.NewLine }.forEach(::println)
	PrettyPrint.print(code, tokens)
}