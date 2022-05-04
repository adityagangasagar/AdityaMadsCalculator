package com.aditya.adityamadscalculator.models

class HistoryData {
    var expression: String? = null
    var result = 0
    var count = 0

    constructor() {}
    constructor(expression: String?, result: Int, count: Int) {
        this.expression = expression
        this.result = result
        this.count = count
    }
}