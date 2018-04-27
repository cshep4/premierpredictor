package com.cshep4.wcpredictor.repository

import com.cshep4.wcpredictor.entity.UserLeagueEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLeagueRepository : JpaRepository<UserLeagueEntity, Long>