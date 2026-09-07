package com.example.ui.student

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EduRepository
import com.example.model.*

@Composable
fun StudentDashboard(
    onChangeGradeOrTeacher: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by EduRepository.currentUser.collectAsState()
    val allVideos by EduRepository.videos.collectAsState()
    val allBooks by EduRepository.books.collectAsState()
    val allExams by EduRepository.exams.collectAsState()
    val examResults by EduRepository.studentExamResults.collectAsState()
    val notifications by EduRepository.notifications.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("🎬 الفيديوهات", "📚 الكتب والمذكرات", "📝 الامتحانات", "🏆 لوحة الشرف")

    // Active Dialogs
    var activeVideoForPlayer by remember { mutableStateOf<LessonVideo?>(null) }
    var activeExamForTaking by remember { mutableStateOf<Exam?>(null) }
    var activeBookForReading by remember { mutableStateOf<Book?>(null) }

    // Strict Filter: Only show content for student's grade and teacher code!
    val studentGrade = currentUser.grade ?: SchoolGrade.SEC_3
    val teacherCode = currentUser.enrolledTeacherCode ?: "PHYS-AHMED-2025"

    val studentVideos = remember(allVideos, studentGrade, teacherCode) {
        allVideos.filter { it.grade == studentGrade && (it.teacherCode == teacherCode || teacherCode.isEmpty()) }
    }

    val studentBooks = remember(allBooks, studentGrade, teacherCode) {
        allBooks.filter { it.grade == studentGrade && (it.teacherCode == teacherCode || teacherCode.isEmpty()) }
    }

    val studentExams = remember(allExams, studentGrade, teacherCode) {
        allExams.filter { it.grade == studentGrade && (it.teacherCode == teacherCode || teacherCode.isEmpty()) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Student Status & Teacher Profile Header
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFF6C63FF).copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF6C63FF), Color(0xFF4F46E5))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👨‍🎓", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = currentUser.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF6C63FF).copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = studentGrade.titleAr,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4F46E5),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Switch Grade / Teacher
                        TextButton(
                            onClick = onChangeGradeOrTeacher,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "تغيير", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Enrolled Teacher Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👨‍🏫 معتمد لدى: ", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "أ. أحمد سمير (الفيزياء)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "كود: $teacherCode",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Gamification & Streak Banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1E1B4B), Color(0xFF312E81))
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥 ${currentUser.streakDays} يوم التزام مستمر!", color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "نقاط الخبرة: ${currentUser.xpPoints} XP • المحفظة: ${currentUser.walletBalanceEgp} ج.م", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "الرتبة: عبقري ⭐",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live notification banner if surprise exam or new video exists
        if (notifications.isNotEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEF4444).copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📢", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = notifications.first(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB91C1C),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Content Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }
        }

        // TAB 0: Videos
        if (selectedTabIndex == 0) {
            if (studentVideos.isEmpty()) {
                item {
                    EmptyContentNotice("لا توجد فيديوهات منشورة حالياً لهذه السنة الدراسية.")
                }
            } else {
                items(studentVideos) { video ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF6C63FF).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = video.videoSourceType.titleAr,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4F46E5),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(text = "⏱️ ${video.durationMinutes} دقيقة", fontSize = 11.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = video.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = video.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "👁️ ${video.viewsCount} مشاهدة • ${video.uploadDate}",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )

                                Button(
                                    onClick = { activeVideoForPlayer = video },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("مشاهدة الشرح", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: Books & Booklets
        if (selectedTabIndex == 1) {
            if (studentBooks.isEmpty()) {
                item {
                    EmptyContentNotice("لا توجد مذكرات أو كتب منشورة لهذه السنة حالياً.")
                }
            } else {
                items(studentBooks) { book ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (book.isFree) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (book.isFree) "مجاني 🎁" else "${book.priceEgp} ج.م",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (book.isFree) Color(0xFF059669) else Color(0xFFD97706),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(text = "📄 ${book.pagesCount} صفحة", fontSize = 11.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = book.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = book.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "📥 ${book.downloadsCount} تحميل", fontSize = 10.sp, color = Color.Gray)

                                OutlinedButton(
                                    onClick = { activeBookForReading = book },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تصفح المذكرة", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: Exams
        if (selectedTabIndex == 2) {
            if (studentExams.isEmpty()) {
                item {
                    EmptyContentNotice("لا توجد امتحانات نشطة حالياً. تفقد هذه الصفحة لاحقاً.")
                }
            } else {
                items(studentExams) { exam ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = if (exam.isSurprise) BorderStroke(1.5.dp, Color(0xFFEF4444)) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (exam.isSurprise) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFEF4444).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "⚡ امتحان مفاجئ عاجل",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFDC2626),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF10B981).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "📝 امتحان تجريبي",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF059669),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(text = "⏱️ المدة: ${exam.durationMinutes} دقيقة", fontSize = 11.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = exam.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "عدد الأسئلة: ${exam.questions.size} • الدرجة العظمى: ${exam.maxScore} درجة",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { activeExamForTaking = exam },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (exam.isSurprise) Color(0xFFEF4444) else Color(0xFF10B981)
                                )
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (exam.isSurprise) "بدء الامتحان المفاجئ الآن ⚡" else "بدء الامتحان التجريبي",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Student Past Results
            if (examResults.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "سجل نتائجي السابقة:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                items(examResults) { res ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = res.examTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(text = "تاريخ الإنجاز: ${res.completedAt}", fontSize = 10.sp, color = Color.Gray)
                            }
                            Text(
                                text = "${res.score}/${res.maxScore} (${res.percentage}%)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }
        }

        // TAB 3: Leaderboard
        if (selectedTabIndex == 3) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "لوحة الشرف للأوائل (ثالثة ثانوي) 🏆",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val leaders = listOf(
                            Triple("1", "زياد وليد (أنت)", "1850 XP 🥇"),
                            Triple("2", "مريم خالد إبراهيم", "1720 XP 🥈"),
                            Triple("3", "عمر طارق الشريف", "1650 XP 🥉"),
                            Triple("4", "سما ياسر عبد العزيز", "1540 XP"),
                            Triple("5", "كريم مصطفى", "1480 XP")
                        )

                        leaders.forEach { (rank, name, score) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .background(
                                        if (rank == "1") Color(0xFFFBBF24).copy(alpha = 0.15f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = rank, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF6C63FF))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = name, fontSize = 12.sp, fontWeight = if (rank == "1") FontWeight.Bold else FontWeight.Normal)
                                }
                                Text(text = score, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Triggered Dialogs
    activeVideoForPlayer?.let { video ->
        VideoPlayerDialog(
            lesson = video,
            onDismiss = { activeVideoForPlayer = null }
        )
    }

    activeExamForTaking?.let { exam ->
        ExamTakingDialog(
            exam = exam,
            onDismiss = { activeExamForTaking = null }
        )
    }

    activeBookForReading?.let { book ->
        BookReaderDialog(
            book = book,
            onDismiss = { activeBookForReading = null }
        )
    }
}

@Composable
private fun EmptyContentNotice(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📂", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = msg,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
