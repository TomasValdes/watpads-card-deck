package com.sordle.watpadsCardDeck.repository

import com.sordle.watpadsCardDeck.models.User
import org.springframework.data.cassandra.repository.CassandraRepository
import java.util.*

interface UserProfileRepository : CassandraRepository<User, UUID> {

}