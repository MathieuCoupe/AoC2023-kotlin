private const val DAY = "Day06"

fun main() {
    fun parse(input: List<String>): List<Race> {
        val times = input[0].substring("Time:".length).trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        val distances = input[1].substring("Distance:".length).trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        return times.zip(distances).map { Race(time = it.first, distance = it.second) }.toList()
    }

    fun part1(input: List<String>): Long {
        val races = parse(input)
        var result = 1L

        races.forEach { race ->
            val winningRaces = LongRange(0, race.time).count { t ->
                val distance = t * (race.time - t)

                // check if race finished in time
                distance > race.distance
            }

            result *= winningRaces
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val time = input[0].substring("Time:".length).replace(" ", "").toLong()
        val distance = input[1].substring("Distance:".length).replace(" ", "").toLong()

        println("Race $time for $distance")

        val winningRaces = LongRange(0, time).count { t ->
            val d = t * (time - t)

            // check if race finished in time
            d > distance
        }

        return winningRaces.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(testInput) == 288L)

    val input = readInput(DAY)
    part1(input).println()

    check(part2(testInput) == 71503L)
    part2(input).println()
}

data class Race(val time: Long, val distance: Long)
