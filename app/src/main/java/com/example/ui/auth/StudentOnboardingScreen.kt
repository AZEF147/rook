package com.example.ui.auth

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.model.SchoolGrade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentOnboardingScreen(
    onCompleteOnboarding: () -> Unit
) {
    val context = LocalContext.current
    var studentName by remember { mutableStateOf("زياد وليد") }
    var selectedGrade by remember { mutableStateOf(SchoolGrade.SEC_3) }
    var teacherCodeInput by remember { mutableStateOf("PHYS-AHMED-2025") }
    var loginProvider by remember { mutableStateOf("Google") } // Google or Facebook

    val quickCodes = listOf(
        Pair("أ. أحمد سمير (فيزياء)", "PHYS-AHMED-2025"),
        Pair("د. محمد رضا (عربي)", "ARAB-REDA-2025"),
        Pair("أ. سارة النجار (رياضيات)", "MATH-SARA-2025"),
        Pair("أ. جورج مكرم (كيمياء)", "CHEM-GEORGE-2025")
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Welcome Header with Gradient Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF6C63FF), Color(0xFF4F46E5))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🚀", fontSize = 34.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "مرحباً بك في منصة EduMaster",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "المنصة التعليمية الذكية لجميع مراحل الإعدادي والثانوي",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Social Login Simulation Switch
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (loginProvider == "Google") Color.White else Color.Transparent,
                            shadowElevation = if (loginProvider == "Google") 2.dp else 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    loginProvider = "Google"
                                    Toast.makeText(context, "تم تسجيل الدخول بحساب Google", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(text = "🌐", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "حساب Google",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (loginProvider == "Google") Color(0xFF1F2937) else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (loginProvider == "Facebook") Color(0xFF1877F2).copy(alpha = 0.15f) else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    loginProvider = "Facebook"
                                    Toast.makeText(context, "تم تسجيل الدخول بحساب Facebook", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(text = "📘", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "حساب Facebook",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (loginProvider == "Facebook") Color(0xFF1877F2) else Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Step 1: Student Name
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("اسم الطالب ثلاثي") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Step 2: Choose School Grade (من أولى إعدادي حتى ثالثة ثانوي)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "1", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "اختر سنتك الدراسية:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Grid of 6 Grades
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val grades = SchoolGrade.values()
                    for (i in grades.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GradeCard(
                                grade = grades[i],
                                isSelected = selectedGrade == grades[i],
                                onClick = { selectedGrade = grades[i] },
                                modifier = Modifier.weight(1f)
                            )
                            if (i + 1 < grades.size) {
                                GradeCard(
                                    grade = grades[i + 1],
                                    isSelected = selectedGrade == grades[i + 1],
                                    onClick = { selectedGrade = grades[i + 1] },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Step 3: Teacher Code
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "2", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "أدخل كود المدرس الخاص بك:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = teacherCodeInput,
                    onValueChange = { teacherCodeInput = it.uppercase() },
                    label = { Text("كود المدرس (مثل PHYS-AHMED-2025)") },
                    leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                    trailingIcon = {
                        if (teacherCodeInput.isNotEmpty()) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "أو اختر كوداً سريعاً للتجربة:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Quick code suggestions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickCodes.forEach { (teacherLabel, code) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (teacherCodeInput == code) Color(0xFF10B981).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (teacherCodeInput == code) BorderStroke(1.dp, Color(0xFF10B981)) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { teacherCodeInput = code }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = teacherLabel, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text(text = code, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (studentName.isBlank()) {
                            Toast.makeText(context, "من فضلك اكتب اسمك", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (teacherCodeInput.isBlank()) {
                            Toast.makeText(context, "من فضلك أدخل كود المعلم", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        EduRepository.updateStudentProfile(
                            name = studentName,
                            grade = selectedGrade,
                            teacherCode = teacherCodeInput
                        )
                        Toast.makeText(context, "تم حفظ بياناتك بنجاح! جاري تحميل دروسك...", Toast.LENGTH_SHORT).show()
                        onCompleteOnboarding()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C63FF)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "دخول المنصة وعرض دروسي",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun GradeCard(
    grade: SchoolGrade,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) Color(0xFF6C63FF) else MaterialTheme.colorScheme.outlineVariant
    val bgColor = if (isSelected) Color(0xFF6C63FF).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (grade.stageAr == "المرحلة الثانوية") "ثانوي 🎓" else "إعدادي 🎒",
                    fontSize = 10.sp,
                    color = if (isSelected) Color(0xFF6C63FF) else Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = grade.titleAr,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color(0xFF6C63FF) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
