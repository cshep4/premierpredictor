package com.cshep4.premierpredictor.data.api

data class ResultLinks(val self: Link = Link(),
                       val competition: Link = Link(),
                       val homeTeam: Link? = null,
                       val awayTeam: Link? = null)

data class Link(val href: String = "")