package com.joaoigm.kotlin.persistence.entities

import javax.persistence.*

@Entity
data class TaskEntity
(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false, length = 100)
        val title: String,
        @Enumerated(EnumType.ORDINAL)
        @Column(nullable = false)
        var status: TaskStatus = TaskStatus.ABERTO
)
{
        constructor(title: String, status: TaskStatus) : this(null, title, status)

}

