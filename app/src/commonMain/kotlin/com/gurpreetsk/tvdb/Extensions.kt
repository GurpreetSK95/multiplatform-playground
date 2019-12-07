package com.gurpreetsk.tvdb

/*
 * Quick and dirty method to pad a number with `0`s until
 * it's at least `digits` char long
 *
 * `3.padding(3)`    will return "003"
 * `1234.padding(3)` will return "1234"
 **/
fun Number.padding(digits: Int): String {
    var str = "$this"
    while(str.length < digits) {
        str = "0$str"
    }
    return str
}