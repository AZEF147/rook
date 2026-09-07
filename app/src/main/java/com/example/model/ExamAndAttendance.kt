package com.example.model

data class Question(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanationAr: String
)

data class Exam(
    val id: String,
    val title: String,
    val subject: Subject,
    val grade: SchoolGrade,
    val teacherId: String,
    val teacherName: String,
    val teacherCode: String,
    val durationMinutes: Int,
    val questions: List<Question>,
    val isSurprise: Boolean = false,
    val createdAt: String = "2025-03-01",
    val maxScore: Int = 20
)

data class StudentExamResult(
    val id: String,
    val examId: String,
    val examTitle: String,
    val studentId: String,
    val studentName: String,
    val grade: SchoolGrade,
    val score: Int,
    val maxScore: Int,
    val percentage: Int,
    val completedAt: String = "2025-03-02"
)

enum class AttendanceStatus(val titleAr: String) {
    ATTENDED("حاضر"),
    ABSENT("غائب"),
    LATE("متأخر")
}

data class AttendanceRecord(
    val id: String,
    val studentId: String,
    val studentName: String,
    val studentPhone: String,
    val grade: SchoolGrade,
    val teacherCode: String,
    val lessonTitle: String,
    val date: String,
    val status: AttendanceStatus,
    val watchPercentage: Int, // 0 - 100%
    val lastExamScore: String = "18/20",
    val notes: String = "شاهد الحصة بتركيز كامل"
)
