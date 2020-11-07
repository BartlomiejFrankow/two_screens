package com.example.twoscreens.modules

import com.example.twoscreens.firebase.*
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val firebaseModule = module {
    factory<CreateTask> { CreateTaskImpl(FirebaseFirestore.getInstance()) }
    factory<GetTasks> { GetTasksImpl(FirebaseFirestore.getInstance()) }
    factory<UpdateTask> { UpdateTaskImpl(FirebaseFirestore.getInstance()) }
}