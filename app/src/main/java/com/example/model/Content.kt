package com.example.model

data class LessonVideo(
    val id: String,
    val title: String,
    val subject: Subject,
    val grade: SchoolGrade,
    val teacherId: String,
    val teacherName: String,
    val teacherCode: String,
    val videoSourceType: VideoSourceType,
    val videoUrlOrPath: String,
    val durationMinutes: Int,
    val viewsCount: Int,
    val description: String,
    val isFreePreview: Boolean = false,
    val uploadDate: String = "2025-02-28",
    val tags: List<String> = listOf("شرح تفصيلي", "حل أسئلة")
)

enum class VideoSourceType(val titleAr: String) {
    EXTERNAL_URL("رابط خارجي (YouTube/Vimeo)"),
    GALLERY_UPLOAD("مرفوع من الاستوديو / الجهاز")
}

data class Book(
    val id: String,
    val title: String,
    val subject: Subject,
    val grade: SchoolGrade,
    val teacherId: String,
    val teacherName: String,
    val teacherCode: String,
    val priceEgp: Double,
    val pagesCount: Int,
    val description: String,
    val sampleSummary: String,
    val downloadsCount: Int = 180,
    val isFree: Boolean = false,
    val releaseDate: String = "2025-02-15"
)
