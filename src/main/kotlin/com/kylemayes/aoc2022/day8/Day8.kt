// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day8

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.geometry.Point
import com.kylemayes.aoc2022.common.geometry.Tile
import com.kylemayes.aoc2022.common.geometry.toTile
import com.kylemayes.aoc2022.common.solve
import kotlin.math.max

typealias ParsedInput = Tile<Int>

class Day8 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .read()
        .trim()
        .toTile()
        .map { it.digitToInt() }

    override fun solvePart1(input: ParsedInput): Int {
        val visible = mutableSetOf<Point>()

        val rows = input.entries().rows()
        val columns = input.entries().columns()

        val lines = listOf(
            rows,
            columns,
            rows.map { it.reversed() },
            columns.map { it.reversed() },
        ).flatten()

        for (line in lines) {
            var height = -1

            for (tree in line) {
                if (tree.second > height) {
                    visible.add(tree.first)
                }

                height = max(height, tree.second)
            }
        }

        return visible.size
    }

    override fun solvePart2(input: ParsedInput): Int = input.bounds.points().maxOf { point ->
        var score = 1

        for (ray in input.values().rays(point).map { it.second.drop(1) }) {
            val height = input[point]
            var rayScore = 0

            for (tree in ray) {
                rayScore += 1
                if (tree >= height) {
                    break
                }
            }

            score *= rayScore
        }

        score
    }
}

fun main() = solve({ Day8() }, ResourceInput("day8.txt"))
