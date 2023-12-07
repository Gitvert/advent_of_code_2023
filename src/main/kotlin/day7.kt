fun day7 (lines: List<String>) {

    val cards = parseCamelCards(lines)
    
    cards.forEach { it.handStrength = findHandStrength(it) }
    
    cards.forEach { 
        it.cards = it.cards
            .replace("A", "Z")
            .replace("K", "Y")
            .replace("Q", "X")
            .replace("J", "V")
            .replace("T", "U")
    }
    
    var sum = 0L
    
    cards.sortedWith(compareBy({ it.handStrength }, { it.cards })).forEachIndexed { index, card ->
        sum += (index + 1) * card.bid
    } 
    
    println("Day 7 part 1: $sum")
    println("Day 7 part 2: ")
    println()
}

fun findHandStrength(hand: CamelCardsHand): Int {
    val cardOccurrences = hand.cards.groupingBy { it }.eachCount()
    
    val sortedCardOccurrences = cardOccurrences.entries.map { it.value.toString() + it.key.toString() }.sortedDescending()
    
    if (sortedCardOccurrences[0][0] == '5') {
        return 7
    } else if (sortedCardOccurrences[0][0] == '4') {
        return 6
    } else if (sortedCardOccurrences[0][0] == '3') {
        return if (sortedCardOccurrences[1][0] == '2') {
            5
        } else {
            4
        }
    } else if (sortedCardOccurrences[0][0] == '2') {
        return if (sortedCardOccurrences[1][0] == '2') {
            3
        } else {
            2
        }
    } else {
        return 1
    }
}

fun parseCamelCards(lines: List<String>): List<CamelCardsHand> {
    val cards = mutableListOf<CamelCardsHand>()
    
    lines.forEach { 
        cards.add(CamelCardsHand(it.split(" ")[0], it.split(" ")[1].toLong(), 0))
    }
    
    return cards
}

data class CamelCardsHand(var cards: String, val bid: Long, var handStrength: Int)