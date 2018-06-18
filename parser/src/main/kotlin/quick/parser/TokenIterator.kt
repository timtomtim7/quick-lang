package quick.parser

import quick.lexer.Token
import quick.lexer.TokenType

class TokenIterator(val tokens: List<Token>) : Iterator<Token> {

    var currentIndex = 0
    lateinit var currentToken: Token

    override fun hasNext(): Boolean {
        return currentIndex < tokens.size
    }

    fun hasPrevious(): Boolean {
        return currentIndex > 0
    }

    fun previous(): Token {
        if (!hasPrevious())
            throw NoSuchElementException("HOW IN THU FUCK MAN")
        currentToken = tokens[--currentIndex]
        return currentToken
    }

    override fun next(): Token {
        if (!hasNext())
            throw NoSuchElementException("YA CRIKEY DONE IT AGAIN MATE")

        currentToken = tokens[currentIndex++]
        return currentToken
    }

    inline fun <reified T: TokenType> nextOptional(): Token? {
        if (!hasNext())
            throw NoSuchElementException("YA DON FUKED UP MORE")

        val next = next()

        if (next.type !is T)
            return null

        return next
    }
}