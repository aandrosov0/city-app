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
import org.commonmark.node.Text

internal class ArticleRenderer {
    class Visitor(private val onAppendBlock: (ArticleBlockModel) -> Unit) : AbstractVisitor() {
        override fun visit(heading: Heading?) {
            heading?.let {
                val level = heading.level
                val text = (heading.firstChild as Text?)?.literal ?: ""
                val block = when (level) {
                    1 -> ArticleHeadlineModel(text)
                    3 -> ArticleSubheadlineModel(text)
                    else -> throw IllegalArgumentException("Header level $level isn't applicable")
                }
                println(block)
                onAppendBlock(block)
            }
        }

        override fun visit(image: Image?) {
            image?.let {
                println(image)
                onAppendBlock(
                    ArticleThumbnailModel(
                        url = it.destination,
                        caption = it.title ?: ""
                    )
                )
            }
        }

        override fun visit(text: Text?) {
            text?.let {
                println(text)
                onAppendBlock(ArticleParagraphModel(text.literal))
            }
        }
    }

    fun render(document: Node): List<ArticleBlockModel> {
        val blocks = mutableListOf<ArticleBlockModel>()
        val visitor = Visitor(blocks::add)
        document.accept(visitor)
        return blocks
    }
}