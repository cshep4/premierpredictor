package com.cshep4.wcpredictor.service

import com.cshep4.wcpredictor.component.override.MatchOverrideMerger
import com.cshep4.wcpredictor.data.Match
import com.cshep4.wcpredictor.data.MatchWithOverride
import com.cshep4.wcpredictor.data.OverrideMatch
import com.cshep4.wcpredictor.entity.OverrideMatchEntity
import com.cshep4.wcpredictor.repository.OverrideMatchRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class OverrideMatchServiceTest {
    @Mock
    private lateinit var overrideMatchRepository: OverrideMatchRepository

    @Mock
    private lateinit var fixturesService: FixturesService

    @Mock
    private lateinit var matchOverrideMerger: MatchOverrideMerger

    @InjectMocks
    private lateinit var overrideMatchService: OverrideMatchService

    @Test
    fun `'retrieveAllOverriddenMatches' retrieves all matches where scores have been overridden and no others`() {
        val overriddenMatch1 = OverrideMatch(id = 1, hGoals = 2, aGoals = 0)
        val overriddenMatch2 = OverrideMatch(id = 1, hGoals = 2, aGoals = 0)
        val notOverriddenMatch = OverrideMatch(id = 1, hGoals = null, aGoals = null)

        val overriddenMatches = listOf(overriddenMatch1, overriddenMatch2)
        val overrideMatchRepoResult = listOf(
                OverrideMatchEntity.fromDto(overriddenMatch1),
                OverrideMatchEntity.fromDto(overriddenMatch2),
                OverrideMatchEntity.fromDto(notOverriddenMatch)
        )

        whenever(overrideMatchRepository.findAll()).thenReturn(overrideMatchRepoResult)

        val result = overrideMatchService.retrieveAllOverriddenMatches()

        assertThat(result, `is`(overriddenMatches))
    }

    @Test
    fun `'updateOverrides' sends list of updated overrides to the db and returns th result`() {
        val overrides = listOf(OverrideMatch())
        val overrideEntities = overrides.map { OverrideMatchEntity.fromDto(it) }

        whenever(overrideMatchRepository.saveAll(overrideEntities)).thenReturn(overrideEntities)

        val result = overrideMatchService.updateOverrides(overrides)

        verify(overrideMatchRepository).saveAll(overrideEntities)

        assertThat(result, `is`(overrides))
    }

    @Test
    fun `'retrieveAllMatchesWithOverrideScores' retrieves all matches from API, all overrides then merges them`() {
        val matches = listOf(Match())
        val overrides = listOf(OverrideMatch())
        val overrideEntities = overrides.map { OverrideMatchEntity.fromDto(it) }
        val mergedMatches = listOf(MatchWithOverride())

        whenever(fixturesService.retrieveMatchesFromApi()).thenReturn(matches)
        whenever(overrideMatchRepository.findAll()).thenReturn(overrideEntities)
        whenever(matchOverrideMerger.merge(matches, overrides)).thenReturn(mergedMatches)

        val result = overrideMatchService.retrieveAllMatchesWithOverrideScores()

        assertThat(result, `is`(mergedMatches))

        verify(fixturesService).retrieveMatchesFromApi()
        verify(overrideMatchRepository).findAll()
        verify(matchOverrideMerger).merge(matches, overrides)
    }

    @Test
    fun `'retrieveAllMatchesWithOverrideScores' returns empty list if no result is returned from API`() {
        whenever(fixturesService.retrieveMatchesFromApi()).thenReturn(null)

        val result = overrideMatchService.retrieveAllMatchesWithOverrideScores()

        assertThat(result, `is`(emptyList()))

        verify(fixturesService).retrieveMatchesFromApi()
        verify(overrideMatchRepository, times(0)).findAll()
        verify(matchOverrideMerger, times(0)).merge(any(), any())
    }
}