package com.cshep4.premierpredictor.component.score

import com.cshep4.premierpredictor.entity.MatchPredictionResultEntity
import com.cshep4.premierpredictor.entity.UserEntity
import com.cshep4.premierpredictor.repository.sql.PredictedMatchRepository
import com.cshep4.premierpredictor.repository.sql.UserRepository
import com.cshep4.premierpredictor.service.UserScoreService
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.persistence.EntityManager

@RunWith(MockitoJUnitRunner::class)
internal class UserScoreServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var predictedMatchRepository: PredictedMatchRepository

    @Mock
    private lateinit var leagueTableScoreCalculator: LeagueTableScoreCalculator

    @Mock
    private lateinit var matchScoreCalculator: MatchScoreCalculator

    @Mock
    private lateinit var winnerScoreCalculator: WinnerScoreCalculator

    @Mock
    private lateinit var entityManager: EntityManager

    @InjectMocks
    private lateinit var userScoreService: UserScoreService

    @Test
    fun `'updateScores' will get a list of users, add score for individual matches, then league position, then winner and save back to db`() {
        val userEntities = listOf(UserEntity())
        val users = userEntities.map { it.toDto() }

        val predictedMatchEntities = listOf(MatchPredictionResultEntity(hGoals = 1, aGoals = 1))
        val predictedMatches = predictedMatchEntities.map { it.toDto() }

        whenever(userRepository.findAll()).thenReturn(userEntities)
        whenever(predictedMatchRepository.getAllMatchesWithPredictions()).thenReturn(predictedMatchEntities)
        whenever(leagueTableScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(matchScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(winnerScoreCalculator.calculate(users)).thenReturn(users)

        userScoreService.updateScores()

        verify(leagueTableScoreCalculator).calculate(users, predictedMatches)
        verify(matchScoreCalculator).calculate(users, predictedMatches)
        verify(winnerScoreCalculator).calculate(users)
        verify(userRepository).saveAll(userEntities)
        verify(entityManager).clear()
    }

}