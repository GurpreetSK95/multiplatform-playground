package com.gurpreetsk.tvdb.shared

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class SaveState()

expect interface Restorable
