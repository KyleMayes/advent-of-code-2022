@file:OptIn(ExperimentalStdlibApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.kylemayes.aoc2022.common.regex

import com.google.gson.Gson
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/** Extracts one patterned value from the entirety of a string. */
fun <T : Any> extractOne(string: String, clazz: KClass<T>): T {
    val extractor = clazz.java.getAnnotation(Extractor::class.java)

    val matcher = Pattern.compile(extractor.regex).matcher(string)
    if (!matcher.matches()) {
        throw Error("/${extractor.regex}/ failed to match string: '$string'")
    }

    return extract(matcher, clazz)
}

/** Extracts multiple patterned values from the entirety of a string. */
fun <T : Any> extractMany(string: String, clazz: KClass<T>): List<T> {
    val extractor = clazz.java.getAnnotation(Extractor::class.java)

    val values = mutableListOf<T>()
    val matcher = Pattern.compile(extractor.regex).matcher(string)
    while (matcher.find()) {
        values.add(extract(matcher, clazz))
    }

    if (!matcher.hitEnd()) {
        throw Error("/${extractor.regex}/ failed to match string: '$string' ")
    }

    return values
}

private fun <T : Any> extract(matcher: Matcher, clazz: KClass<T>): T {
    val arguments = mutableListOf<Any>()

    for (parameter in clazz.primaryConstructor!!.parameters) {
        val value = matcher.group(parameter.name)
        if (parameter.type == typeOf<Char>()) {
            arguments.add(value[0])
        } else if (parameter.type != typeOf<String>()) {
            arguments.add(Gson().fromJson(value, parameter.type.javaType))
        } else {
            arguments.add(value)
        }
    }

    return clazz.primaryConstructor!!.call(*arguments.toTypedArray())
}
