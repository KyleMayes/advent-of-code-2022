// SPDX-License-Identifier: Apache-2.0

package com.kylemayes.aoc2022.day7

import com.kylemayes.aoc2022.common.Input
import com.kylemayes.aoc2022.common.ResourceInput
import com.kylemayes.aoc2022.common.Solution
import com.kylemayes.aoc2022.common.readLines
import com.kylemayes.aoc2022.common.solve

data class Dir(
    val name: String?,
    val files: MutableMap<String, Long> = mutableMapOf(),
    val directories: MutableMap<String, Dir> = mutableMapOf(),
)

sealed class Entry {
    data class Dir(val name: String) : Entry()
    data class File(val name: String, val bytes: Long) : Entry()
}

sealed class Command {
    data class Cd(val path: String) : Command()
    data class Ls(val entries: List<Entry>) : Command()
}

typealias ParsedInput = List<Command>

class Day7 : Solution<ParsedInput> {
    override fun parse(input: Input): ParsedInput {
        val commands = mutableListOf<Command>()

        var entries: MutableList<Entry>? = null
        for (line in input.readLines()) {
            if (line.startsWith("$") && entries != null) {
                commands.add(Command.Ls(entries))
                entries = null
            }

            if (line.startsWith("$ cd")) {
                commands.add(Command.Cd(line.substring(5)))
            } else if (line == "$ ls") {
                entries = mutableListOf()
            } else if (line.startsWith("dir")) {
                entries!!.add(Entry.Dir(line.substring(4)))
            } else {
                val values = line.split(" ")
                entries!!.add(Entry.File(values[1], values[0].toLong()))
            }
        }

        if (entries != null) {
            commands.add(Command.Ls(entries))
        }

        return commands
    }

    override fun solvePart1(input: ParsedInput): Long {
        val root = getFilesystem(input)

        val sizes = mutableMapOf<Dir, Long>()
        getSizes(root, sizes)

        return sizes.values.filter { it <= 100000 }.sum()
    }

    override fun solvePart2(input: ParsedInput): Long {
        val root = getFilesystem(input)

        val sizes = mutableMapOf<Dir, Long>()
        getSizes(root, sizes)

        val free = 70000000 - sizes[root]!!
        val missing = 30000000 - free

        return sizes.entries.sortedBy { it.value }.find { it.value > missing }!!.value
    }

    private fun getFilesystem(input: ParsedInput): Dir {
        val root = Dir(null)
        var path = mutableListOf<Dir>()

        for (command in input) {
            when (command) {
                is Command.Cd -> when (command.path) {
                    "/" -> path = mutableListOf(root)
                    ".." -> path.removeLast()
                    else -> {
                        val current = path.last()
                        val next = current.directories.computeIfAbsent(command.path) { Dir(command.path) }
                        path.add(next)
                    }
                }
                is Command.Ls -> command.entries.forEach { entry ->
                    when(entry) {
                        is Entry.File -> path.last().files[entry.name] = entry.bytes
                        is Entry.Dir -> path.last().directories.computeIfAbsent(entry.name) { Dir(entry.name) }
                    }
                }
            }
        }

        return root
    }

    private fun getSizes(dir: Dir, sizes: MutableMap<Dir, Long>): Long {
        val cached = sizes[dir]
        if (cached != null) {
            return cached
        }

        val size = dir.files.values.sum() + dir.directories.values.sumOf { getSizes(it, sizes) }
        sizes[dir] = size
        return size
    }
}

fun main() = solve({ Day7() }, ResourceInput("day7.txt"))
