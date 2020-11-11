package com.example.network

import com.example.domain.CreateTask
import com.example.domain.DeleteTask
import com.example.domain.ObserveTasks
import com.example.domain.UpdateTask
import com.example.network.impl.CreateTaskImpl
import com.example.network.impl.DeleteTaskImpl
import com.example.network.impl.ObserveTasksImpl
import com.example.network.impl.UpdateTaskImpl
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val networkModule = module {
    factory<CreateTask> { CreateTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory<ObserveTasks> { ObserveTasksImpl(FirebaseFirestore.getInstance(), get()) }
    factory<UpdateTask> { UpdateTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory<DeleteTask> { DeleteTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory { ErrorExecutor(get()) }
}