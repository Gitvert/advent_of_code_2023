fun day3 (lines: List<String>) {
    val partNumbers = findPartNumbers(lines)

    markInvalidPartNumbers(lines, partNumbers)
    findAdjacentGears(lines, partNumbers)
    
    val partNumberSum = partNumbers.filter { it.isValidPartNumber }.sumOf { it.number }
    
    val gearRatioSum = calculateGearRatioSum(partNumbers)

    println("Day 3 part 1: $partNumberSum")
    println("Day 3 part 2: $gearRatioSum")
}

fun findAdjacentGears(lines: List<String>, partNumbers: List<PartNumber>) {
    partNumbers.forEach {
        val gearCoordinate = anyAdjacentGear(lines, it)
        if(gearCoordinate != null) {
            it.gearCoordinate = gearCoordinate
        }
    }
}

fun anyAdjacentGear(lines: List<String>, partNumber: PartNumber): Coordinate? {
    for (i in partNumber.startX..<partNumber.startX + partNumber.length) {
        val gearCoordinate = findAdjacentGear(lines, i , partNumber.y)
        if (gearCoordinate != null) {
            return gearCoordinate
        }
    }
    
    return null
}

fun findAdjacentGear(lines: List<String>, x: Int, y: Int): Coordinate? {
    for (xi in x-1..x+1) {
        for (yi in y-1..y+1) {
            try {
                if (lines[yi][xi] == '*') {
                    return Coordinate(xi, yi)
                }
            } catch (e: IndexOutOfBoundsException) {
                continue
            }
        }
    }
    
    return null
}

fun calculateGearRatioSum(partNumbers: List<PartNumber>): Long {
    var gearRatioSum = 0L
    
    for (i in partNumbers.indices) {
        if (partNumbers[i].gearCoordinate == null) {
            continue
        }
        val numbersWithGear = mutableListOf(partNumbers[i].number)
        for (j in partNumbers.indices) {
            if (i == j) {
                continue
            }
            
            if (partNumbers[i].gearCoordinate == partNumbers[j].gearCoordinate) {
                numbersWithGear.add(partNumbers[j].number)
            }
        }
        
        if (numbersWithGear.size == 2) {
            gearRatioSum += (numbersWithGear[0] * numbersWithGear[1])
        }
    }
    
    return gearRatioSum / 2
}

fun markInvalidPartNumbers(lines: List<String>, partNumbers: List<PartNumber>) {
    partNumbers.forEach {
        if(anyAdjacent(lines, it)) {
            it.isValidPartNumber = true
        }
    }
}

fun anyAdjacent(lines: List<String>, partNumber: PartNumber): Boolean {
    for (i in partNumber.startX..<partNumber.startX + partNumber.length) {
        if (isAdjacent(lines, i, partNumber.y)) {
            return true
        }
    }

    return false
}

fun isAdjacent(lines: List<String>, x: Int, y: Int): Boolean {
    for (xi in x-1..x+1) {
        for (yi in y-1..y+1) {
            try {
                if (lines[yi][xi] != '.' && !lines[yi][xi].isDigit()) {
                    return true
                }
            } catch (e: IndexOutOfBoundsException) {
                continue
            }
        }
    }
    
    return false
}

fun findPartNumbers(lines: List<String>): List<PartNumber> {
    val partNumbers = mutableListOf<PartNumber>()

    lines.forEachIndexed { yIndex, row ->
        var numberBuilder = ""
        row.forEachIndexed { xIndex, cell ->
            if (cell.isDigit()) {
                numberBuilder += cell
            } else {
                if (numberBuilder.isNotEmpty()) {
                    partNumbers.add(PartNumber(Integer.parseInt(numberBuilder), xIndex - numberBuilder.length, numberBuilder.length, yIndex, false, null))
                }
                numberBuilder = ""
            }
            
            if (xIndex == row.length - 1 && numberBuilder.isNotEmpty()) {
                partNumbers.add(PartNumber(Integer.parseInt(numberBuilder), xIndex - numberBuilder.length, numberBuilder.length, yIndex, false, null))
            }
        }
    }

    return partNumbers
}

data class PartNumber (val number: Int, val startX: Int, val length: Int, val y: Int, var isValidPartNumber: Boolean, var gearCoordinate: Coordinate?)

data class Coordinate (val x: Int, val y: Int)