plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.safeargs) apply false
}

// For avoid spam in claude code
gradle.taskGraph.whenReady {
    val lastTask = allTasks.lastOrNull()
    lastTask?.doLast {
        if (this.state.failure != null) return@doLast
        println("✅ TASK SUCCESSFUL. Some messages suppressed by logging.level=warn")
    }
}