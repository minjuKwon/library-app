package com.example.library.core

class DefaultTimeProvider:TimeProvider {
    override fun now(): Long = System.currentTimeMillis()
}