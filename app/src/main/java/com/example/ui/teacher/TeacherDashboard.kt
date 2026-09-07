package com.example.ui.teacher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.example.ui.components.ExcelExportDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboard() {
    val context = LocalContext.current
    val activeTeacher by EduRepository.activeTeacher.collectAsState()
    val attendanceRecords by EduRepository.attendanceRecords.collectAsState()

    var showExcelDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("👥 سجل الطلاب والإكسيل", "🎬 نشر فيديو", "📚 نشر وتسعير كتاب", "⚡ إنشاء امتحان")

    // Grade filter in Student list
    var filterGrade by remember { mutableStateOf<SchoolGrade?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Forms State: Video Publish
    var videoTitle by remember { mutableStateOf("") }
    var videoSubject by remember { mutableStateOf(Subject.PHYSICS) }
    var videoGrade by remember { mutableStateOf(SchoolGrade.SEC_3) }
    var videoSourceType by remember { mutableStateOf(VideoSourceType.EXTERNAL_URL) }
    var videoUrlOrPath by remember { mutableStateOf("https://youtube.com/watch?v=sample_lesson") }
    var videoDuration by remember { mutableStateOf("45") }
    var videoDescription by remember { mutableStateOf("") }
    var isVideoFree by remember { mutableStateOf(false) }

    // Forms State: Book Publish & Pricing
    var bookTitle by remember { mutableStateOf("") }
    var bookSubject by remember { mutableStateOf(Subject.PHYSICS) }
    var bookGrade by remember { mutableStateOf(SchoolGrade.SEC_3) }
    var bookPriceEgp by remember { mutableStateOf("50") }
    var bookPages by remember { mutableStateOf("120") }
    var bookDescription by remember { mutableStateOf("") }
    var bookSampleSummary by remember { mutableStateOf("") }

    // Forms State: Exam & Task Creator
    var examTitle by remember { mutableStateOf("") }
    var examGrade by remember { mutableStateOf(SchoolGrade.SEC_3) }
    var examDuration by remember { mutableStateOf("15") }
    var isExamSurprise by remember { mutableStateOf(true) }
    var qText by remember { mutableStateOf("") }
    var opt1 by remember { mutableStateOf("") }
    var opt2 by remember { mutableStateOf("") }
    var opt3 by remember { mutableStateOf("") }
    var opt4 by remember { mutableStateOf("") }
    var correctOpt by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Teacher Profile Card with Code and Quick Copy
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF10B981), Color(0xFF047857))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👨‍🏫", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = activeTeacher.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${activeTeacher.title} • ${activeTeacher.subject.titleAr}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Copy Teacher Code Chip
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Teacher Code", activeTeacher.teacherCode)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ كودك الخاص: ${activeTeacher.teacherCode}", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = activeTeacher.teacherCode,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color(0xFF059669)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Stat Metric Counters
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "الطلاب المسجلين", fontSize = 10.sp, color = Color.Gray)
                            Text(text = "${activeTeacher.studentCount}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "سجلات الحضور", fontSize = 10.sp, color = Color.Gray)
                            Text(text = "${attendanceRecords.size}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF059669))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "التقييم", fontSize = 10.sp, color = Color.Gray)
                            Text(text = "⭐ ${activeTeacher.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF59E0B))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "أرباح الكتب", fontSize = 10.sp, color = Color.Gray)
                            Text(text = "28,500 ج.م", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF6C63FF))
                        }
                    }
                }
            }
        }

        // Prominent Auto-Updating Excel Sheet Generation Button (Requested by User)
        item {
            Button(
                onClick = { showExcelDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF107C41) // Microsoft Excel Green
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "📊", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إصدار وتحديث شيت إكسيل الطلاب والامتحانات تلقائياً",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Navigation Tabs
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

        // TAB 0: Student Records, Attendance & Excel Live Table
        if (selectedTabIndex == 0) {
            item {
                // Search & Filter
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("بحث باسم الطالب أو رقم الهاتف...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grade Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = filterGrade == null,
                            onClick = { filterGrade = null },
                            label = { Text("الكل", fontSize = 11.sp) }
                        )
                        SchoolGrade.values().forEach { g ->
                            FilterChip(
                                selected = filterGrade == g,
                                onClick = { filterGrade = if (filterGrade == g) null else g },
                                label = { Text(if (g.titleAr.contains("الثانوي")) g.titleAr.replace("الصف ", "") else g.titleAr.replace("الصف ", ""), fontSize = 10.sp) }
                            )
                        }
                    }
                }
            }

            val filteredList = attendanceRecords.filter { rec ->
                (filterGrade == null || rec.grade == filterGrade) &&
                (searchQuery.isBlank() || rec.studentName.contains(searchQuery) || rec.studentPhone.contains(searchQuery))
            }

            if (filteredList.isEmpty()) {
                item {
                    Text(
                        text = "لا توجد سجلات مطابقة للبحث",
                        color = Color.Gray,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 12.sp
                    )
                }
            } else {
                items(filteredList) { record ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = record.studentName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "📞 ${record.studentPhone} • ${record.grade.titleAr}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (record.status) {
                                        AttendanceStatus.ATTENDED -> Color(0xFF10B981).copy(alpha = 0.2f)
                                        AttendanceStatus.ABSENT -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                        AttendanceStatus.LATE -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                    }
                                ) {
                                    Text(
                                        text = record.status.titleAr,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (record.status) {
                                            AttendanceStatus.ATTENDED -> Color(0xFF059669)
                                            AttendanceStatus.ABSENT -> Color(0xFFDC2626)
                                            AttendanceStatus.LATE -> Color(0xFFD97706)
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "📺 المشاهدة: ${record.watchPercentage}%", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                Text(text = "📝 آخر امتحان: ${record.lastExamScore}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "تاريخ الحضور: ${record.date}", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // TAB 1: Publish Video Form
        if (selectedTabIndex == 1) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "نشر فيديو شرح جديد 🎬",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "يمكنك رفع الفيديو من الاستوديو أو وضع رابط مباشر، وتحديد السنة الدراسية بدقة.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = videoTitle,
                            onValueChange = { videoTitle = it },
                            label = { Text("عنوان الفيديو / الحصة") },
                            placeholder = { Text("مثلاً: محاضرة 4 - الحث الكهرومغناطيسي") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Target Grade Picker
                        Text(text = "السنة الدراسية المستهدفة:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            SchoolGrade.values().forEach { gr ->
                                FilterChip(
                                    selected = videoGrade == gr,
                                    onClick = { videoGrade = gr },
                                    label = { Text(if (gr.stageAr == "المرحلة الثانوية") "ثانوي" else "إعدادي", fontSize = 10.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Video Source Picker (URL vs Gallery/Studio)
                        Text(text = "مصدر الفيديو:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { videoSourceType = VideoSourceType.EXTERNAL_URL }
                            ) {
                                RadioButton(
                                    selected = videoSourceType == VideoSourceType.EXTERNAL_URL,
                                    onClick = { videoSourceType = VideoSourceType.EXTERNAL_URL }
                                )
                                Text(text = "رابط يوتيوب/فيميو", fontSize = 11.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        videoSourceType = VideoSourceType.GALLERY_UPLOAD
                                        videoUrlOrPath = "gallery/videos/my_lesson_hd.mp4"
                                        Toast.makeText(context, "تم تحديد ملف الفيديو من الاستوديو بنجاح!", Toast.LENGTH_SHORT).show()
                                    }
                            ) {
                                RadioButton(
                                    selected = videoSourceType == VideoSourceType.GALLERY_UPLOAD,
                                    onClick = {
                                        videoSourceType = VideoSourceType.GALLERY_UPLOAD
                                        videoUrlOrPath = "gallery/videos/my_lesson_hd.mp4"
                                        Toast.makeText(context, "تم تحديد ملف الفيديو من الاستوديو بنجاح!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                Text(text = "من الاستوديو مباشرة 📁", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = videoUrlOrPath,
                            onValueChange = { videoUrlOrPath = it },
                            label = { Text(if (videoSourceType == VideoSourceType.EXTERNAL_URL) "رابط الفيديو" else "مسار ملف الفيديو") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = videoDuration,
                                onValueChange = { videoDuration = it },
                                label = { Text("مدة الفيديو (بالدقائق)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f).padding(top = 10.dp)
                            ) {
                                Checkbox(
                                    checked = isVideoFree,
                                    onCheckedChange = { isVideoFree = it }
                                )
                                Text("معاينة مجانية", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = videoDescription,
                            onValueChange = { videoDescription = it },
                            label = { Text("وصف الحصة والنقاط المشروحة") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (videoTitle.isBlank()) {
                                    Toast.makeText(context, "من فضلك اكتب عنوان الفيديو", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                EduRepository.publishVideo(
                                    title = videoTitle,
                                    subject = videoSubject,
                                    grade = videoGrade,
                                    sourceType = videoSourceType,
                                    urlOrPath = videoUrlOrPath,
                                    durationMinutes = videoDuration.toIntOrNull() ?: 45,
                                    description = videoDescription.ifBlank { "شرح الدرس وحل التمارين لطلاب ${videoGrade.titleAr}" },
                                    isFree = isVideoFree
                                )
                                Toast.makeText(context, "تم نشر الفيديو بنجاح وإرسال إشعار فوري لطلاب ${videoGrade.titleAr}!", Toast.LENGTH_LONG).show()
                                videoTitle = ""
                                videoDescription = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                        ) {
                            Text("نشر الفيديو وتنبيه الطلاب فوراً 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // TAB 2: Publish & Price Books Form
        if (selectedTabIndex == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "نشر كتاب / مذكرة وتسعيرها 📚",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "حدد سعر مذكرتك بالجنيه المصري أو اجعلها مجانية، ليتمكن الطلاب من تحميلها.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = bookTitle,
                            onValueChange = { bookTitle = it },
                            label = { Text("عنوان الكتاب أو المذكرة") },
                            placeholder = { Text("مثلاً: بنك أسئلة الفيزياء الحديثة 2025") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = bookPriceEgp,
                                onValueChange = { bookPriceEgp = it },
                                label = { Text("السعر بالجنيه (EGP)") },
                                placeholder = { Text("0 للمجاني") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = bookPages,
                                onValueChange = { bookPages = it },
                                label = { Text("عدد الصفحات") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = bookDescription,
                            onValueChange = { bookDescription = it },
                            label = { Text("وصف محتويات المذكرة") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = bookSampleSummary,
                            onValueChange = { bookSampleSummary = it },
                            label = { Text("مقدمة أو فهرس للعرض كمعاينة للطالب") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (bookTitle.isBlank()) {
                                    Toast.makeText(context, "اكتب عنوان المذكرة أولاً", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val price = bookPriceEgp.toDoubleOrNull() ?: 0.0
                                EduRepository.publishBook(
                                    title = bookTitle,
                                    subject = bookSubject,
                                    grade = bookGrade,
                                    priceEgp = price,
                                    pages = bookPages.toIntOrNull() ?: 80,
                                    description = bookDescription.ifBlank { "مذكرة تفاعلية تحتوي على بنك أسئلة وتلخيص شامل" },
                                    sample = bookSampleSummary.ifBlank { "فهرس وأهم قوانين ومفاهيم المنهج" }
                                )
                                Toast.makeText(context, "تم نشر الكتاب بسعر $price ج.م في متجر الطلاب بنجاح!", Toast.LENGTH_LONG).show()
                                bookTitle = ""
                                bookDescription = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("نشر المذكرة في المتجر 💰", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // TAB 3: Create Exam / Surprise Task
        if (selectedTabIndex == 3) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "إنشاء امتحان أو كويز مفاجئ ⚡",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "أنشئ تاسك في أي وقت لطلابك، مع تصحيح فوري ورصد تلقائي في شيت الإكسيل.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = examTitle,
                            onValueChange = { examTitle = it },
                            label = { Text("عنوان الامتحان") },
                            placeholder = { Text("مثلاً: اختبار مفاجئ على الباب الثاني") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = examDuration,
                                onValueChange = { examDuration = it },
                                label = { Text("المدة (بالدقائق)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Checkbox(
                                    checked = isExamSurprise,
                                    onCheckedChange = { isExamSurprise = it }
                                )
                                Text("امتحان مفاجئ عاجل ⚡", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "إضافة سؤال اختيار من متعدد:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = qText,
                            onValueChange = { qText = it },
                            label = { Text("نص السؤال") },
                            placeholder = { Text("مثال: شدة التيار تقاس بوحدة...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = opt1,
                            onValueChange = { opt1 = it },
                            label = { Text("الخيار (1) - الإجابة الصحيحة افتراضياً") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = opt2,
                            onValueChange = { opt2 = it },
                            label = { Text("الخيار (2)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = opt3,
                            onValueChange = { opt3 = it },
                            label = { Text("الخيار (3)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = opt4,
                            onValueChange = { opt4 = it },
                            label = { Text("الخيار (4)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (examTitle.isBlank()) {
                                    Toast.makeText(context, "اكتب عنوان الامتحان أولاً", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val questionList = listOf(
                                    Question(
                                        id = "q_${System.currentTimeMillis()}",
                                        questionText = qText.ifBlank { "وحدة قياس شدة المجال المغناطيسي هي التسلا (T) وتكافئ:" },
                                        options = if (opt1.isNotBlank() && opt2.isNotBlank()) {
                                            listOf(opt1, opt2, opt3.ifBlank { "خيار إضافي" }, opt4.ifBlank { "خيار إضافي" })
                                        } else {
                                            listOf("ويبر / متر مربع (Wb/m²)", "فولت × ثانية", "أمبير / متر", "جول / كولوم")
                                        },
                                        correctOptionIndex = 0,
                                        explanationAr = "التسلا T = Wb / m² وتكافئ نيوتن / (أمبير × متر)."
                                    )
                                )
                                EduRepository.publishExam(
                                    title = examTitle,
                                    subject = Subject.PHYSICS,
                                    grade = examGrade,
                                    durationMinutes = examDuration.toIntOrNull() ?: 15,
                                    isSurprise = isExamSurprise,
                                    questions = questionList
                                )
                                Toast.makeText(context, "تم نشر الامتحان وإرسال تنبيه عاجل للطلاب!", Toast.LENGTH_LONG).show()
                                examTitle = ""
                                qText = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isExamSurprise) Color(0xFFEF4444) else Color(0xFF10B981)
                            )
                        ) {
                            Text("تفعيل الامتحان وبثه للطلاب الآن", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Excel Export Preview Dialog
    if (showExcelDialog) {
        ExcelExportDialog(onDismiss = { showExcelDialog = false })
    }
}
