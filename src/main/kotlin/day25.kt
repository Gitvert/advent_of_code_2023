import java.util.*

fun day25 (lines: List<String>) {
    val components = parseComponentConnections(lines)
    val connections = mutableListOf<Pair<String, String>>()
    
    components.forEach { component ->
        component.value.forEach { 
            if (!connections.contains(Pair(component.key, it)) && !connections.contains(Pair(it, component.key))) {
                connections.add(Pair(component.key, it))
            }
        }
    }

    val connectionCount = countConnectionUsages(connections, components).entries.sortedByDescending { it.value }
    val remainingConnections = connections.toMutableList()
    remainingConnections.remove(connectionCount[0].key)
    remainingConnections.remove(connectionCount[1].key)
    remainingConnections.remove(connectionCount[2].key)

    val lhs = getReachableComponents(remainingConnections, remainingConnections.first().first)
    val rem = remainingConnections.filterNot { lhs.contains(it.first) || lhs.contains(it.second) }

    val rhs = getReachableComponents(rem, rem.first().first)
    
    println("Day 25 part 1: ${lhs.size * rhs.size}")
    println()
}

fun countConnectionUsages(connections: MutableList<Pair<String, String>>, components: MutableMap<String, MutableSet<String>>): MutableMap<Pair<String, String>, Int> {
    val connectionCount = mutableMapOf<Pair<String, String>, Int>()

    connections.forEach { 
        connectionCount[it] = 0
    }
    
    components.forEach { component ->
        val queue: Queue<String> = LinkedList()

        val visited = mutableListOf(component.key)
        queue.add(component.key)

        while(queue.isNotEmpty()) {
            val current = queue.peek()
            queue.remove()

            connections.filter { it.first == current || it.second == current }.forEach {
                val next = if (it.first == current) {
                    it.second
                } else {
                    it.first
                }

                if (!visited.contains(next)) {
                    visited.add(next)
                    queue.add(next)
                    connectionCount[it] = connectionCount[it]!! + 1
                }
            }
        }
    }
    
    return connectionCount
}

fun getReachableComponents(connections: List<Pair<String, String>>, start: String): List<String> {
    val queue: Queue<String> = LinkedList()

    val visited = mutableListOf(start)
    queue.add(start)

    while(queue.isNotEmpty()) {
        val current = queue.peek()
        queue.remove()
        
        connections.filter { it.first == current || it.second == current }.forEach { 
            val next = if (it.first == current) {
                it.second
            } else {
                it.first
            }
            
            if (!visited.contains(next)) {
                visited.add(next)
                queue.add(next)
            }
        }
    }
    
    return visited
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
