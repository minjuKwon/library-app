package com.example.library.data

object QueryNormalizer {
    fun normalizeQuery(raw:String): String{
        return raw
            .trim()
            .lowercase()
            .replace(Regex("\\s+"), " ")
            .replace(Regex("[-_/.,#]"), " ")
            .trim()
    }
}