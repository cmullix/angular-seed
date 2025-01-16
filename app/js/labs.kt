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
