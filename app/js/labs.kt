import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MapResultsToColumnsTest {

    @Test
    fun `mapResultsToColumns with Array rows and matching columns`() {
        val resultList = mutableListOf(
            arrayOf("value1", "value2"),
            arrayOf("value3", "value4")
        )
        val columns = listOf("col1", "col2")

        val result = mapResultsToColumns(resultList, columns)

        val expected = listOf(
            mutableMapOf("col1" to "value1", "col2" to "value2"),
            mutableMapOf("col1" to "value3", "col2" to "value4")
        )

        assertEquals(expected, result)
    }

    @Test
    fun `mapResultsToColumns with List rows and matching columns`() {
        val resultList = mutableListOf(
            listOf("value1", "value2"),
            listOf("value3", "value4")
        )
        val columns = listOf("col1", "col2")

        val result = mapResultsToColumns(resultList, columns)

        val expected = listOf(
            mutableMapOf("col1" to "value1", "col2" to "value2"),
            mutableMapOf("col1" to "value3", "col2" to "value4")
        )

        assertEquals(expected, result)
    }

    @Test
    fun `mapResultsToColumns with single non-list row`() {
        val resultList = mutableListOf<Any?>("value1")
        val columns = listOf("col1")

        val result = mapResultsToColumns(resultList, columns)

        val expected = listOf(
            mutableMapOf("col1" to "value1")
        )

        assertEquals(expected, result)
    }

    @Test
    fun `mapResultsToColumns with mismatched Array row size`() {
        val resultList = mutableListOf(
            arrayOf("value1", "value2", "value3")
        )
        val columns = listOf("col1", "col2")

        val exception = assertThrows<IllegalArgumentException> {
            mapResultsToColumns(resultList, columns)
        }
        assertEquals("Invalid number of columns", exception.message)
    }

    @Test
    fun `mapResultsToColumns with mismatched List row size`() {
        val resultList = mutableListOf(
            listOf("value1", "value2", "value3")
        )
        val columns = listOf("col1", "col2")

        val exception = assertThrows<IllegalArgumentException> {
            mapResultsToColumns(resultList, columns)
        }
        assertEquals("Invalid number of columns", exception.message)
    }

    @Test
    fun `mapResultsToColumns with single column but multiple columns expected`() {
        val resultList = mutableListOf<Any?>("value1")
        val columns = listOf("col1", "col2")

        val exception = assertThrows<IllegalArgumentException> {
            mapResultsToColumns(resultList, columns)
        }
        assertEquals("Only one column expected, but got: [col1, col2]", exception.message)
    }
}


fun mapResultsToColumns(
        resultList: MutableList<Any?>,
        columns: List<String>
    ): List<MutableMap<String, Any?>> =
        resultList.map { row ->
            when (row) {
                is Array<*> -> {
                    if (row.size != columns.size) {
                        throw IllegalArgumentException("Invalid number of columns")
                    }
                    columns.zip(row.toList()).toMap()
                }
                is List<*> -> {
                    if (row.size != columns.size) {
                        throw IllegalArgumentException("Invalid number of columns")
                    }
                    columns.zip(row).toMap()
                }

                else -> {
                    if (columns.size != 1) {
                        throw IllegalArgumentException("Only one column expected, but got: $columns")
                    }
                    mapOf(columns.first() to row)
                }
            } as MutableMap<String, Any?>
        }
