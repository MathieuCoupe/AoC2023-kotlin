import kotlin.math.min

private const val DAY = "Day05"

fun main() {
    fun parse_layer(input: List<String>, name: String): Map<LongRange, LongRange> {
        // find start of layer
        val start = input.indexOf("$name map:") + 1
        val indexOfNextEmptyLine = input.subList(start, input.size).indexOfFirst { it.isBlank() }
        val end = if (indexOfNextEmptyLine >= 0) start + indexOfNextEmptyLine else input.size

//        println("$name: $start -> $end")

        return input.subList(start, end).map { line ->
            val (destination, source, length) = line.split(" ").map { it.toLong() }
//            println("Range : $destination -> $source, length = $length")
            LongRange(source, source + length - 1) to LongRange(destination, destination + length - 1)
        }.toMap()
    }

    fun parse(input: List<String>): layers {
        return layers(
            seeds = input[0].substring("seeds: ".length).split(" ").map { it.trim().toLong() },
            layers = listOf(
                parse_layer(input, "seed-to-soil"),
                parse_layer(input, "soil-to-fertilizer"),
                parse_layer(input, "fertilizer-to-water"),
                parse_layer(input, "water-to-light"),
                parse_layer(input, "light-to-temperature"),
                parse_layer(input, "temperature-to-humidity"),
                parse_layer(input, "humidity-to-location")
            )
        )
    }

    fun part1(data: layers): Long {
        with(data) {
            var lowestLocation = Long.MAX_VALUE
            seeds.stream().map { seed ->
                // pass seed through each layer
                var position = seed
//                println("\nSeed start at $position")

                layers.forEach { layer ->
//                    println("layer : $layer")
                    val matchs = layer.filter {
//                        println("Check if position $position is in ${it.key} : ${position in it.key}")

                        position in it.key
                    }.toList()

                    if (matchs.size == 1) {
                        val source = matchs[0].first
                        val destination = matchs[0].second

//                        println("Found $position in $source")
                        position = position - source.start + destination.start
//                        println("Seed moved to $position")
                    }
                }

//                println("Seed moved from $seed to $position")
                position
            }.forEach {
                lowestLocation = min(it, lowestLocation)
            }

            return lowestLocation
        }
    }

    fun part2(data: layers): Long {
        with(data) {
            var lowestLocation = Long.MAX_VALUE

            // convert list to ranges
            seeds.zipWithNext().filterIndexed { index, _ -> index % 2 == 0 }.stream().parallel().forEach { seedRange ->
                println("Seeds in ${LongRange(seedRange.first, seedRange.first + seedRange.second - 1)}")
                for (seed in LongRange(seedRange.first, seedRange.first + seedRange.second - 1)) {
                    // pass seed through each layer
                    var position = seed
//                    println("Seed start at $position")

                    layers.forEach { layer ->
//                    println("layer : $layer")
                        val matchs = layer.filter {
//                        println("Check if position $position is in ${it.key} : ${position in it.key}")

                            position in it.key
                        }.toList()

                        if (matchs.size == 1) {
                            val source = matchs[0].first
                            val destination = matchs[0].second

//                        println("Found $position in $source")
                            position = position - source.first + destination.first
//                        println("Seed moved to $position")
                        }
                    }

//                    println("Seed moved from $seed to $position ($lowestLocation)")
                    lowestLocation = min(position, lowestLocation)
                }

            }

            return lowestLocation
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY + "_test")
    check(part1(parse(testInput)) == 35L)

    val input = readInput(DAY)
    part1(parse(input)).println()

    check(part2(parse(testInput)) == 46L)
    part2(parse(input)).println()
}

data class layers(
    val seeds: List<Long>,
    val layers: List<Map<LongRange, LongRange>>
)
