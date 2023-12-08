private const val DAY = "Day07"

fun main() {
    val cardValues: Map<Char, Int> = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2
    )

    fun parse(input: List<String>): List<Hand> {
        return input.map {
            // 32T3K 765
            Hand(cards = it.substring(0, 5), bid = it.substring(6).trim().toInt())
        }.toList()
    }

    fun compareCards(hand1: Hand, hand2: Hand): Int {
        if (hand1.type() == hand2.type()) {
            for (index in IntRange(0, 4)) {
                if (hand1.cards[index] != hand2.cards[index]) {
                    return cardValues.getOrDefault(hand1.cards[index], 0) - cardValues.getOrDefault(hand2.cards[index], 0)
                }
            }
        }

        return hand1.type().ordinal - hand2.type().ordinal
    }

    fun part1(input: List<String>): Long {
        val hands = parse(input)
        var result = 0L

        hands.sortedWith { hand1, hand2 ->
            if (hand1.type() == hand2.type()) {
                for (index in IntRange(0, 4)) {
                    if (hand1.cards[index] != hand2.cards[index]) {
                        return@sortedWith cardValues.getOrDefault(hand1.cards[index], 0) - cardValues.getOrDefault(hand2.cards[index], 0)
                    }
                }
            }

            return@sortedWith hand1.type().ordinal - hand2.type().ordinal
        }.forEachIndexed { index, hand ->
            // println("Hand $hand (${hand.type()}) at ${index+1}")
            result += hand.bid * (index + 1)
        }

        println("Result = $result")
        return result
    }

    // basic check on hand type
    check(Hand("AAAAA", 0).type() == HandType.FiveOfAKind)
    check(Hand("AA8AA", 0).type() == HandType.FourOfAKind)
    check(Hand("23332", 0).type() == HandType.FullHouse)
    check(Hand("TTT98", 0).type() == HandType.ThreeOfAKind)
    check(Hand("23432", 0).type() == HandType.TwoPair)
    check(Hand("A23A4", 0).type() == HandType.OnePair)
    check(Hand("23456", 0).type() == HandType.HighCard)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 6440L)

    val input = readInput(DAY)
    part1(input).println()
}

data class Hand(val cards: String, val bid: Int) {
    fun type(): HandType {
        val groups = cards.toCharArray().groupBy { it }.map { it.value.first() to it.value.size }.sortedBy { -it.second }

        return when (groups.size) {
            // Five identical cards
            1 -> HandType.FiveOfAKind

            // Four identical cards or FullHouse
            2 -> if (groups[0].second == 4) return HandType.FourOfAKind else HandType.FullHouse

            // Three cards or two pairs
            3 -> if (groups[0].second == 3) return HandType.ThreeOfAKind else HandType.TwoPair

            4 -> return HandType.OnePair

            else -> HandType.HighCard
        }
    }
}

enum class HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind
}
