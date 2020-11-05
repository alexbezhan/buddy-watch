package me.gmur.buddywatch.auth.adapter.persistence

import me.gmur.buddywatch.auth.domain.model.Token
import me.gmur.buddywatch.auth.domain.port.TokenRepository
import me.gmur.buddywatch.jooq.tables.JToken.TOKEN
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PostgresTokenRepository(
    private val db: DSLContext,
    private val mapper: TokenMapper
) : TokenRepository {

    override fun store(token: Token): Token {
        val record = mapper.mapToEntity(token, db.newRecord(TOKEN))

        record.store()

        return mapper.mapToDomain(record)
    }

    override fun exists(token: Token): Boolean {
        return db.fetchExists(
            db.selectFrom(TOKEN).where(TOKEN.TOKEN_.eq(token.toString()))
        )
    }
}
