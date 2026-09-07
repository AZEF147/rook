package com.example.model

enum class UserRole(val titleAr: String) {
    STUDENT("طالب"),
    TEACHER("معلم / أستاذ"),
    CENTER_ADMIN("مدير سنتر تعليمي")
}

enum class SchoolGrade(val id: String, val titleAr: String, val stageAr: String) {
    PREP_1("prep_1", "الصف الأول الإعدادي", "المرحلة الإعدادية"),
    PREP_2("prep_2", "الصف الثاني الإعدادي", "المرحلة الإعدادية"),
    PREP_3("prep_3", "الصف الثالث الإعدادي", "المرحلة الإعدادية"),
    SEC_1("sec_1", "الصف الأول الثانوي", "المرحلة الثانوية"),
    SEC_2("sec_2", "الصف الثاني الثانوي", "المرحلة الثانوية"),
    SEC_3("sec_3", "الصف الثالث الثانوي", "المرحلة الثانوية")
}

enum class Subject(val id: String, val titleAr: String, val iconEmoji: String) {
    PHYSICS("physics", "الفيزياء", "⚡"),
    CHEMISTRY("chemistry", "الكيمياء", "🧪"),
    BIOLOGY("biology", "الأحياء والجيولوجيا", "🧬"),
    MATH("math", "الرياضيات", "📐"),
    ARABIC("arabic", "اللغة العربية", "📖"),
    ENGLISH("english", "اللغة الإنجليزية", "🌍"),
    FRENCH("french", "اللغة الفرنسية", "🗼"),
    HISTORY("history", "التاريخ والجغرافيا", "🏛️"),
    PHILOSOPHY("philosophy", "الفلسفة وعلم النفس", "💡")
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val grade: SchoolGrade? = null,
    val enrolledTeacherCode: String? = null,
    val avatarUrl: String = "",
    val xpPoints: Int = 1450,
    val streakDays: Int = 12,
    val walletBalanceEgp: Double = 250.0
)

data class TeacherProfile(
    val id: String,
    val name: String,
    val subject: Subject,
    val teacherCode: String,
    val title: String,
    val bio: String,
    val phone: String,
    val supportedGrades: List<SchoolGrade>,
    val studentCount: Int = 340,
    val rating: Double = 4.9
)
