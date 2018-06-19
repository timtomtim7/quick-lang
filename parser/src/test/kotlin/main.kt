import quick.lexer.Lexer
import quick.lexer.PrettyPrint

fun main(args: Array<String>) {
	val code = """
		main {
			Int number = 544
			String tom = "1024"
			Boolean haha = true
			more()
		}

		Rectangle(public Int x, y, width, height)

		Rectangle more {
			return new Rectangle()
		}

    """.trimIndent()

	val tokens = Lexer.lex(code)
	PrettyPrint.print(code, tokens)
}