package com.cshep4.premierpredictor.service.fixtures

import com.cshep4.premierpredictor.data.Match
import com.cshep4.premierpredictor.entity.MatchEntity
import com.cshep4.premierpredictor.repository.sql.FixturesRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(MockitoJUnitRunner::class)
internal class UpdateFixturesServiceTest {
    @Mock
    private lateinit var fixturesRepository: FixturesRepository

    @InjectMocks
    private lateinit var updateFixturesService: UpdateFixturesService

    @Test
    fun `'update' saves all matches and returns list of matches`() {
        val matches = listOf(Match(), Match(), Match(), Match())
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        whenever(fixturesRepository.saveAll(matchEntities)).thenReturn(matchEntities)

        val result = updateFixturesService.update(matches)

        assertThat(result, Is(matches))
        verify(fixturesRepository).saveAll(matchEntities)
    }

    @Test
    fun `'update' does not save if no matches and returns empty list`() {
        val result = updateFixturesService.update(emptyList())

        assertThat(result, Is(emptyList()))
        verify(fixturesRepository, times(0)).saveAll(emptyList())
    }
}