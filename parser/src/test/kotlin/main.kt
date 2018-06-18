import quick.lexer.Lexer

fun main(args: Array<String>) {
    val code = """

        main() {
            number = 544
            tom = "1024"
            haha = true
        }

        more() Rectangle {
            return new Rectangle()
        }

    """.trimIndent().lines().filter(String::isNotBlank)

    val tokens = Lexer.lex(code)
}