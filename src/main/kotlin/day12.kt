fun day12 (lines: List<String>) {
    var totalArrangements = 0L
    
    lines.forEach { line ->
        totalArrangements += findArrangements(line.split(" ")[0], line.split(" ")[1].split(",").map { Integer.parseInt(it) })
    }

    println("Day 12 part 1: $totalArrangements")
    println("Day 12 part 2: ")
    println()
}

fun findArrangements(springs: String, groups: List<Int>): Long {
    if (springs.contains("?")) {
        return findArrangements(springs.replaceFirst("?", "."), groups) + findArrangements(springs.replaceFirst("?", "#"), groups)
    }
    
    var springGroupCount = 0
    val foundGroups = mutableListOf<Int>()
    
    springs.forEach { 
        if (it == '#') {
            springGroupCount++
        } else if (it == '.' && springGroupCount > 0) {
            foundGroups.add(springGroupCount)
            springGroupCount = 0
        }
    }

    if (springGroupCount > 0) {
        foundGroups.add(springGroupCount)
        springGroupCount = 0
    }

    return if (foundGroups == groups) {
        1
    } else {
        0
    }
}