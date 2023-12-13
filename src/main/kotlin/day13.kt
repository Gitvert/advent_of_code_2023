val originalReflection = mutableMapOf<Int, Mirror>()

fun day13 (lines: List<String>) {
    
    val patterns = parsePatterns(lines)
    var sum = 0L
    
    patterns.forEachIndexed { index, it -> 
        var newSum = findHorizontalReflectionSum(it, 100, true, index) 
        if (newSum == 0) {
            newSum = findVerticalReflectionSum(it, true, index)
        }
        sum += newSum
    }

    println("Day 13 part 1: $sum")
    var smudgeSum = 0L
    var hits = 0
    
    patterns.forEachIndexed { index, pattern ->
        var horizontalSum: Int
        var verticalSum: Int
        for (row in pattern.pattern.indices) {
            for (col in pattern.pattern[row].indices) {
                horizontalSum = findHorizontalReflectionSum(replaceSmudge(pattern, row, col), 100, false, index)
                verticalSum = findVerticalReflectionSum(replaceSmudge(pattern, row, col), false, index)
                
                if (horizontalSum != 0) {
                    smudgeSum += horizontalSum
                    hits++
                    return@forEachIndexed
                }
                
                if (verticalSum != 0) {
                    smudgeSum += verticalSum
                    hits++
                    return@forEachIndexed
                }
            }
            
        }
    }
    
    println("Day 13 part 2: $smudgeSum")
    println()
}

fun replaceSmudge(pattern: Pattern, row: Int, col: Int): Pattern {
    val replaced = Pattern(mutableListOf())
    
    for (y in pattern.pattern.indices) {
        var line = ""
        for (x in pattern.pattern[y].indices) {
            line += if (y == row && x == col) {
                if (pattern.pattern[y][x] == '.') { '#' } else { '.' }
            } else {
                pattern.pattern[y][x]
            }
        }
        replaced.pattern.add(line)
    }
    
    return replaced
}

fun findHorizontalReflectionSum(pattern: Pattern, multiplier: Int, part1: Boolean, index: Int): Int {
    for (i in pattern.pattern.indices) {
        if (i == pattern.pattern.size - 1) {
            continue
        }
        
        if (!part1 && multiplier == 1 && originalReflection[index]!!.originalVerticalLine != null && originalReflection[index]!!.originalVerticalLine == i) {
            continue
        }

        if (!part1 && multiplier == 100 && originalReflection[index]!!.originalHorizontalLine != null && originalReflection[index]!!.originalHorizontalLine == i) {
            continue
        }
        
        if (pattern.pattern[i] == pattern.pattern[i + 1]) {
            var matching = true
            var indexOffset = 1

            try {
                while (matching) {
                    matching = pattern.pattern[i - indexOffset + 1] == pattern.pattern[i + indexOffset]
                    indexOffset++
                }
            } catch (e: IndexOutOfBoundsException) {
                if (part1) {
                    if (multiplier == 1) {
                        originalReflection[index] = Mirror(null, i)
                    } else {
                        originalReflection[index] = Mirror(i, null)
                    }
                }
                return (i + 1) * multiplier
            }
        }
    }
    
    return 0
}

fun rotatePattern(pattern: Pattern): Pattern {
    val rotated = Pattern(mutableListOf())

    pattern.pattern[0].forEach { _ ->
        rotated.pattern.add("")
    }

    pattern.pattern.reversed().forEach { row ->
        row.forEachIndexed { index, c ->
            rotated.pattern[index] = rotated.pattern[index] + c
        }
        
    }
    
    return rotated
}

fun findVerticalReflectionSum(pattern: Pattern, part1: Boolean, index: Int): Int {
    return findHorizontalReflectionSum(rotatePattern(pattern), 1, part1, index)
}

fun parsePatterns(lines: List<String>): List<Pattern> {
    val patterns = mutableListOf<Pattern>()
    
    var pattern = Pattern(mutableListOf())
    
    lines.forEach { 
        if (it.isEmpty()) {
            patterns.add(pattern)
            pattern = Pattern(mutableListOf())
        } else {
            pattern.pattern.add(it)
        }
    }
    
    patterns.add(pattern)
    
    return patterns
}

data class Pattern(val pattern: MutableList<String>)

data class Mirror(val originalHorizontalLine: Int?, val originalVerticalLine: Int?)