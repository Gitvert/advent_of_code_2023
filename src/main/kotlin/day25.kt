fun day25 (lines: List<String>) {
    val components = parseComponentConnections(lines)

    println("Day 25 part 1: ")
    println("Day 25 part 2: ")
    println()
}

fun parseComponentConnections(lines: List<String>): MutableMap<String, MutableSet<String>> {
    val components = mutableMapOf<String, MutableSet<String>>()
    
    lines.forEach { line ->
        line.replace(":", "").split(" ").forEach { 
            components[it] = mutableSetOf()
        }
    }
    
    lines.forEach { line ->
        val from = line.split(": ")[0]
        val to = line.split(": ")[1].split(" ")
        
        to.forEach { 
            components[from]?.add(it)
            components[it]?.add(from)
        }
    }
    
    return components
}
