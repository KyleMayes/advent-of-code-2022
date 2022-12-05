package com.kylemayes.aoc2022.common.regex

import org.intellij.lang.annotations.Language

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Extractor(@Language("RegExp") val regex: String)
