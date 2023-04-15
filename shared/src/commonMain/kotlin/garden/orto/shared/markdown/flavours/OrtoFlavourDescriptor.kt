package garden.orto.shared.markdown.flavours

import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.space.lexer._SFMLexer
import org.intellij.markdown.html.makeXssSafeDestination
import org.intellij.markdown.lexer.MarkdownLexer

/**
 * Orto Markdown based flavour, to be used as a base for other flavours.
 *
 * @param useSafeLinks `true` if all rendered links should be checked for XSS and `false` otherwise.
 * See [makeXssSafeDestination]
 */
class OrtoFlavourDescriptor(useSafeLinks: Boolean = true) : GFMFlavourDescriptor(useSafeLinks) {
    override fun createInlinesLexer(): MarkdownLexer {
        return MarkdownLexer(_SFMLexer())
    }
}