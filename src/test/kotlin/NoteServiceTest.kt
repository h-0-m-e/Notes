import org.junit.Test
import kotlin.test.assertEquals

class NoteServiceTest {

    @Test
    fun addComment() {
        val service = NoteService()
        service.add(Note(0,"First note","Hello", mutableListOf()))
        val comment = Comment(1,1,false,"Hi")
        val result = service.addComment(1,comment)

        assertEquals(comment, result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun addComment_ShouldThrow() {
        val service = NoteService()
        service.add(Note(0,"First note","Hello", mutableListOf()))
        service.addComment(9,Comment(0,0,false,"Hello there"))
    }

    @Test
    fun getNoteById() {
        val service = NoteService()
        val note = Note(1,"First note","Hello", mutableListOf())
        service.add(note)
        val result = service.getNoteById(1)

        assertEquals(note, result)

    }

    @Test(expected = NoteNotFoundException::class)
    fun getNoteById_ShouldThrow() {
        val service = NoteService()
        val note = Note(1,"First note","Hello", mutableListOf())
        service.add(note)
        service.getNoteById(10)
    }

    @Test
    fun getDeletedComments() {
        val service = NoteService()

        val result = service.getDeletedComments()

        assertEquals(mutableListOf(),result)
    }

    @Test
    fun getCommentsByNoteId() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        val result = service.getCommentsByNoteId(1)

        assertEquals(mutableListOf(comment),result)
    }

    @Test(expected = NoCommentsException::class)
    fun getCommentsByNoteId_ShouldThrow_NoComments() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        service.add(note)
        service.getCommentsByNoteId(1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getCommentsByNoteId_ShouldThrow() {
        val service = NoteService()
        service.getCommentsByNoteId(1)
    }

    @Test
    fun updateComment() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        val updComment = Comment(1,1,false,"Hi maaaaan")
        service.add(note)
        service.addComment(1,comment)
        val result = service.updateComment(updComment)

        assertEquals(true,result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun updateComment_ShouldThrow_CommentNotFound() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        val updComment = Comment(9,1,false,"Hi maaaaan")
        service.add(note)
        service.addComment(1,comment)
        service.updateComment(updComment)
    }

    @Test(expected = NoteNotFoundException::class)
    fun updateComment_ShouldThrow_NoteNotFound() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        val updComment = Comment(1,2,false,"Hi maaaaan")
        service.add(note)
        service.addComment(1,comment)
        service.updateComment(updComment)
    }

    @Test
    fun deleteComment() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)

        val result = service.deleteComment(1,1)

        assertEquals(true, result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteComment_ShouldThrow_CommentNotFound() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(1,9)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteComment_ShouldThrow_NoteNotFound() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(9,1)
    }

    @Test
    fun restoreComment() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(1,1)

        val result = service.restoreComment(1)

        assertEquals(true, result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreComment_ShouldThrow_CommentNotFound() {
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(1,1)
        service.restoreComment(9)
    }

    @Test
    fun update(){
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val updNote = Note(1,"First UPDATED","Hello UPDATED", mutableListOf())
        service.add(note)

        val result = service.update(updNote)

        assertEquals(true, result)

    }

    @Test
    fun update_NotUpdated(){
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val updNote = Note(9,"First UPDATED","Hello UPDATED", mutableListOf())
        service.add(note)
        val result = service.update(updNote)

        assertEquals(false, result)

    }

    @Test
    fun delete(){
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(1,1)

        val result = service.delete(1)

        assertEquals(true,result)

    }

    @Test(expected = NoteNotFoundException::class)
    fun delete_ShouldThrow_NoteNotFound(){
        val service = NoteService()
        val note = Note(0,"First","Hello", mutableListOf())
        val comment = Comment(0,0,false,"Hi")
        service.add(note)
        service.addComment(1,comment)
        service.deleteComment(1,1)
        service.delete(9)
    }
}