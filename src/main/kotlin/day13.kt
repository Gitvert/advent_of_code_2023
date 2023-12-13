fun day13 (lines: List<String>) {
    
    val patterns = parsePatterns(lines)
    var sum = 0L
    
    patterns.forEach { 
        var newSum = findHorizontalReflectionSum(it, 100) 
        if (newSum == 0) {
            newSum = findVerticalReflectionSum(it)
        }
        println(newSum)
        sum += newSum
    }

    println("Day 13 part 1: $sum")
    println("Day 13 part 2: ")
    println()
}

fun findHorizontalReflectionSum(pattern: List<String>, multiplier: Int): Int {
    for (i in pattern.indices) {
        if (i == pattern.size - 1) {
            continue
        }
        
        if (pattern[i] == pattern[i + 1]) {
            var matching = true
            var indexOffset = 1

            try {
                while (matching) {
                    matching = pattern[i - indexOffset + 1] == pattern[i + indexOffset]
                    indexOffset++
                }
            } catch (e: IndexOutOfBoundsException) {
                return (i + 1) * multiplier
            }
        }
    }
    
    return 0
}

fun rotatePattern(pattern: List<String>): List<String> {
    val rotated = mutableListOf<String>()

    pattern[0].forEach { _ ->
        rotated.add("")
    }

    pattern.reversed().forEach { row ->
        row.forEachIndexed { index, c ->
            rotated[index] = rotated[index] + c
        }
        
    }
    
    return rotated
}

fun findVerticalReflectionSum(pattern: List<String>): Int {
    return findHorizontalReflectionSum(rotatePattern(pattern), 1)
}

fun parsePatterns(lines: List<String>): List<List<String>> {
    val patterns = mutableListOf<MutableList<String>>()
    
    var pattern = mutableListOf<String>()
    
    lines.forEach { 
        if (it.isEmpty()) {
            patterns.add(pattern)
            pattern = mutableListOf()
        } else {
            pattern.add(it)
        }
    }
    
    patterns.add(pattern)
    
    return patterns
}