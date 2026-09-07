package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
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
import com.example.model.UserRole
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EduTopAppBar(
    title: String,
    onRoleSwitchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onExplorerClick: () -> Unit
) {
    val currentUser by EduRepository.currentUser.collectAsState()
    val notifications by EduRepository.notifications.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Brand & Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF6C63FF), Color(0xFF3F3D56))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎓", fontSize = 20.sp)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "EduMaster Pro",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "ذكية",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action Buttons: 100 Features Badge, Role Switch Chip, Notifications
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // 100 Features Button
                    FilledTonalButton(
                        onClick = onExplorerClick,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFF59E0B).copy(alpha = 0.15f),
                            contentColor = Color(0xFFD97706)
                        )
                    ) {
                        Text(text = "⭐ الـ 100 ميزة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Role switch button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable { onRoleSwitchClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = when (currentUser.role) {
                                    UserRole.STUDENT -> Icons.Default.School
                                    UserRole.TEACHER -> Icons.Default.Person
                                    UserRole.CENTER_ADMIN -> Icons.Default.Apartment
                                },
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser.role.titleAr,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Notifications Bell
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.size(38.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (notifications.isNotEmpty()) {
                                    Badge(
                                        containerColor = Color(0xFFEF4444)
                                    ) {
                                        Text("${notifications.size}", fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "الإشعارات",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dynamic Anti-Screen-Record Watermark that continuously shifts positions
 * across the screen with the student's name and phone number (Requested Feature #1 & #5).
 */
@Composable
fun DynamicSecurityWatermark(
    studentName: String,
    studentPhone: String,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(20f) }
    var offsetY by remember { mutableFloatStateOf(40f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            offsetX = (20..220).random().toFloat()
            offsetY = (40..300).random().toFloat()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.22f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .offset(x = offsetX.dp, y = offsetY.dp)
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "🔒 $studentName ($studentPhone) - محمي بحقوق النشر",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Dialog to preview and export the auto-updating Excel/CSV sheet
 */
@Composable
fun ExcelExportDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val records by EduRepository.attendanceRecords.collectAsState()
    val csvContent = remember(records) { EduRepository.generateExcelSheetCsv() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF107C41)), // Excel Green
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📊", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "شيت إكسيل حضور وامتحانات الطلاب",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "يتم تحديثه تلقائياً لحظة بلحظة مع كل تفاعل",
                                fontSize = 11.sp,
                                color = Color(0xFF107C41)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "إجمالي السجلات", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "${records.size} طالب", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "نسبة الحضور", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "88.5%", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF059669))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "صيغة التصدير", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "Excel .CSV UTF-8", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF107C41))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Table preview
                Text(
                    text = "معاينة البيانات الحية (تحدث فوراً):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                ) {
                    items(records) { record ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = record.studentName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (record.status.titleAr == "حاضر") Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = record.status.titleAr,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (record.status.titleAr == "حاضر") Color(0xFF059669) else Color(0xFFDC2626),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "📞 ${record.studentPhone}", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = "🎓 ${record.grade.titleAr}", fontSize = 11.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "📺 مشاهدة: ${record.watchPercentage}%", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(text = "📝 آخر امتحان: ${record.lastExamScore}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Copy CSV / Share Excel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Excel Data", csvContent)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "تم نسخ شيت الإكسيل إلى الحافظة بنجاح!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "نسخ النص", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, csvContent)
                                putExtra(Intent.EXTRA_TITLE, "شيت_طلاب_EduMaster.csv")
                                type = "text/csv"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "مشاركة شيت الإكسيل")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF107C41))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "مشاركة / فتح بالإكسيل", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Role Switch Dialog allowing quick switching between Student, Teacher, and Center Admin modes.
 */
@Composable
fun RoleSwitchDialog(
    onDismiss: () -> Unit,
    onRoleSelected: (UserRole) -> Unit
) {
    val currentUser by EduRepository.currentUser.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "تبديل وضع التطبيق",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "اختر الدور لتجربة كامل إمكانيات المنصة فوراً:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Student Role Card
                RoleOptionCard(
                    title = "وضع الطالب (Student View)",
                    subtitle = "اختيار السنة، إدخال الكود، مشاهدة فيديوهات مدرستي، وحل الامتحانات.",
                    icon = Icons.Default.School,
                    isSelected = currentUser.role == UserRole.STUDENT,
                    badgeColor = Color(0xFF6366F1),
                    onClick = {
                        EduRepository.switchRole(UserRole.STUDENT)
                        onRoleSelected(UserRole.STUDENT)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Teacher Role Card
                RoleOptionCard(
                    title = "لوحة تحكم المعلم (Teacher Dashboard)",
                    subtitle = "نشر كتب وتسعيرها، رفع فيديوهات، كود المدرس، وإصدار شيتات إكسيل تلقائية.",
                    icon = Icons.Default.Person,
                    isSelected = currentUser.role == UserRole.TEACHER,
                    badgeColor = Color(0xFF10B981),
                    onClick = {
                        EduRepository.switchRole(UserRole.TEACHER)
                        onRoleSelected(UserRole.TEACHER)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Center Admin Card
                RoleOptionCard(
                    title = "إدارة السنتر التعليمي (Center Mode)",
                    subtitle = "إدارة القاعات والمواعيد، ربط المدرسين، والتحضير بالـ QR Code.",
                    icon = Icons.Default.Apartment,
                    isSelected = currentUser.role == UserRole.CENTER_ADMIN,
                    badgeColor = Color(0xFFF59E0B),
                    onClick = {
                        EduRepository.switchRole(UserRole.CENTER_ADMIN)
                        onRoleSelected(UserRole.CENTER_ADMIN)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("إغلاق", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) badgeColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, badgeColor) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = badgeColor, modifier = Modifier.size(20.dp))
            }
        }
    }
}

/**
 * Notifications Dialog
 */
@Composable
fun NotificationsDialog(
    onDismiss: () -> Unit
) {
    val notifications by EduRepository.notifications.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "مركز الإشعارات والتنبيهات 🔔",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (notifications.isEmpty()) {
                    Text(
                        text = "لا توجد إشعارات جديدة حالياً",
                        color = Color.Gray,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 13.sp
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(notifications) { notif ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = notif, fontSize = 12.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
