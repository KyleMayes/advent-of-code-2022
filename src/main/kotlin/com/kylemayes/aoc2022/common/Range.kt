package com.kylemayes.aoc2022.common

fun <T : Comparable<T>> ClosedRange<T>.contains(other: ClosedRange<T>) =
    start <= other.start && endInclusive >= other.endInclusive

fun IntRange.union(other: IntRange) = IntRange(
    minOf(first, other.first),
    maxOf(last, other.last),
)

fun LongRange.union(other: LongRange) = LongRange(
    minOf(first, other.first),
    maxOf(last, other.last),
)

fun IntRange.overlap(other: IntRange): IntRange? {
    return if (last >= other.first && other.last >= first) {
        IntRange(maxOf(first, other.first), minOf(last, other.last))
    } else {
        return null
    }
}

fun LongRange.overlap(other: LongRange): LongRange? {
    return if (last >= other.first && other.last >= first) {
        LongRange(maxOf(first, other.first), minOf(last, other.last))
    } else {
        return null
    }
}
