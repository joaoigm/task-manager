package com.joaoigm.kotlin.services

import com.joaoigm.kotlin.grpc.FindAllByStatusResponse
import com.joaoigm.kotlin.grpc.FindAllResponse
import com.joaoigm.kotlin.grpc.Task
import com.joaoigm.kotlin.persistence.entities.TaskEntity
import com.joaoigm.kotlin.persistence.entities.TaskStatus
import com.joaoigm.kotlin.persistence.repositories.TaskRepository
import io.micronaut.tracing.annotation.ContinueSpan
import javax.inject.Singleton

@Suppress("UNUSED_PARAMETER")
@Singleton
class TaskService(
        private val taskRepo: TaskRepository
) {

    fun createTask(request: com.joaoigm.kotlin.grpc.CreateRequest): Task {
        return create(request)
    }

    fun finalizeTask(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        return finalize(request)
    }

    fun excludeTask(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        return exclude(request)
    }

    fun findAllTasks(request: com.joaoigm.kotlin.grpc.FindAllRequest): FindAllResponse {
        return findAll(request)
    }

    fun findAllTasksByStatus(request: com.joaoigm.kotlin.grpc.FindAllByStatusRequest): FindAllByStatusResponse {
        return findAllByStatus(request)
    }

    fun findTaskById(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        return findById(request)
    }

    @ContinueSpan
    private fun findById(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun findAllByStatus(request: com.joaoigm.kotlin.grpc.FindAllByStatusRequest): FindAllByStatusResponse {
        val tasks = taskRepo.findAllByStatus(TaskStatus.valueOf(request.status.name))
        return FindAllByStatusResponse.newBuilder().addAllTasks(tasks.map { t ->
            Task.newBuilder()
                    .setId(t.id!!)
                    .setTitle(t.title)
                    .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(t.status.name))
                    .build() }
        ).build()
    }

    @ContinueSpan
    private fun findAll(request: com.joaoigm.kotlin.grpc.FindAllRequest): FindAllResponse {
        val tasks = taskRepo.findAll()
        return FindAllResponse.newBuilder().addAllTasks(
                tasks.map { t ->
                    Task.newBuilder()
                        .setId(t.id!!)
                        .setTitle(t.title)
                        .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(t.status.name))
                        .build() }
        ).build()
    }

    @ContinueSpan
    private fun exclude(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        task.status = TaskStatus.EXCLUIDO
        taskRepo.update(task)
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun finalize(request: com.joaoigm.kotlin.grpc.TaskWithIdRequest): Task {
        val task = taskRepo.findById(request.id).get()
        task.status = TaskStatus.CONCLUIDO
        taskRepo.update(task)
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(task.status.name))
                .build()
    }

    @ContinueSpan
    private fun create(request: com.joaoigm.kotlin.grpc.CreateRequest): Task {

        val task = taskRepo.save(TaskEntity(request.title, TaskStatus.ABERTO))
        return Task.newBuilder()
                .setId(task.id!!)
                .setTitle(task.title)
                .setStatus(com.joaoigm.kotlin.grpc.TaskStatus.valueOf(task.status.name))
                .build()
    }
}