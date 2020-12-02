package com.joaoigm.kotlin.grpc.taskManager.api.endpoint

import com.joaoigm.kotlin.grpc.taskManager.api.*
import com.joaoigm.kotlin.grpc.taskManager.api.services.TaskService
import javax.inject.Singleton

@Singleton
class TaskManagerEndpoint(
        private val taskService: TaskService
)
    : TaskManagerServiceGrpcKt.TaskManagerServiceCoroutineImplBase() {

    @Override
    override suspend fun create(request: CreateRequest): Task {
        return taskService.createTask(request)
    }

    @Override
    override suspend fun finalize(request: TaskWithIdRequest): Task {
        return taskService.finalizeTask(request)
    }
    @Override
    override suspend fun exclude(request: TaskWithIdRequest): Task {
        return taskService.excludeTask(request)
    }
    @Override
    override suspend fun findById(request: TaskWithIdRequest): Task {
        return taskService.findTaskById(request)
    }
    @Override
    override suspend fun findAll(request: FindAllRequest): FindAllResponse {
        return taskService.findAllTasks(request)
    }
    @Override
    override suspend fun findAllByStatus(request: FindAllByStatusRequest): FindAllByStatusResponse {
        return taskService.findAllTasksByStatus(request)
    }
}