package com.cshep4.premierpredictor.data

import com.cshep4.premierpredictor.enum.Location
import com.cshep4.premierpredictor.enum.Location.HOME
import com.cshep4.premierpredictor.enum.Result
import com.cshep4.premierpredictor.enum.Result.WIN

data class TeamMatchResult(val result: Result = WIN, val score: String = "", val opponent: String, val location: Location = HOME)