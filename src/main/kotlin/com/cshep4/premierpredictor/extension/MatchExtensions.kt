package com.cshep4.premierpredictor.extension

import com.cshep4.premierpredictor.data.PredictedMatch

fun List<PredictedMatch>.getMatchById(id: Long?): PredictedMatch {
    return this.first { it.id == id }
}