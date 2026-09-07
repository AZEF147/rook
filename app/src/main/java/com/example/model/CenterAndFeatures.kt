package com.example.model

enum class FeatureCategory(val titleAr: String, val iconEmoji: String) {
    SECURITY("الأمان والحماية", "🛡️"),
    GAMIFICATION("التحفيز والـ Gamification", "🎮"),
    AI_TOOLS("الذكاء الاصطناعي والأدوات", "🤖"),
    MANAGEMENT("الإدارة والتقارير", "📊"),
    UX_UI("تجربة المستخدم والواجهة", "📱"),
    COMMERCE("التجارة والدفع الإلكتروني", "💰"),
    COMMUNICATION("التواصل والتسويق", "📣")
}

data class FeatureItem(
    val number: Int,
    val title: String,
    val description: String,
    val category: FeatureCategory,
    val isInteractive: Boolean = true,
    val actionType: String = "info"
)

data class CenterHall(
    val id: String,
    val centerName: String,
    val hallName: String,
    val teacherName: String,
    val subject: Subject,
    val grade: SchoolGrade,
    val dayTime: String,
    val capacity: Int,
    val enrolledCount: Int,
    val location: String = "مدينة نصر - القاهرة"
)
