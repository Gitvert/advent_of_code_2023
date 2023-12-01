fun day1 (lines: List<String>) {
    
    day1part1(lines)
    day1part2(lines)
    println()
}

fun day1part1(lines: List<String>) {
    var sum = 0

    lines.forEach { line ->
        val digits = line.filter { it.isDigit() }
        sum += Integer.parseInt(digits.first().toString() + digits.last().toString());
    }
    
    println("Day 1 part 1: $sum")
}

fun day1part2(lines: List<String>) {
    var sum = 0

    lines.forEach { line ->
        sum += findInOrder(line)
    }
    
    println("Day 1 part 2: $sum")
}

fun findInOrder(line: String): Int {
    val length = line.length
    var start = 0
    var end = 0
    val numbers = mutableListOf<Int>()
    
    while (start < length) {
        addIfNumber(line.substring(start, end), numbers)
        
        end++

        if (end > length) {
            start++
            end = start
        }
    }
    
    return Integer.parseInt(numbers.first().toString() + numbers.last().toString())
}

fun addIfNumber(word: String, numbers: MutableList<Int>) {
    if (word.length == 1 && word[0].isDigit()) {
        numbers.add(Integer.parseInt(word[0].toString()))
    }
    
    when (word) {
        "one" -> numbers.add(1)
        "two" -> numbers.add(2)
        "three" -> numbers.add(3)
        "four" -> numbers.add(4)
        "five" -> numbers.add(5)
        "six" -> numbers.add(6)
        "seven" -> numbers.add(7)
        "eight" -> numbers.add(8)
        "nine" -> numbers.add(9)
    }
}
