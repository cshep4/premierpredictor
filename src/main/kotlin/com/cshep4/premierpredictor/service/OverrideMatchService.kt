package com.cshep4.premierpredictor.service

import com.cshep4.premierpredictor.component.fixtures.FixturesApi
import com.cshep4.premierpredictor.component.override.MatchOverrideMerger
import com.cshep4.premierpredictor.data.MatchWithOverride
import com.cshep4.premierpredictor.data.OverrideMatch
import com.cshep4.premierpredictor.entity.OverrideMatchEntity
import com.cshep4.premierpredictor.repository.sql.OverrideMatchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OverrideMatchService {
    @Autowired
    private lateinit var overrideMatchRepository: OverrideMatchRepository

    @Autowired
    private lateinit var fixturesApi: FixturesApi

    @Autowired
    private lateinit var matchOverrideMerger: MatchOverrideMerger

    fun retrieveAllOverriddenMatches() : List<OverrideMatch> = overrideMatchRepository.findAll()
            .filter { it.hGoals != null && it.aGoals != null }
            .map { it.toDto() }

    fun updateOverrides(overrides: List<OverrideMatch>): List<OverrideMatch> {
        val overrideEntities = overrides.map { OverrideMatchEntity.fromDto(it) }

        val savedOverrides = overrideMatchRepository.saveAll(overrideEntities)

        return savedOverrides.map { it.toDto() }
    }

    fun retrieveAllMatchesWithOverrideScores() : List<MatchWithOverride> {
        val matches = fixturesApi.retrieveMatches()

        if (matches.isEmpty()) {
            return emptyList()
        }

        val overrides = overrideMatchRepository.findAll()
                .map { it.toDto() }

        return matchOverrideMerger.merge(matches, overrides)
    }
}