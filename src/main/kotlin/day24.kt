const val MIN_INTERSECTION = 200000000000000.0
const val MAX_INTERSECTION = 400000000000000.0

fun day24 (lines: List<String>) {
    
    val hailstones = parseHailstones(lines)
    val intersections = mutableListOf<Intersection>()
    
    for (i in hailstones.indices) {
        for (j in i + 1..hailstones.indices.last) {
            val intersection = findIntersection(hailstones[i], hailstones[j])
            if (intersection != null) {
                if (intersectionInThePast(hailstones[i], intersection) || intersectionInThePast(hailstones[j], intersection)) {
                    continue
                }
                intersections.add(intersection)
            }
        }
    }
    
    val validIntersections = intersections.filter { it.x in MIN_INTERSECTION..MAX_INTERSECTION && it.y in MIN_INTERSECTION..MAX_INTERSECTION }

    println("Day 24 part 1: ${validIntersections.size}")
    println("Day 24 part 2: ")
    println()
}

fun intersectionInThePast(hailstone: HailStone, intersection: Intersection): Boolean {
    return (intersection.x > hailstone.pos.x && hailstone.vel.x < 0) || (intersection.x < hailstone.pos.x && hailstone.vel.x > 0)
}

fun findIntersection(h1: HailStone, h2: HailStone): Intersection? {
    val denominator = h1.vel.x * h2.vel.y - h2.vel.x * h1.vel.y
    
    if (denominator == 0L) {
        return null
    }
    
    val t = 1.0 * (h2.vel.y * (h2.pos.x - h1.pos.x) + h2.vel.x * (h1.pos.y - h2.pos.y)) / denominator
    
    val intersectionX = h1.vel.x * t + h1.pos.x
    val intersectionY = h1.vel.y * t + h1.pos.y

    return Intersection(intersectionX, intersectionY)
}

fun parseHailstones(lines: List<String>): MutableList<HailStone> {
    val hailstones = mutableListOf<HailStone>()
    
    lines.map{ it.replace(" ", "") }.forEach { line ->
        val positions = line.split("@")[0].split(",").map { it.toLong() }
        val velocities = line.split("@")[1].split(",").map { it.toLong() }
        
        hailstones.add(HailStone(BigPoint(positions[0], positions[1], positions[2]), BigPoint(velocities[0], velocities[1], velocities[2])))
    }
    
    return hailstones
}

data class HailStone(val pos: BigPoint, val vel: BigPoint)

data class BigPoint(val x: Long, val y: Long, val z: Long)

data class Intersection(val x: Double, val y: Double)