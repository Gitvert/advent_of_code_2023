var hits = 0L
val foundSprings = mutableMapOf<String, Long>()
var cacheHits = 0L

fun day12 (lines: List<String>) {
    var totalArrangements = 0L
    
    lines.forEach { line ->
        totalArrangements += findArrangements(line.split(" ")[0], line.split(" ")[1].split(",").map { Integer.parseInt(it) })
        foundSprings.clear()
    }

    println("Day 12 part 1: $totalArrangements")
    
    val unfoldedLines = unfoldLines(lines)
    val totalUnfoldedArrangements = 0L

    /*unfoldedLines.forEach { line ->
        println(line)
        totalArrangements += findArrangements(line.split(" ")[0], line.split(" ")[1].split(",").map { Integer.parseInt(it) })
        foundSprings.clear()
    }*/
    println(hits)
    println(foundSprings.size)
    println(cacheHits)
    
    println("Day 12 part 2: $totalUnfoldedArrangements")
    println()
}

fun findArrangements(springs: String, groups: List<Int>): Long {
    hits++
    /*if (hits % 10000L == 0L) {
        println(hits)
    }*/

    var springGroupCount = 0
    val foundGroups = mutableListOf<Int>()

    springs.forEach {
        if (it == '#') {
            springGroupCount++
        } else if (it == '.' && springGroupCount > 0) {
            foundGroups.add(springGroupCount)
            springGroupCount = 0
        } else if (it != '.') {
            springGroupCount = 0
        }
    }

    if (springGroupCount > 0) {
        foundGroups.add(springGroupCount)
        springGroupCount = 0
    }
    
    if (foundGroups.size > groups.size || (foundGroups.size > 0 && foundGroups.max() > groups.max())) {
        return 0
    }
    
    if (springs.contains("?")) {
        val dotReplace = springs.replaceFirst("?", ".")
        val hashtagReplace = springs.replaceFirst("?", "#")
        
        val left: Long
        val right: Long
        
        if (foundSprings.contains(dotReplace)) {
            cacheHits++
            left = foundSprings[dotReplace]!!
        } else {
            val temp = findArrangements(dotReplace, groups)
            foundSprings[dotReplace] = temp
            left = temp
        }
        
        if(foundSprings.contains(hashtagReplace)) {
            cacheHits++
            right = foundSprings[hashtagReplace]!!
        } else {
            val temp = findArrangements(hashtagReplace, groups)
            foundSprings[hashtagReplace] = temp
            right = temp
        }
        
        return left + right
    }

    if (foundGroups == groups) {
        return 1
    }
    
    return 0
}

fun unfoldLines(lines: List<String>): List<String> {
    val unfoldedLines = mutableListOf<String>()
    
    lines.forEach { 
        val springs = it.split(" ")[0]
        val groups = it.split(" ")[1]
        
        unfoldedLines.add("$springs?$springs?$springs?$springs?$springs $groups,$groups,$groups,$groups,$groups")
    }
    
    return unfoldedLines
}