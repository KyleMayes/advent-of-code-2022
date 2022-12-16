// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day9

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.geometry.Direction
import com.kylemayes.aoc2022.common.geometry.Field
import com.kylemayes.aoc2022.common.geometry.Point
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve
import kotlin.math.absoluteValue

typealias ParsedInput = List<Pair<Direction, Int>>

class Day9 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()
        .map {
            val components = it.split(" ")
            when (components[0]) {
                "D" -> Direction.N
                "R" -> Direction.E
                "U" -> Direction.S
                "L" -> Direction.W
                else -> error("Unreachable.")
            } to components[1].toInt()
        }

    override fun solvePart1(input: ParsedInput) = solve(input, 2)
    override fun solvePart2(input: ParsedInput) = solve(input, 10)

    private fun solve(input: ParsedInput, length: Int): Int {
        val visited = Field<Boolean>()

        val knots = MutableList(length) { Point(0, 0) }
        for ((direction, amount) in input) {
            (1..amount).forEach { _ ->
                knots[0] += direction.delta

                for (index in knots.indices.drop(1)) {
                    val current = knots[index]
                    val previous = knots[index - 1]

                    val distance = current - previous
                    if (distance.x.absoluteValue > 1 || distance.y.absoluteValue > 1) {
                        if (current.y < previous.y) {
                            knots[index] += Point(0, 1)
                        } else if (current.y > previous.y) {
                            knots[index] += Point(0, -1)
                        }

                        if (current.x < previous.x) {
                            knots[index] += Point(1, 0)
                        } else if (current.x > previous.x) {
                            knots[index] += Point(-1, 0)
                        }
                    }
                }

                visited[knots.last()] = true
            }
        }

        return visited.size
    }
}

fun main() = solve({ Day9() }, ResourceInput("day9.txt"))
