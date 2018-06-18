package quick.lexer


sealed class TokenType {
    abstract fun parse(text: TextIterator): Token

    companion object {
        val types = TokenKeyword.types + listOf(
                BooleanLiteral,
                Identifier,
                Symbol,
                StringLiteral,
                FloatLiteral,
                IntLiteral
        )
    }

    override fun toString(): String {
        return javaClass.simpleName
    }
}

sealed class TokenKeyword(val str: String) : TokenType() {

    override fun parse(text: TextIterator): Token {

        val pos = text.pos
        val value = text.takeWhile(Char::isLetterOrDigit)

        if (value != str)
            throw LexerException()

        return Token(this, value, pos)

    }

    companion object {
        val types = listOf(
                ReturnKeyword,
                IfKeyword,
                PublicKeyword,
                PrivateKeyword,
                ProtectedKeyword,
                NewKeyword,
                MutableKeyword
        )
    }
}

object ReturnKeyword : TokenKeyword("return")
object IfKeyword : TokenKeyword("if")
object PublicKeyword : TokenKeyword("public")
object PrivateKeyword : TokenKeyword("private")
object ProtectedKeyword : TokenKeyword("protected")
object NewKeyword : TokenKeyword("new")
object MutableKeyword : TokenKeyword("mutable")

sealed class TokenLiteral : TokenType()

object Identifier : TokenType() {
    override fun parse(text: TextIterator): Token {
        val start = text.pos
        val first = text.next()

        if (!first.isLetter())
            throw LexerException()

        val tokenString = first + text.takeWhile(Char::isLetterOrDigit)

        return Token(this, tokenString, start)
    }
}

object Symbol : TokenType() {

    private val symbols = "(){}[]=-+;:/*"

    override fun parse(text: TextIterator): Token {
        val pos = text.pos
        val symbol = text.next()
        if (symbol !in symbols)
            throw LexerException()

        return Token(this, symbol.toString(), pos)

    }

}

object BooleanLiteral : TokenLiteral() {
    override fun parse(text: TextIterator): Token {
        val pos = text.pos
        val value = text.takeWhile(Char::isLetterOrDigit)

        if (value != "true" && value != "false")
            throw LexerException()

        return Token(this, value, pos)
    }

}

object StringLiteral : TokenLiteral() {
    override fun parse(text: TextIterator): Token {
        val pos = text.pos
        val first = text.next()

        if (first != '"')
            throw LexerException()

        val value = StringBuilder()

        var isEscaped = false

        while (true) {
            val next = text.next()

            if (!isEscaped) {
                if (next == '"')
                    break
                if (next == '\\')
                    isEscaped = true
            }

            value.append(next)
        }

        return Token(this, value.toString(), pos)
    }

}

object IntLiteral : TokenLiteral() {
    override fun parse(text: TextIterator): Token {
        val pos = text.pos
        val first = text.next()

        if (first != '-' && !first.isDigit())
            throw LexerException()

        val digit = first + text.takeWhile { it.isDigit() }

        return Token(this, digit, pos)
    }

}

object FloatLiteral : TokenLiteral() {
    override fun parse(text: TextIterator): Token {
        val pos = text.pos
        val thing = text.takeWhile { it in "-+e." || it.isDigit() }

        try {
            thing.toDouble()
        } catch (ex: NumberFormatException) {
            throw LexerException()
        }

        if (text.next() != 'f') {
            text.previous()
            try {
                thing.toLong()

                return Token(IntLiteral, thing, pos)
            } catch (ex: NumberFormatException) {
            }
        }

        return Token(this, thing, pos)
    }
}