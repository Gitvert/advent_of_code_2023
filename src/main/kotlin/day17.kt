fun day17 (lines: List<String>) {
    val map = parseFactoryMap(lines)
    val maxX = map[0].indices.last
    val maxY = map.indices.last

    val leastHeat = dijkstra(map, maxX, maxY)
    
    println("Day 17 part 1: $leastHeat")
    println("Day 17 part 2: ")
    println()
}

fun dijkstra(map: List<List<Int>>, maxX: Int, maxY: Int): Int {
    val lowestCost = mutableMapOf<NodeKey, Int>()
    val queue = mutableSetOf<Node>()
    
    lowestCost[NodeKey(Pos(0, 0), Pos(0, 0))] = 0
    queue.add(Node(NodeKey(Pos(0, 0), Pos(0, 0)), 0))
    
    while(queue.isNotEmpty()) {
        val currentNode = queue.minBy { lowestCost[it.key]!! }
        queue.remove(currentNode)
        
        val newNodes = mutableSetOf<Node>()
        
        if (currentNode.key.direction.x == 0) {
            evaluateNewNode(map, currentNode, Pos(1, 0), 3, newNodes, maxX, maxY)
            evaluateNewNode(map, currentNode, Pos(-1, 0), 3, newNodes, maxX, maxY)
        }
        
        if (currentNode.key.direction.y == 0) {
            evaluateNewNode(map, currentNode, Pos(0, 1), 3, newNodes, maxX, maxY)
            evaluateNewNode(map, currentNode, Pos(0, -1), 3, newNodes, maxX, maxY)
        }
        
        newNodes.forEach { newNode ->
            if (lowestCost[newNode.key] != null && lowestCost[newNode.key]!! > newNode.heatLoss) {
                lowestCost[newNode.key] = newNode.heatLoss
                queue.removeIf { it.key == newNode.key }
                queue.add(newNode)
            } else if (lowestCost[newNode.key] == null) {
                lowestCost[newNode.key] = newNode.heatLoss
                queue.add(newNode)
            }
        }
    }
    
    return lowestCost.entries.filter { it.key.pos == Pos(maxX, maxY) }.minOf { it.value }
}

fun evaluateNewNode(map: List<List<Int>>, start: Node, direction: Pos, steps: Int, newNodes: MutableSet<Node>, maxX: Int, maxY: Int) {
    var addedHeatLoss = 0
    for (i in 1..steps) {
        val newNode = Node(NodeKey(Pos(start.key.pos.x + direction.x * i, start.key.pos.y + direction.y * i), direction), start.heatLoss)
        if (newNode.key.pos.x in 0..maxX && newNode.key.pos.y in 0..maxY) {
            addedHeatLoss += map[newNode.key.pos.y][newNode.key.pos.x]
            newNode.heatLoss += addedHeatLoss
            
            newNodes.add(newNode)
        }
    }
}

fun parseFactoryMap(lines: List<String>): List<List<Int>> {
    val map = mutableListOf<MutableList<Int>>()
    
    for (y in lines.indices) {
        val row = mutableListOf<Int>()
        for (x in lines[0].indices) {
            row.add(Integer.parseInt(lines[y][x].toString()))
        }
        map.add(row)
    }
    
    return map
}

data class NodeKey(val pos: Pos, val direction: Pos) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeKey) return false

        if (pos != other.pos) return false
        if (direction != other.direction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + direction.hashCode()
        return result
    }
}

data class Node(val key: NodeKey, var heatLoss: Int)