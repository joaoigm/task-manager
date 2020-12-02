package com.joaoigm.kotlin.persistence.repositories

import com.joaoigm.kotlin.persistence.entities.TaskEntity
import com.joaoigm.kotlin.persistence.entities.TaskStatus
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface TaskRepository : CrudRepository<TaskEntity, Long> {

    fun findAllByStatus(status: TaskStatus): Iterable<TaskEntity>
}