import org.junit.Test

import org.junit.Assert.*
import kotlin.test.BeforeTest


class CrudServiceTest<T: Item> {

    @BeforeTest
    fun clear(){
    }

    @Test
    fun add() {
        val item = Note(0,"Hello","World", mutableListOf())
        val result = CrudService<Item>().add(item)

        assertEquals( item,result)

    }

    @Test
    fun get() {
        val container = mutableListOf<T>()
        val result = CrudService<Item>().get()

        assertEquals(container, result)
    }

    @Test
    fun update() {
        val service = CrudService<Item>()
        val item = Item(1)
        service.add(item)
        val updItem = Item(1)
        val result = service.update(updItem)

        assertEquals(true, result)
    }

    @Test
    fun update_IdNotTheSame() {
        val service = CrudService<Item>()
        val item = Item(1)
        service.add(item)
        val updItem = Item(2)
        val result = service.update(updItem)

        assertEquals(false, result)
    }

    @Test
    fun delete() {
        val service = CrudService<Item>()
        val item = Item(1)
        service.add(item)
        val result = service.delete(1)

        assertEquals(true, result)
    }

    @Test(expected = ItemNotFoundException::class)
    fun delete_ShouldThrow() {
        val service = CrudService<Item>()
        val item = Item(1)
        service.add(item)
        service.delete(2)
    }
}