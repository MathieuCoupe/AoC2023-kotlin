
import java.util.*

private const val DAY = "Day08"

fun main() {
    fun parse(input: List<String>): Map<String, Node> {
        val regex = Regex(pattern = "([A-Z0-9]{3}) = \\(([A-Z0-9]{3}), ([A-Z0-9]{3})\\)", options = setOf(RegexOption.IGNORE_CASE))
        var output: MutableMap<String, Node> = mutableMapOf()

        // ignore first lines
        input.subList(2, input.size).map { line ->
            val (a, b, c) = regex.find(line)!!.destructured
            output[a] = Node(b, c)
        }

        return output
    }


    fun part1(input: List<String>): Int {
        val network = parse(input)

        var current = "AAA"
        var steps = 0

        input[0].asSequence().repeatForever().forEach { step ->
            if (current == "ZZZ") {
                return steps
            }

            current = when (step) {
                'L' -> network[current]!!.left
                'R' -> network[current]!!.right
                else -> throw Exception("Step $step not found")
            }
            steps += 1
        }

        // not reached
        return 0
    }

    fun part2(input: List<String>): Long {
        val network = parse(input)

        // find starting points (names end with A)
        val startingPoints = network.keys.filter { it.endsWith('A') }

        // find how many steps are needed for each starting points
        val stepsForEach: List<Long> = startingPoints.map { item ->
            var current = item
            var steps = 0L

            input[0].asSequence().repeatForever().forEach { step ->
                if (current.endsWith('Z')) {
                    return@map steps
                }

                current = when (step) {
                    'L' -> network[current]!!.left
                    'R' -> network[current]!!.right
                    else -> throw Exception("Step $step not found")
                }
                steps += 1
            }

            return@map 0L
        }

        // compute LCM to find the total number of steps needed
        return lcm(stepsForEach)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 6)

    val input = readInput(DAY)
    part1(input).println()

    val testInput_part2 = readInput("Day08_part2_test")
    check(part2(testInput_part2) == 6L)
    part2(input).println()
}

data class Node(val left: String, val right: String)

fun <T> Sequence<T>.repeatForever() = generateSequence(this) { it }.flatten()

private fun gcd(x: Long, y: Long): Long {
    return if ((y == 0L)) x else gcd(y, x % y)
}

fun gcd(vararg numbers: Long): Long {
    return Arrays.stream(numbers).reduce(0) { x: Long, y: Long -> gcd(x, y) }
}

fun lcm(numbers: List<Long>): Long {
    return numbers.stream().reduce(1) { x: Long, y: Long -> x * (y / gcd(x, y)) }
}
