package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.entity.UserLeagueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLeagueRepository : JpaRepository<UserLeagueEntity, Long>