package com.example.sparktestdemchenko.domain.mapper

import com.example.sparktestdemchenko.domain.model.AppQuery
import com.example.sparktestdemchenko.domain.mapper.base.Mapper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class FirebaseQueryMapper(private val reference: DatabaseReference) : Mapper<AppQuery, Query> {
    override fun map(source: AppQuery): Query {
        var query = reference.orderByKey()
        if (source.startItem != null) {
            query = query.startAfter(source.startItem)
        }
        return query.limitToFirst(source.limit)
    }

    override fun mapReverse(source: Query): AppQuery {
        return AppQuery(limit = Int.MAX_VALUE)
    }
}