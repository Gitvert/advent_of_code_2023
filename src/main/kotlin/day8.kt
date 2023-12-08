fun day8 (lines: List<String>) {
    val map = parseDesertMap(lines)
    
    val steps = countSteps(lines[0], map)

    println("Day 8 part 1: $steps")
    println("Day 8 part 2: ")
    println()
}

fun countSteps(order: String, map: Map<String, LeftRightInstruction>): Long {
    var steps = 0L
    var currentNode = "AAA"
    
    while (currentNode != "ZZZ") {
        for (i in order.indices) {
            val nextNode = map[currentNode]!!

            currentNode = if (order[i] == 'R') {
                nextNode.right
            } else {
                nextNode.left
            }
            
            steps++
        }
    }
    
    return steps
}
fun parseDesertMap(lines: List<String>): Map<String, LeftRightInstruction> {
    val instructions = mutableMapOf<String, LeftRightInstruction>()
    
    for (i in lines.indices) {
        if (i < 2) {
            continue
        }
        
        val key = lines[i].split(" = ")[0]
        val value = lines[i].split(" = ")[1].trim('(').trim(')')
        val left = value.split(", ")[0]
        val right = value.split(", ")[1]
        
        instructions[key] = LeftRightInstruction(left, right)
    }
    
    return instructions
}

data class LeftRightInstruction(val left: String, val right: String)