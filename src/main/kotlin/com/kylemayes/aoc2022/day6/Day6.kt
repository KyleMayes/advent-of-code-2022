// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day6

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve

typealias ParsedInput = List<Char>

class Day6 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()[0]
        .toCharArray()
        .toList()

    override fun solvePart1(input: ParsedInput) = solve(input, 4)
    override fun solvePart2(input: ParsedInput) = solve(input, 14)

    private fun solve(input: ParsedInput, unique: Int): Int {
        val ring = ArrayDeque<Char>()

        for (i in input.indices) {
            ring.addLast(input[i])
            if (ring.size > unique) ring.removeFirst()
            if (ring.size == unique && ring.toSet().size == unique) {
                return i + 1
            }
        }

        throw RuntimeException()
    }
}

fun main() = solve({ Day6() }, ResourceInput("day6.txt"))
