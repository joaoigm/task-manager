package com.joaoigm.kotlin.grpc

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.joaoigm.kotlin.grpc")
		.start()
}

