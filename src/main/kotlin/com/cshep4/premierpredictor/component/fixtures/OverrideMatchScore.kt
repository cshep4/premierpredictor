package com.cshep4.premierpredictor.component.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.data.OverrideMatch
import org.springframework.stereotype.Component

@Component
class OverrideMatchScore {
    fun update(matches: List<Match>, overrides: List<OverrideMatch>) : List<Match> {
        matches.forEach {
            val id = it.id
            val override = overrides.firstOrNull { it.id == id } ?: return@forEach

            it.hGoals = override.hGoals
            it.aGoals = override.aGoals
        }

        return matches
    }
}