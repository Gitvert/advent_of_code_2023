fun day8 (lines: List<String>) {
    val map = parseDesertMap(lines)
    
    val steps = countSteps(lines[0], map)
    println("Day 8 part 1: $steps")
    
    val ghostSteps = countGhostSteps(lines[0], map)
    var lcm = ghostSteps[0]
    
    for (i in ghostSteps.indices) {
        if (i == 0) {
            continue
        }
        
        lcm = lcm(lcm, ghostSteps[i])
    }
    
    println("Day 8 part 2: $lcm")
    println()
}

fun lcm(number1: Long, number2: Long): Long {
    val high = number1.coerceAtLeast(number2)
    val low = number1.coerceAtMost(number2)
    var lcm = high
    
    while (lcm % low != 0L) {
        lcm += high
    }
    
    return lcm
}

fun countGhostSteps(order: String, map: Map<String, LeftRightInstruction>): List<Long> {
    var steps = 0L
    
    val currentNodes = map.keys.filter { it.endsWith("A") }.toTypedArray()
    
    val stepsList = mutableListOf<Long>()
    
    while (true) {
        for (i in order.indices) {
            steps++
            for (j in currentNodes.indices) {
                val nextNode = map[currentNodes[j]]!!

                currentNodes[j] = if (order[i] == 'R') {
                    nextNode.right
                } else {
                    nextNode.left
                }
                
                if (currentNodes[j].endsWith("Z")) {
                    stepsList.add(steps)
                }
                
                if (stepsList.size == currentNodes.size) {
                    return stepsList
                }
            }
        }
    }
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