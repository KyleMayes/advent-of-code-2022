// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day5

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.geometry.Tile
import com.kylemayes.aoc2022.common.readGroups
import com.kylemayes.aoc2022.common.regex.Extractor
import com.kylemayes.aoc2022.common.regex.extractOne
import com.kylemayes.aoc2022.common.solve

@Extractor("move (?<count>\\d+) from (?<source>\\d+) to (?<target>\\d+)")
data class Move(val source: Int, val target: Int, val count: Int)

data class ParsedInput(val stacks: List<MutableList<Char>>, val moves: List<Move>)

class Day5 : Solution<ParsedInput> {
    override fun parse(input: Input): ParsedInput {
        val groups = input.readGroups(trim = false)

        val crates = groups[0]
            .dropLast(1)
            .map { it.chunked(4).map { c -> c[1] } }
        val stacks = Tile(crates) { ' ' }
            .values()
            .columns()
            .map { it.filter { c -> c != ' ' }.reversed().toMutableList() }

        return ParsedInput(
            stacks = stacks,
            moves = groups[1].map { extractOne(it, Move::class) },
        )
    }

    override fun solvePart1(input: ParsedInput) = solve(input) { source, target, count ->
        for (i in 1..count) target.add(source.removeLast())
    }

    override fun solvePart2(input: ParsedInput) = solve(input) { source, target, count ->
        target.addAll(source.subList(source.size - count, source.size))
        for (i in 1..count) source.removeLast()
    }

    private fun solve(
        input: ParsedInput,
        process: (source: MutableList<Char>, target: MutableList<Char>, count: Int) -> Unit
    ): String {
        for (move in input.moves) {
            val source = input.stacks[move.source - 1]
            val target = input.stacks[move.target - 1]
            process(source, target, move.count)
        }

        return input
            .stacks
            .filter { it.isNotEmpty() }
            .map { it.last() }
            .joinToString("")
    }
}

fun main() = solve({ Day5() }, ResourceInput("day5.txt"))
