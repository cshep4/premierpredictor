package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.entity.LeagueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeagueRepository : JpaRepository<LeagueEntity, Long>