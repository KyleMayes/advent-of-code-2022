// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day4

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.contains
import com.kylemayes.aoc2022.common.overlap
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve

typealias ParsedInput = List<Pair<IntRange, IntRange>>

class Day4 : Solution<ParsedInput> {
    override fun parse(input: Input) = input
        .readLines()
        .map {
            val sections = it.split(Regex("[^\\d+]")).map { s -> s.toInt() }
            IntRange(sections[0], sections[1]) to IntRange(sections[2], sections[3])
        }

    override fun solvePart1(input: ParsedInput) = input.count {
        it.first.contains(it.second) || it.second.contains(it.first)
    }

    override fun solvePart2(input: ParsedInput) = input.count {
        it.first.overlap(it.second) != null
    }
}

fun main() = solve({ Day4() }, ResourceInput("day4.txt"))
