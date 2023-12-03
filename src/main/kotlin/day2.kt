const val RED_MAX = 12
const val GREEN_MAX = 13
const val BLUE_MAX = 14

fun day2 (lines: List<String>) {
    var sum = 0
    var powerSum = 0
    
    lines.forEach { 
        sum += getIdToSum(it)
        powerSum += getPowerOfSetToSum(it)
    }
    
    
    println("Day 2 part 1: $sum")
    println("Day 2 part 2: $powerSum")
    println()
}

fun getIdToSum(line: String): Int {
    val game = line.split(":")[1]
    val rounds = game.split(";")
    var possible = true
    
    rounds.forEach { round ->
        val cubes = round.split(",")
        
        cubes.forEach { cube ->
            val number = Integer.parseInt(cube.filter { it.isDigit() })
            
            if (cube.contains("blue") && number > BLUE_MAX) {
                possible = false
            } else if (cube.contains("green") && number > GREEN_MAX) {
                possible = false
            } else if (cube.contains("red") && number > RED_MAX) {
                possible = false
            }
        }
    }
    
    if (possible) {
        return Integer.parseInt(line.split(":")[0].filter { it.isDigit() })
    }
    
    return 0
}

fun getPowerOfSetToSum(line: String): Int {
    val game = line.split(":")[1]
    val rounds = game.split(";")
    
    var minRed = Integer.MIN_VALUE
    var minGreen = Integer.MIN_VALUE
    var minBlue = Integer.MIN_VALUE

    rounds.forEach { round ->
        val cubes = round.split(",")

        cubes.forEach { cube ->
            val number = Integer.parseInt(cube.filter { it.isDigit() })

            if (cube.contains("blue") && number > minBlue) {
                minBlue = number
            } else if (cube.contains("green") && number > minGreen) {
                minGreen = number
            } else if (cube.contains("red") && number > minRed) {
                minRed = number
            }
        }
    }
    
    return minRed * minGreen * minBlue
}