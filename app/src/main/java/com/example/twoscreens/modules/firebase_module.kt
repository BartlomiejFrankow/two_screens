package com.example.twoscreens.modules

import com.example.twoscreens.firebase.*
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {
    factory<CreateTask> { CreateTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory<TasksCollection> { TasksCollectionImpl(FirebaseFirestore.getInstance(), get()) }
    factory<UpdateTask> { UpdateTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory<DeleteTask> { DeleteTaskImpl(FirebaseFirestore.getInstance(), get()) }
    factory { FirebaseErrorExecutor(androidContext(), get()) }
}