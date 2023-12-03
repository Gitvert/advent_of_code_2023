fun day3 (lines: List<String>) {
    val partNumbers = findPartNumbers(lines)

    markInvalidPartNumbers(lines, partNumbers)
    
    val partNumberSum = partNumbers.filter { it.isValidPartNumber }.sumOf { it.number }

    println("Day 3 part 1: $partNumberSum")
    println("Day 3 part 2:")
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
                    partNumbers.add(PartNumber(Integer.parseInt(numberBuilder), xIndex - numberBuilder.length, numberBuilder.length, yIndex, false))
                }
                numberBuilder = ""
            }
            
            if (xIndex == row.length - 1 && numberBuilder.isNotEmpty()) {
                partNumbers.add(PartNumber(Integer.parseInt(numberBuilder), xIndex - numberBuilder.length, numberBuilder.length, yIndex, false))
            }
        }
    }

    return partNumbers
}

data class PartNumber (val number: Int, val startX: Int, val length: Int, val y: Int, var isValidPartNumber: Boolean)

//data class Coordinate (val x: Int, val y: Int)