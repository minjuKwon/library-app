package com.example.library.fake

import com.example.library.core.TimeProvider

class FakeTimeProvider:TimeProvider {
    override fun now(): Long = 1_700_000_000_000L//2023년
}