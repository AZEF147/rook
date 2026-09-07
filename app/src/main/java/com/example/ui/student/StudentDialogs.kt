package com.example.ui.student

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.EduRepository
import com.example.model.Book
import com.example.model.Exam
import com.example.model.LessonVideo
import com.example.ui.components.DynamicSecurityWatermark
import kotlinx.coroutines.delay

/**
 * Modern Interactive Video Player Dialog with Speed Control, Timestamped Notes,
 * and Active Dynamic Anti-Screen-Recording Watermark.
 */
@Composable
fun VideoPlayerDialog(
    lesson: LessonVideo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by EduRepository.currentUser.collectAsState()

    var isPlaying by remember { mutableStateOf(true) }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var progress by remember { mutableFloatStateOf(0.25f) }
    var userNoteText by remember { mutableStateOf("") }
    var notesList by remember { mutableStateOf(listOf("دقيقة 05:20: ركز الأستاذ على اتجاه التيار الاصطلاحي مقابل الفعلي")) }

    // Automatically record attendance when the student starts watching
    LaunchedEffect(lesson) {
        EduRepository.recordVideoWatched(lesson)
    }

    // Playback progress ticker simulation
    LaunchedEffect(isPlaying, currentSpeed) {
        while (isPlaying) {
            delay((1000 / currentSpeed).toLong())
            if (progress < 1.0f) {
                progress += 0.01f
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lesson.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "${lesson.teacherName} • ${lesson.grade.titleAr}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                // Video Screen Area with Dynamic Watermark
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(Color(0xFF0F172A)),
                    contentAlignment = Alignment.Center
                ) {
                    // Simulated Video Background Visual
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🎬", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.subject.titleAr,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "جودة 1080p HD • مصدر الفيديو: ${lesson.videoSourceType.titleAr}",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }

                    // Active Anti-Screen-Record Dynamic Watermark (Moves randomly)
                    DynamicSecurityWatermark(
                        studentName = currentUser.name,
                        studentPhone = currentUser.phone
                    )

                    // Bottom Player Controls Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            // Timeline slider
                            Slider(
                                value = progress,
                                onValueChange = { progress = it },
                                modifier = Modifier.fillMaxWidth().height(20.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF6C63FF),
                                    activeTrackColor = Color(0xFF6C63FF)
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { isPlaying = !isPlaying },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${(progress * lesson.durationMinutes).toInt()}:00 / ${lesson.durationMinutes}:00 دقيقة",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }

                                // Playback Speed Buttons
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (currentSpeed == speed) Color(0xFF6C63FF) else Color.White.copy(alpha = 0.2f),
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .clickable {
                                                    currentSpeed = speed
                                                    Toast.makeText(context, "السرعة: ${speed}x", Toast.LENGTH_SHORT).show()
                                                }
                                        ) {
                                            Text(
                                                text = "${speed}x",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Video Details & Notes Section
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(14.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "تم تسجيل حضورك في شيت المعلم تلقائياً بنسبة مشاهدة 100%",
                                    fontSize = 11.sp,
                                    color = Color(0xFF065F46),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "وصف الحصة والملاحظات:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lesson.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Timestamped notes creator (Feature #68)
                        Text(
                            text = "📝 كتابة ملاحظة بالوقت الزمني (Timestamped Note):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = userNoteText,
                                onValueChange = { userNoteText = it },
                                placeholder = { Text("اكتب ملاحظتك على هذه اللحظة...") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (userNoteText.isNotBlank()) {
                                        val curMin = (progress * lesson.durationMinutes).toInt()
                                        val note = "دقيقة ${curMin}:00 - $userNoteText"
                                        notesList = notesList + note
                                        userNoteText = ""
                                        Toast.makeText(context, "تم حفظ الملاحظة بالوقت!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("حفظ", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(notesList) { note ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = note,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Exam Taking Dialog with Live Countdown Timer, Kiosk anti-cheating,
 * and instant auto-grading updating teacher records.
 */
@Composable
fun ExamTakingDialog(
    exam: Exam,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var remainingSeconds by remember { mutableIntStateOf(exam.durationMinutes * 60) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionIndex -> selectedOptionIndex
    var isSubmitted by remember { mutableStateOf(false) }
    var finalScore by remember { mutableIntStateOf(0) }

    // Live countdown timer
    LaunchedEffect(isSubmitted) {
        while (!isSubmitted && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        if (remainingSeconds <= 0 && !isSubmitted) {
            // Auto submit when time ends
            isSubmitted = true
            var score = 0
            exam.questions.forEachIndexed { index, q ->
                if (userAnswers[index] == q.correctOptionIndex) {
                    score += 5
                }
            }
            finalScore = score
            EduRepository.submitExamResult(exam, score, exam.questions.size * 5)
        }
    }

    Dialog(
        onDismissRequest = {
            if (!isSubmitted) {
                Toast.makeText(context, "⚠️ تنبيه أمني: لا يمكنك الخروج حتى تسليم الامتحان منعاً لتسجيل صفر!", Toast.LENGTH_LONG).show()
            } else {
                onDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = isSubmitted,
            dismissOnClickOutside = isSubmitted
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header with Timer & Anti-Cheat Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = exam.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (exam.isSurprise) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEF4444).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "مفاجئ ⚡",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "الصف: ${exam.grade.titleAr} • المعلم: ${exam.teacherName}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Timer Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (remainingSeconds < 120) Color(0xFFEF4444) else Color(0xFF6C63FF)
                    ) {
                        val minutes = remainingSeconds / 60
                        val seconds = remainingSeconds % 60
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Anti-cheating banner
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🛡️ وضع كشك الاختبار نشط: الخروج من التطبيق يرصد محاولة غش فورية.", fontSize = 10.sp, color = Color(0xFF475569))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isSubmitted) {
                    // Active Question View
                    val question = exam.questions[currentQuestionIndex]

                    // Progress indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "السؤال ${currentQuestionIndex + 1} من ${exam.questions.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${(currentQuestionIndex + 1) * 5} درجات",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Question Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = question.questionText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Options List
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(question.options.indices.toList()) { optIdx ->
                            val optionText = question.options[optIdx]
                            val isSelected = userAnswers[currentQuestionIndex] == optIdx

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF6C63FF).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) Color(0xFF6C63FF) else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { userAnswers[currentQuestionIndex] = optIdx }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { userAnswers[currentQuestionIndex] = optIdx },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6C63FF))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optionText,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Navigation & Submit Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (currentQuestionIndex > 0) {
                            OutlinedButton(
                                onClick = { currentQuestionIndex-- },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("السابق", fontSize = 12.sp)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        if (currentQuestionIndex < exam.questions.size - 1) {
                            Button(
                                onClick = { currentQuestionIndex++ },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("التالي", fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    // Submit
                                    var score = 0
                                    exam.questions.forEachIndexed { index, q ->
                                        if (userAnswers[index] == q.correctOptionIndex) {
                                            score += 5
                                        }
                                    }
                                    finalScore = score
                                    isSubmitted = true
                                    EduRepository.submitExamResult(exam, score, exam.questions.size * 5)
                                    Toast.makeText(context, "تم تصدير نتيجتك لشيت المعلم بنجاح!", Toast.LENGTH_LONG).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                            ) {
                                Text("تسليم الإجابات والإنهاء", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    // Result Celebration View
                    val maxScore = exam.questions.size * 5
                    val percentage = ((finalScore.toDouble() / maxScore.toDouble()) * 100).toInt()

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = if (percentage >= 85) "🎉🏆" else "👏", fontSize = 50.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (percentage >= 85) "أحسنت! أداء متميز ومبهر" else "تم إتمام الاختبار بنجاح",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Score Badge
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.15f),
                                border = BorderStroke(1.5.dp, Color(0xFF10B981))
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "درجتك النهائية:", fontSize = 12.sp, color = Color(0xFF059669))
                                    Text(
                                        text = "$finalScore / $maxScore",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 28.sp,
                                        color = Color(0xFF047857)
                                    )
                                    Text(text = "النسبة المئوية: $percentage%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "تم تحديث شيت الإكسيل الخاص بالأستاذ ${exam.teacherName} فوراً بدرجتك وتوقيت الحل.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF334155),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "مراجعة الإجابات وشرح الأفكار النموذجية:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(exam.questions.indices.toList()) { qIdx ->
                            val q = exam.questions[qIdx]
                            val userOpt = userAnswers[qIdx]
                            val isCorrect = userOpt == q.correctOptionIndex

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCorrect) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFFEF4444).copy(alpha = 0.1f)
                                ),
                                border = BorderStroke(1.dp, if (isCorrect) Color(0xFF10B981).copy(alpha = 0.4f) else Color(0xFFEF4444).copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "السؤال ${qIdx + 1}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(
                                            text = if (isCorrect) "إجابة صحيحة (+5)" else "إجابة خاطئة (0)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCorrect) Color(0xFF059669) else Color(0xFFDC2626)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = q.questionText, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "الإجابة النموذجية: ${q.options[q.correctOptionIndex]}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "💡 التفسير العلمي: ${q.explanationAr}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("العودة إلى قائمة الدروس", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Interactive Book & Booklet Reader Dialog with Page Zoom, Sample Chapters, and Pricing
 */
@Composable
fun BookReaderDialog(
    book: Book,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var fontSizeSp by remember { mutableIntStateOf(14) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📖", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = book.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            Text(
                                text = "${book.teacherName} • ${book.pagesCount} صفحة • السعر: ${if (book.isFree) "مجاني" else "${book.priceEgp} ج.م"}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tools Bar: Font Size + Dark Mode + Download
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "حجم الخط:", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        FilledTonalButton(
                            onClick = { if (fontSizeSp > 11) fontSizeSp -= 2 },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("A-", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        FilledTonalButton(
                            onClick = { if (fontSizeSp < 22) fontSizeSp += 2 },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("A+", fontSize = 11.sp)
                        }
                    }

                    Row {
                        IconButton(
                            onClick = { Toast.makeText(context, "تم حفظ المذكرة للقراءة بدون إنترنت", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = { Toast.makeText(context, "تمت إضافة علامة مرجعية (Bookmark)", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Book Content Reader Preview
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    item {
                        Text(
                            text = "فهرس ومقدمة المذكرة الرسمية",
                            fontWeight = FontWeight.Bold,
                            fontSize = (fontSizeSp + 2).sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = book.description,
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp + 8).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ملخص المحتوى ومفاتيح الحل:",
                            fontWeight = FontWeight.Bold,
                            fontSize = (fontSizeSp + 1).sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = book.sampleSummary,
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp + 8).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "📌 بنك الأسئلة الملحق:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "يحتوي هذا الإصدار على 450 سؤال تدرج صعوبة من مستوى الفهم المباشر وحتى مستويات التفكير العليا، مع حلول تفصيلية لكافة مسائل كيرشوف والفيزياء الحديثة.",
                                    fontSize = 11.sp,
                                    lineHeight = 17.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Purchase / Download Button
                if (!book.isFree) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "تم شراء المذكرة بقيمة ${book.priceEgp} ج.م بنجاح من محفظتك!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "شراء النسخة الكاملة (${book.priceEgp} ج.م) والتحميل الفوري",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            Toast.makeText(context, "جاري تنزيل المذكرة المجانية على جهازك...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تحميل المذكرة المجانية (PDF)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
