package com.cshep4.premierpredictor.data.api

import java.util.*

data class TeamsAPIResult(val _links: ResultLinks? = null,
                          val count:Int = 0,
                          val fixtures:Array<TeamResult>? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeamsAPIResult

        if (_links != other._links) return false
        if (count != other.count) return false
        if (!Arrays.equals(fixtures, other.fixtures)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _links?.hashCode() ?: 0
        result = 31 * result + count
        result = 31 * result + (fixtures?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}

data class TeamResult(val _links: ResultLinks? = null,
                      val name:String = "",
                      val code:String? = null,
                      val shortName:String? = null,
                      val squadMarketValue: String? = null,
                      val crestUrl:String? = null)