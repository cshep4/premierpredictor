package com.cshep4.premierpredictor.repository.sql

import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_SCORE_AND_RANK
import com.cshep4.premierpredictor.constant.Queries.QUERY_GET_USER_BY_EMAIL
import com.cshep4.premierpredictor.constant.Queries.QUERY_RESET_USER_PASSWORD
import com.cshep4.premierpredictor.constant.Queries.QUERY_SAVE_USER
import com.cshep4.premierpredictor.constant.Queries.QUERY_SET_USER_SIGNATURE
import com.cshep4.premierpredictor.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    @Query(value = QUERY_GET_USER_BY_EMAIL, nativeQuery = true)
    fun findByEmail(email: String): Optional<UserEntity>

    @Query(value = QUERY_SAVE_USER, nativeQuery = true)
    fun save(email: String, password: String): Optional<UserEntity>?

    @Query(value = QUERY_GET_SCORE_AND_RANK, nativeQuery = true)
    fun getUserRankAndScore(): List<Array<Any>>

    @Modifying(clearAutomatically = true)
    @Query(value = QUERY_SET_USER_SIGNATURE, nativeQuery = true)
    fun setUserSignature(signature: String, email: String): Int

    @Modifying(clearAutomatically = true)
    @Query(value = QUERY_RESET_USER_PASSWORD, nativeQuery = true)
    fun resetUserPassword(password: String, email: String, signature: String): Int
}