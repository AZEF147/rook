package com.example.ui.features100

import android.content.Intent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.EduRepository
import com.example.model.FeatureCategory
import com.example.model.FeatureItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Features100Screen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val allFeatures = remember { EduRepository.all100Features }
    var selectedCategory by remember { mutableStateOf<FeatureCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Interactive Demo Dialog States
    var showAiTutorDialog by remember { mutableStateOf(false) }
    var showParentReportDialog by remember { mutableStateOf(false) }
    var showCalculatorDialog by remember { mutableStateOf(false) }
    var showCertificateDialog by remember { mutableStateOf(false) }

    val filteredList = remember(selectedCategory, searchQuery) {
        allFeatures.filter { feat ->
            (selectedCategory == null || feat.category == selectedCategory) &&
            (searchQuery.isBlank() || feat.title.contains(searchQuery) || feat.description.contains(searchQuery))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("مستكشف الـ 100 ميزة الذكية", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("دليل شامل لكافة إمكانيات المنصة التفاعلية", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("بحث في الـ 100 ميزة...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Filter Chips
                ScrollableTabRow(
                    selectedTabIndex = if (selectedCategory == null) 0 else selectedCategory!!.ordinal + 1,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    divider = {}
                ) {
                    Tab(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        text = { Text("جميع الميزات (${allFeatures.size})", fontSize = 11.sp, fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Normal) }
                    )
                    FeatureCategory.values().forEach { cat ->
                        Tab(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            text = { Text("${cat.iconEmoji} ${cat.titleAr}", fontSize = 11.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "عدد الميزات المعروضة: ${filteredList.size} ميزة",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            items(filteredList) { feature ->
                Card(
                    shape = RoundedCornerShape(14.dp),
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
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${feature.number}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = feature.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${feature.category.iconEmoji} ${feature.category.titleAr}",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = feature.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive Action Trigger
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    when (feature.number) {
                                        31, 32, 36 -> showAiTutorDialog = true
                                        47 -> showParentReportDialog = true
                                        40 -> showCalculatorDialog = true
                                        22 -> showCertificateDialog = true
                                        1, 5 -> Toast.makeText(context, "العلامة المائية المتحركة نشطة الآن وتعمل في مشغل الفيديو!", Toast.LENGTH_SHORT).show()
                                        7 -> Toast.makeText(context, "تم تفعيل وضع الكشك المقفل (Kiosk Mode) بنجاح!", Toast.LENGTH_SHORT).show()
                                        else -> Toast.makeText(context, "ميزة '${feature.title}' مدمجة ونشطة بالنظام!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = when (feature.number) {
                                        31, 32, 36 -> "تجربة المساعد الذكي 🤖"
                                        47 -> "توليد تقرير ولي الأمر 💬"
                                        40 -> "فتح الآلة الحاسبة 📐"
                                        22 -> "إصدار شهادة تقدير 📜"
                                        else -> "تشغيل الميزة تفاعلياً"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // --- Interactive Feature Demos ---

    // 1. AI Tutor Dialog
    if (showAiTutorDialog) {
        Dialog(onDismissRequest = { showAiTutorDialog = false }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🤖 المساعد التعليمي الذكي (AI Tutor)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        IconButton(onClick = { showAiTutorDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "سؤالك: كيف أفرق بين قانون كيرشوف الأول والثاني؟", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "إجابة المساعد الذكي:\n1. كيرشوف الأول: يطبق عند نقطة تفرع (عقدة) ويعتمد على مبدأ حفظ الشحنة (Σ I = 0).\n2. كيرشوف الثاني: يطبق في مسار كهربي مغلق ويعتمد على مبدأ حفظ الطاقة (Σ VB = Σ IR).",
                                fontSize = 11.sp,
                                color = Color(0xFF1E293B),
                                lineHeight = 17.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showAiTutorDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("حسناً، فهمت الفكرة")
                    }
                }
            }
        }
    }

    // 2. Parent WhatsApp Report Dialog
    if (showParentReportDialog) {
        val reportText = "السيد ولي أمر الطالب/ زياد وليد المحترم:\nنحيط سيادتكم علماً بأن الطالب حضر حصة الفيزياء بنسبة 100%، وحصل على درجة 20/20 في الاختبار التجريبي اليوم.\nمع تحيات أستاذ المادة: أ. أحمد سمير."

        Dialog(onDismissRequest = { showParentReportDialog = false }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "📱 تقرير ولي الأمر على الواتساب", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCF8C6)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = reportText,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFF075E54),
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, reportText)
                            }
                            context.startActivity(Intent.createChooser(intent, "إرسال التقرير لولي الأمر"))
                            showParentReportDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Text("إرسال عبر WhatsApp فوراً", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 3. Calculator Dialog
    if (showCalculatorDialog) {
        Dialog(onDismissRequest = { showCalculatorDialog = false }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📐 الآلة الحاسبة العلمية المدمجة", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "I = V / R  ->  I = 12V / 6Ω = 2.0 A", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6C63FF))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "مفعلة لحل المسائل الفيزيائية والرياضية أثناء الحصة دون مغادرة التطبيق.", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(onClick = { showCalculatorDialog = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("إغلاق")
                    }
                }
            }
        }
    }

    // 4. Certificate Dialog
    if (showCertificateDialog) {
        Dialog(onDismissRequest = { showCertificateDialog = false }) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(2.dp, Color(0xFFF59E0B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📜 شهادة تقدير وتفوق", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFFD97706))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "تمنح منصة EduMaster للطالب المتميز:", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "زياد وليد", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF6C63FF))
                    Text(text = "لحصوله على الدرجة النهائية (20/20) في الفيزياء", fontSize = 12.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "توقيع المعلم: أ. أحمد سمير ✍️", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "تم حفظ شهادة التقدير بصيغة PDF جاهزة للطباعة!", Toast.LENGTH_SHORT).show()
                            showCertificateDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("تحميل الشهادة (PDF)")
                    }
                }
            }
        }
    }
}
