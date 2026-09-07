package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.EduRepository
import com.example.model.UserRole
import com.example.ui.auth.StudentOnboardingScreen
import com.example.ui.center.CenterDashboard
import com.example.ui.components.EduTopAppBar
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.RoleSwitchDialog
import com.example.ui.features100.Features100Screen
import com.example.ui.student.StudentDashboard
import com.example.ui.teacher.TeacherDashboard
import com.example.ui.theme.MyApplicationTheme

enum class AppDestination {
    ONBOARDING,
    MAIN_APP,
    FEATURES_100
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Support Arabic RTL layout natively for flawless Arabic UX
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    EduMasterApp()
                }
            }
        }
    }
}

@Composable
fun EduMasterApp() {
    val currentUser by EduRepository.currentUser.collectAsState()
    var currentDestination by remember { mutableStateOf(AppDestination.MAIN_APP) }
    var showRoleDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    if (currentDestination == AppDestination.ONBOARDING) {
        StudentOnboardingScreen(
            onCompleteOnboarding = {
                currentDestination = AppDestination.MAIN_APP
            }
        )
    } else if (currentDestination == AppDestination.FEATURES_100) {
        Features100Screen(
            onBack = { currentDestination = AppDestination.MAIN_APP }
        )
    } else {
        Scaffold(
            topBar = {
                EduTopAppBar(
                    title = when (currentUser.role) {
                        UserRole.STUDENT -> "بوابة الطالب • ${currentUser.grade?.titleAr ?: "اختر السنة"}"
                        UserRole.TEACHER -> "لوحة تحكم المعلم • كود: ${currentUser.enrolledTeacherCode ?: "PHYS-2025"}"
                        UserRole.CENTER_ADMIN -> "إدارة السناتر التعليمية والقاعات"
                    },
                    onRoleSwitchClick = { showRoleDialog = true },
                    onNotificationsClick = { showNotificationsDialog = true },
                    onExplorerClick = { currentDestination = AppDestination.FEATURES_100 }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentUser.role) {
                    UserRole.STUDENT -> {
                        StudentDashboard(
                            onChangeGradeOrTeacher = {
                                currentDestination = AppDestination.ONBOARDING
                            }
                        )
                    }
                    UserRole.TEACHER -> {
                        TeacherDashboard()
                    }
                    UserRole.CENTER_ADMIN -> {
                        CenterDashboard()
                    }
                }
            }
        }
    }

    // Role Switch Dialog
    if (showRoleDialog) {
        RoleSwitchDialog(
            onDismiss = { showRoleDialog = false },
            onRoleSelected = { newRole ->
                if (newRole == UserRole.STUDENT && currentUser.grade == null) {
                    currentDestination = AppDestination.ONBOARDING
                } else {
                    currentDestination = AppDestination.MAIN_APP
                }
            }
        )
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationsDialog(
            onDismiss = { showNotificationsDialog = false }
        )
    }
}
