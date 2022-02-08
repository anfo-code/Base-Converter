package com.example.fomenko_3

import kotlin.math.pow
import kotlin.properties.Delegates

private var startBase by Delegates.notNull<Int>()
private var endBase by Delegates.notNull<Int>()
lateinit var numberToTransfer: String
var legalArguments = true

fun main() {
    getNumbers()
    checkBase()
    checkNum()
    if (legalArguments) {
        calculateTheNumber()
    }
}

fun getNumbers() {
    println("Enter the number, you want to transfer:")
    numberToTransfer = readLine().toString().uppercase().replace(",", ".")
    println("Enter the base of this number:")
    startBase = readLine()!!.toInt()
    println("Enter the result base:")
    endBase = readLine()!!.toInt()
}

fun checkBase() {
    if (startBase !in 2..36 || endBase !in 2..36) {
        legalArguments = false
        println("Please, enter a correct base")
    }
}

fun checkNum() {
    var i = 0
    while (i <= numberToTransfer.length - 1) {
        if (startBase <= 10) {
            if ((numberToTransfer[i].code) in 48..(startBase + 54)
                || numberToTransfer[i].code == 46
            ) {
                i++
            } else if (i == 0 && numberToTransfer[i].code == 45) {
                i++
            } else {
                legalArguments = false
                println("Please, enter a correct number.")
                break
            }
        } else {
            if (numberToTransfer[i].code in 48..(startBase + 54)
                && numberToTransfer[i].code !in 58..64
                || numberToTransfer[i].code == 46
            ) {
                i++
            } else if (i == 0 && numberToTransfer[i].code == 45) {
                i++
            } else {
                legalArguments = false
                println("Please, enter a correct number.")
                break
            }
        }
    }
}

fun calculateTheNumber() {
    var isNegative = false
    var numBeforeDot = ""
    var numAfterDot = ""
    var result = ""

    if (numberToTransfer.contains("-")) {
        isNegative = true
        numberToTransfer = numberToTransfer.substring(1)
    }

    if (numberToTransfer.contains(".")) {
        val splitNumber = numberToTransfer.split(".")
        numBeforeDot = splitNumber[0]
    }

    val tenthNum = transferNumToTenthSystem(numBeforeDot.length)

    if (tenthNum.toString().contains(".")) {
        val splitNumber = tenthNum.toString().split(".")
        numBeforeDot = splitNumber[0]
        numAfterDot = splitNumber[1]
        result = transferNumToEndSystem(numBeforeDot.toInt(), numAfterDot.toInt())
    } else {
        result = transferNumToEndSystem(tenthNum.toInt(), null)
    }

    if (isNegative) {
     result = "-$result"
    }
    println(result)
}

fun transferNumToTenthSystem(charsToDot: Int): Double {
    var i = 0
    var degreeCount = charsToDot - 1
    var tenthNumber = 0.0
    while (i <= (numberToTransfer.length - 1)) {
        if (numberToTransfer[i].code !== 46) {
            tenthNumber += getNumFromCode(numberToTransfer[i].code) * startBase.toDouble().pow(
                (degreeCount).toDouble()
            )
            degreeCount--
            i++
        } else {
            i++
        }
    }
    return tenthNumber
}

fun transferNumToEndSystem(numBeforeDot: Int, numAfterDot: Int?): String {
    var intNum = numBeforeDot
    var fractionalNum = numAfterDot
    val stringBuilder = StringBuilder()
    var result = ""
    while (true) {
        if (intNum < endBase) {
            stringBuilder.append(intNum % endBase)
            result = stringBuilder.reverse().toString()
            break
        } else {
            stringBuilder.append(intNum % endBase)
            intNum /= endBase
        }
    }
    if (fractionalNum != null) {
        stringBuilder.clear()
        while (true) {
            if (fractionalNum !== 0) {
                fractionalNum *= endBase
                var roundedToWhole = fractionalNum.toInt()
                stringBuilder.append(roundedToWhole)
                fractionalNum -= roundedToWhole
            } else {
                stringBuilder.append(intNum % endBase)
                result += ".$stringBuilder"
                break
            }
        }
    }
    return result
}

fun getNumFromCode(code: Int): Double {
    if (code in 48..57) {
        return (code - 48).toDouble()
    } else if (code in 65..90) {
        return (code - 55).toDouble()
    }
    return 0.0
}

