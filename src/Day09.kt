private const val DAY = "Day09"

fun main() {
    fun parse(input: List<String>): List<List<Int>> {
        return input.map { line -> line.split(' ').map { it.trim().toInt() } }
    }

    fun part1(input: List<String>): Int {
        val sequences = parse(input)

        return sequences.sumOf { ints ->
            var sequence = ints
            val last: MutableList<Int> = mutableListOf()

            // iterate if any number is not zero
            while (sequence.any() { it != 0 }) {
                // keep last item
                last += sequence.last()

                // reduce sequence
                sequence = sequence.zipWithNext().map { it.second - it.first }
            }

            last.sum()
        }
    }

    fun part2(input: List<String>): Int {
        val sequences = parse(input)

        return sequences.sumOf { ints ->
            var sequence = ints
            val first: MutableList<Int> = mutableListOf()

            // iterate if any number is not zero
            while (sequence.any() { it != 0 }) {
                // keep last item
                first += sequence.first()

                // reduce sequence
                sequence = sequence.zipWithNext().map { it.second - it.first }
            }

            var result = 0
            first.reversed().forEach {
                result = it - result
            }

            result
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 114)

    val input = readInput(DAY)
    part1(input).println()

    check(part2(testInput) == 2)
    part2(input).println()
}
