package com.cshep4.premierpredictor.extension

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.PredictedMatch
import com.cshep4.premierpredictor.data.api.live.match.MatchFacts

fun List<PredictedMatch>.getMatchById(id: Long?): PredictedMatch =
        first { it.id == id }

fun MatchFacts?.isPlaying(): Boolean =
        this?.status != null && status != "" && status != "FT" && status != "Postp." && status != "Cancl." && status != "Awarded" && status != "Aban." && ":" !in status!!

fun Match.hasPlayed(): Boolean = this.hGoals != null && this.aGoals != null