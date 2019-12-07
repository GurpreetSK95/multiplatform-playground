package com.gurpreetsk.tvdb.shared

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
actual annotation class SaveState // FIXME(gs) iOS analogous to Parcelize??

actual interface Restorable // FIXME(gs) iOS analogous to Parcelable??