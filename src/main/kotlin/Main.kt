class NoteNotFoundException(message: String) : RuntimeException(message)

class NoCommentsException(message: String) : RuntimeException(message)

class CommentNotFoundException(message: String) : RuntimeException(message)

class ItemNotFoundException(message: String) : RuntimeException(message)

open class Item(var id: Int) {
    override fun toString(): String {
        return super.toString()
    }
}


open class CrudService<T : Item> {

    protected var container = mutableListOf<T>()
    protected var counter = 1

    fun add(item: T): T {
        item.id = counter++
        container.add(item)
        return container.last()
    }

    fun get(): MutableList<T> {
        return container
    }

    open fun update(updatedItem: T): Boolean {
        for ((index, item) in container.withIndex()) {
            if (updatedItem.id == item.id) {
                container[index] = updatedItem
                return true
            }
        }
        return false
    }

    open fun delete(itemId: Int): Boolean {
        for ((index, item) in container.withIndex()) {
            if (itemId == item.id) {
                container.removeAt(index)
                return true
            }
        }
        throw ItemNotFoundException("Item with id $itemId not found!")
    }

    open fun clear() {
        container = mutableListOf()
        counter = 1
    }
}

class Note(
    id: Int,
    var title: String,
    var text: String,
    var comments: MutableList<Comment>
) : Item(id) {
    @Override
    override fun toString(): String {
        return "#$id . $title \n" +
                "$text \n" +
                when (comments.isEmpty()) {
                    false -> "$comments"
                    true -> "No comments yet"
                }
    }
}

class Comment(
    id: Int,
    var parentNoteId: Int,
    var deleted: Boolean,
    var text: String
) : Item(id) {
    @Override
    override fun toString(): String {
        return "@$id ($parentNoteId)($deleted). $text \n"

    }
}

class NoteService : CrudService<Note>() {

    private var deletedComments = mutableListOf<Comment>()
    private var commentIdCounter = 1

    @Override
    override fun clear() {
        super.clear()
        deletedComments = mutableListOf<Comment>()
        commentIdCounter = 1
    }

    fun addComment(noteId: Int, comment: Comment): Comment {
        for ((index, note) in container.withIndex()) {
            if (noteId == note.id) {
                comment.id = commentIdCounter++
                comment.parentNoteId = noteId
                container[index].comments += comment
                return comment
            }
        }
        throw NoteNotFoundException("Note with id $noteId not found!")
    }

    fun getNoteById(noteId: Int): Note {
        for ((index, note) in container.withIndex()) {
            if (noteId == note.id) {
                return container[index]
            }
        }
        throw NoteNotFoundException("Note with id $noteId not found!")
    }

    fun getDeletedComments(): MutableList<Comment> {
        return deletedComments
    }

    fun getCommentsByNoteId(noteId: Int): MutableList<Comment> {
        for ((index, note) in container.withIndex()) {
            if (noteId == note.id) {
                when (container[index].comments.isEmpty()) {
                    true -> throw NoCommentsException("No comments found!")
                    false -> return container[index].comments
                }
            }
        }
        throw NoteNotFoundException("Note with id $noteId not found!")
    }

    @Override
    override fun update(updatedNote: Note): Boolean {
        for ((index, note) in container.withIndex()) {
            if (updatedNote.id == note.id) {
                updatedNote.comments = note.comments
                container[index] = updatedNote
                return true
            }
        }
        return false
    }

    fun updateComment(updatedComment: Comment): Boolean {
        for ((index, note) in container.withIndex()) {
            if (updatedComment.parentNoteId == note.id) {
                for ((index, comment) in note.comments.withIndex()) {
                    if (updatedComment.id == note.comments[index].id) {
                        note.comments[index] = updatedComment
                        return true
                    }
                }
                throw CommentNotFoundException("Comment with id ${updatedComment.id} not found!")
            }
        }
        throw NoteNotFoundException("Note with id ${updatedComment.parentNoteId} not found!")
    }

    fun deleteComment(noteId: Int, commentId: Int): Boolean {
        for ((index, note) in container.withIndex()) {
            if (noteId == note.id) {
                val currentNote = index
                for ((index, comment) in container[index].comments.withIndex()) {
                    if (commentId == comment.id && !comment.deleted) {
                        comment.deleted = !comment.deleted
                        deletedComments += comment
                        container[currentNote].comments.removeAt(index)

                        return true
                    }
                }
                throw CommentNotFoundException("Comment with id $commentId not found!")
            }
        }
        throw NoteNotFoundException("Note with id $noteId not found!")
    }

    @Override
    override fun delete(noteId: Int): Boolean {
        for ((index, note) in container.withIndex()) {
            if (noteId == note.id) {
                for ((index, comment) in deletedComments.withIndex()) {
                    if (deletedComments[index].parentNoteId == noteId) {
                        deletedComments.removeAt(index)
                    }
                    if(deletedComments.isEmpty()) {
                        container.removeAt(index)
                        return true
                    }
                }
            }
        }
        throw NoteNotFoundException("Note with id $noteId not found!")
    }

    fun restoreComment(commentId: Int): Boolean {
        for ((index, comment) in deletedComments.withIndex()) {
            if (commentId == comment.id) {
                deletedComments[index].deleted = !deletedComments[index].deleted
                val currentComment = index
                for ((index, note) in container.withIndex()) {
                    if (deletedComments[currentComment].parentNoteId == note.id) {
                        container[index].comments += comment
                        return true
                    }
                }
            }
        }
        throw CommentNotFoundException("Comment with id $commentId not found!")
    }

}

fun main() {
    val service = NoteService()
    val note = Note(0, "First", "Hello", mutableListOf())
    val comment = Comment(0, 0, false, "Hi")
    service.add(note)
    service.addComment(1, comment)
    service.deleteComment(1, 1)

    service.delete(1)
}