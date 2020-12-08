package com.entibaldi.adventofcode2020

import java.io.File

data class ExecResult(
    val lines: List<Int>,
    val terminal: Boolean
)

fun main() {
    val input = File("res/8/input.txt").readLines()
        .map { it.split(" ") }
        .map { it[0] to it[1].toInt() }
    // part1
    val origResult = executeWithInput(input)
    // part2
    val origResultReversedSequence = origResult.lines.asReversed().asSequence().withIndex()
    var execResult = origResult
    var lastIndexCheck = -1
    while (!execResult.terminal) {
        val correctionLine = origResultReversedSequence.first { (i, line) ->
            i > lastIndexCheck && input[line].first.matches("(jmp)|(nop)".toRegex())
        }.value
        lastIndexCheck += 1
        val correctionCmd = if (input[correctionLine].first == "jmp") "nop" else "jmp"
        val correction: Pair<String, Int> = correctionCmd to input[correctionLine].second
        val correctedInput = input.toMutableList().apply { set(correctionLine, correction) }
        execResult = executeWithInput(correctedInput, true)
    }
}

fun executeWithInput(input: List<Pair<String, Int>>, printOnlyTerminal: Boolean = false): ExecResult {
    var accumulator = 0
    var line = 0
    var terminal = false
    val executedLines = linkedSetOf<Int>()
    while (!executedLines.contains(line)) {
        if (line == input.size) {
            terminal = true
            break
        }
        executedLines.add(line)
        val (cmd, jump) = input[line]
        when (cmd) {
            "acc" -> {
                accumulator += jump
                line += 1
            }
            "jmp" -> {
                line += jump
            }
            "nop" -> {
                line += 1
            }
        }
    }
    if (!printOnlyTerminal || terminal) println(accumulator)
    return ExecResult(executedLines.toList(), terminal)
}
