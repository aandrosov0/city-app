package aandrosov.city.data

import aandrosov.city.data.models.ArticleBlockModel
import aandrosov.city.data.models.ArticleHeadlineModel
import aandrosov.city.data.models.ArticleParagraphModel
import aandrosov.city.data.models.ArticleSubheadlineModel
import aandrosov.city.data.models.ArticleThumbnailModel
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.Heading
import org.commonmark.node.Image
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text

internal class ArticleRenderer {
    class DocumentVisitor(private val onAppendBlock: (ArticleBlockModel) -> Unit) : AbstractVisitor() {
        override fun visit(heading: Heading?) {
            heading?.let {
                val level = heading.level
                val text = (heading.firstChild as Text?)?.literal ?: ""
                val block = when (level) {
                    1 -> ArticleHeadlineModel(text)
                    3 -> ArticleSubheadlineModel(text)
                    else -> throw IllegalArgumentException("Header level $level isn't applicable")
                }
                onAppendBlock(block)
            }
        }

        override fun visit(paragraph: Paragraph?) {
            val paragraphVisitor = ParagraphVisitor(onAppendBlock)
            paragraph?.accept(paragraphVisitor)
        }
    }

    private class ParagraphVisitor(private val onAppendBlock: (ArticleBlockModel) -> Unit) : AbstractVisitor() {
        private val stringBuilder = StringBuilder()

        override fun visit(paragraph: Paragraph?) {
            visitChildren(paragraph)

            if (stringBuilder.isNotBlank()) {
                onAppendBlock(
                    ArticleParagraphModel(
                        stringBuilder.toString()
                    )
                )
            }
        }

        override fun visit(image: Image?) {
            if (stringBuilder.isNotBlank()) {
                onAppendBlock(
                    ArticleParagraphModel(
                        stringBuilder.toString()
                    )
                )
            }
            stringBuilder.clear()
            image?.let {
                onAppendBlock(
                    ArticleThumbnailModel(
                        url = it.destination,
                        caption = it.title ?: ""
                    )
                )
            }
        }

        override fun visit(strongEmphasis: StrongEmphasis?) {
            stringBuilder.append("<b>")
            strongEmphasis?.firstChild?.accept(this)
            stringBuilder.append("</b>")
        }

        override fun visit(text: Text?) {
            stringBuilder.append(text?.literal ?: "")
        }
    }

    fun render(document: Node): List<ArticleBlockModel> {
        val blocks = mutableListOf<ArticleBlockModel>()
        val documentVisitor = DocumentVisitor(blocks::add)
        document.accept(documentVisitor)
        return blocks
    }
}