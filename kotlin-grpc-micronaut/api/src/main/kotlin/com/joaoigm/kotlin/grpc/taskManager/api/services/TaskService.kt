package com.joaoigm.kotlin.grpc.taskManager.api.services

import com.joaoigm.kotlin.grpc.taskManager.api.*
import com.joaoigm.kotlin.grpc.taskManager.api.persistence.entities.TaskEntity
import com.joaoigm.kotlin.grpc.taskManager.api.persistence.repositories.TaskRepository
import io.micronaut.tracing.annotation.ContinueSpan
import javax.inject.Singleton

@Suppress("UNUSED_PARAMETER")
@Singleton
class TaskService(
        private val taskRepo: TaskRepository
) {

    fun createTask(request: CreateRequest): Task {
        return create(request)
    }

    fun finalizeTask(request: TaskWithIdRequest): Task {
        return finalize(request)
    }

    fun excludeTask(request: TaskWithIdRequest): Task {
        return exclude(request)
    }

    fun findAllTasks(request: FindAllRequest): FindAllResponse {
        return findAll(request)
    }

    fun findAllTasksByStatus(request: FindAllByStatusRequest): FindAllByStatusResponse {
        return findAllByStatus(request)
    }

    fun findTaskById(request: TaskWithIdRequest): Task {
        return findById(request)
    }

    @ContinueSpan
    private fun findById(request: TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun findAllByStatus(request: FindAllByStatusRequest): FindAllByStatusResponse {
        val tasks = taskRepo.findAllByStatus(
                com.joaoigm.kotlin.grpc.taskManager.api.persistence.entities.TaskStatus.valueOf(
                        request.status.name
                )
        )
        return FindAllByStatusResponse.newBuilder().addAllTasks(tasks.map { t ->
            Task.newBuilder()
                    .setId(t.id!!)
                    .setTitle(t.title)
                    .setStatus(TaskStatus.valueOf(t.status.name))
                    .build() }
        ).build()
    }

    @ContinueSpan
    private fun findAll(request: FindAllRequest): FindAllResponse {
        val tasks = taskRepo.findAll()
        return FindAllResponse.newBuilder().addAllTasks(
                tasks.map { t ->
                    Task.newBuilder()
                        .setId(t.id!!)
                        .setTitle(t.title)
                        .setStatus(TaskStatus.valueOf(t.status.name))
                        .build() }
        ).build()
    }

    @ContinueSpan
    private fun exclude(request: TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        task.status = com.joaoigm.kotlin.grpc.taskManager.api.persistence.entities.TaskStatus.EXCLUIDO
        taskRepo.update(task)
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun finalize(request: TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        task.status = com.joaoigm.kotlin.grpc.taskManager.api.persistence.entities.TaskStatus.CONCLUIDO
        taskRepo.update(task)
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun create(request: CreateRequest): Task {

        val task = taskRepo.save(TaskEntity(
                request.title,
                com.joaoigm.kotlin.grpc.taskManager.api.persistence.entities.TaskStatus.ABERTO))
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(TaskStatus.valueOf(task.status.name))
                .build()
    }
}