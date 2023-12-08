import kotlin.math.pow

private const val DAY = "Day04"

fun main() {
    fun parse(input: List<String>): List<Card> {
        return input.stream()
            .map { line ->
                // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                val parts = line.split(':', '|')

                Card(n = parts[0].substring("Card ".length).trim().toInt(),
                    winning = parts[1].trim().split(' ').filter{ it.isNotBlank() }.map { it.toInt() }.toSet(),
                    numbers = parts[2].trim().split(' ').filter{ it.isNotBlank() }.map { it.toInt() }.toSet()
                )
            }.toList()
    }

    fun part1(input: List<String>): Int {
        val cards = parse(input)

        return cards.stream()
            .map {
                // find how many numbers of this card are winning
                val common = it.winning.intersect(it.numbers)
                // compute card worth
                2.0.pow(common.size-1).toInt()
            }.toList().sum()
    }

    fun part2(input: List<String>): Int {
        val cards = parse(input)

        // an array to hold the number of copies for each card (with an extra value at start to simplify indexing)
        val copies: IntArray = IntArray(cards.size + 1) { i -> if (i == 0) 0 else 1}
        cards.forEach {
            val copiesWon = it.winning.intersect(it.numbers).count()
            if (copiesWon > 0) {
                for (n in it.n + 1..it.n + copiesWon) {
                    // increase number of copies
                    copies[n] += copies[it.n]
                }
            }
        }

        return copies.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 13)

    val input = readInput(DAY)
    part1(input).println()

    val testInput_part2 = readInput(DAY + "_test")
    check(part2(testInput_part2) == 30)
    part2(input).println()
}

data class Card(val n: Int, val winning: Set<Int>, val numbers: Set<Int>, var copies: Int = 1)
