package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object EduRepository {

    // --- Current Active User & Teacher ---
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "std_101",
            name = "زياد وليد",
            email = "zeyad@edumaster.eg",
            phone = "01098765432",
            role = UserRole.STUDENT,
            grade = SchoolGrade.SEC_3,
            enrolledTeacherCode = "PHYS-AHMED-2025",
            xpPoints = 1850,
            streakDays = 14,
            walletBalanceEgp = 320.0
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Current Teacher for Teacher Mode
    private val _activeTeacher = MutableStateFlow(
        TeacherProfile(
            id = "tch_1",
            name = "أ. أحمد سمير",
            subject = Subject.PHYSICS,
            teacherCode = "PHYS-AHMED-2025",
            title = "كبير معلمي الفيزياء للثانوية العامة",
            bio = "خبرة أكثر من 18 عاماً في تبسيط مفاهيم الفيزياء وحل أصعب مسائل الباب الأول حتى الثامن بالفيزياء الحديثة.",
            phone = "01001234567",
            supportedGrades = listOf(SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 428,
            rating = 4.95
        )
    )
    val activeTeacher: StateFlow<TeacherProfile> = _activeTeacher.asStateFlow()

    // Available Teachers List
    val teachersList = listOf(
        TeacherProfile(
            id = "tch_1",
            name = "أ. أحمد سمير",
            subject = Subject.PHYSICS,
            teacherCode = "PHYS-AHMED-2025",
            title = "أستاذ أول الفيزياء",
            bio = "مؤلف سلسلة 'القمة في الفيزياء' - خبرة واسعة في مناهج الثانوية العامة الحديثة",
            phone = "01001234567",
            supportedGrades = listOf(SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 428,
            rating = 4.95
        ),
        TeacherProfile(
            id = "tch_2",
            name = "د. محمد رضا",
            subject = Subject.ARABIC,
            teacherCode = "ARAB-REDA-2025",
            title = "دكتوراه في البلاغة والنحو العربي",
            bio = "شرح مبسط لقواعد النحو وفنون البلاغة والأدب لجميع صفوف الإعدادي والثانوي",
            phone = "01112345678",
            supportedGrades = listOf(SchoolGrade.PREP_1, SchoolGrade.PREP_2, SchoolGrade.PREP_3, SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 612,
            rating = 4.98
        ),
        TeacherProfile(
            id = "tch_3",
            name = "أ. سارة النجار",
            subject = Subject.MATH,
            teacherCode = "MATH-SARA-2025",
            title = "خبيرة الرياضيات البحتة والتطبيقية",
            bio = "تأسيس الرياضيات، التفاضل والتكامل، والجبر الفراغي بطرق تفكير إبداعية وسريعة",
            phone = "01223456789",
            supportedGrades = listOf(SchoolGrade.PREP_3, SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 389,
            rating = 4.92
        ),
        TeacherProfile(
            id = "tch_4",
            name = "أ. جورج مكرم",
            subject = Subject.CHEMISTRY,
            teacherCode = "CHEM-GEORGE-2025",
            title = "معلم الكيمياء العامة والتحليلية",
            bio = "أستاذ الكيمياء بالسناتر الكبرى - تبسيط الكيمياء العضوية ومعادلات الاتزان الكيميائي",
            phone = "01556789012",
            supportedGrades = listOf(SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 510,
            rating = 4.94
        ),
        TeacherProfile(
            id = "tch_5",
            name = "مستر سامح فاروق",
            subject = Subject.ENGLISH,
            teacherCode = "ENG-SAMEH-2025",
            title = "خبير اللغة الإنجليزية ومهارات الترجمة",
            bio = "تدريب على قطع الفهم والقصة والمقال وكبسولات القواعد النحوية الحديثة",
            phone = "01099887766",
            supportedGrades = listOf(SchoolGrade.PREP_1, SchoolGrade.PREP_2, SchoolGrade.PREP_3, SchoolGrade.SEC_1, SchoolGrade.SEC_2, SchoolGrade.SEC_3),
            studentCount = 475,
            rating = 4.91
        )
    )

    // --- Content: Videos ---
    private val _videos = MutableStateFlow<List<LessonVideo>>(
        listOf(
            LessonVideo(
                id = "vid_1",
                title = "محاضرة (1): قانون أوم للدائرة المغلقة وتوزيع التيارات",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                videoSourceType = VideoSourceType.EXTERNAL_URL,
                videoUrlOrPath = "https://youtube.com/watch?v=sample_ohm_law",
                durationMinutes = 45,
                viewsCount = 1240,
                description = "شرح مفصل لطريقة كيرشوف الأولى والثانية وتطبيقات قانون أوم وحل المسائل الصعبة مع ملاحظات هامة للثانوية العامة.",
                isFreePreview = true,
                uploadDate = "2025-03-01"
            ),
            LessonVideo(
                id = "vid_2",
                title = "محاضرة (2): أجهزة القياس الكهربي - الجلفانومتر والأميتر والفولتميتر",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                videoSourceType = VideoSourceType.GALLERY_UPLOAD,
                videoUrlOrPath = "local_storage/recordings/galvanometer_physics.mp4",
                durationMinutes = 58,
                viewsCount = 980,
                description = "استنتاج قوانين مجزئ التيار ومضاعف الجهد وحل 25 فكرة من امتحانات الأعوام السابقة.",
                isFreePreview = false,
                uploadDate = "2025-03-03"
            ),
            LessonVideo(
                id = "vid_3",
                title = "محاضرة (3): الحث الكهرومغناطيسي وقاعدة لنز والدينامو",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                videoSourceType = VideoSourceType.EXTERNAL_URL,
                videoUrlOrPath = "https://youtube.com/watch?v=sample_induction",
                durationMinutes = 62,
                viewsCount = 850,
                description = "تحديد اتجاه التيار المستحث وقوانين فاراداي ورسم المنحنى الجيبي لخرج الدينامو.",
                isFreePreview = false,
                uploadDate = "2025-03-05"
            ),
            LessonVideo(
                id = "vid_4",
                title = "فيزياء أولى ثانوي: معادلات الحركة بعجلة منتظمة وحساب الإزاحة",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_1,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                videoSourceType = VideoSourceType.EXTERNAL_URL,
                videoUrlOrPath = "https://youtube.com/watch?v=motion_sec1",
                durationMinutes = 35,
                viewsCount = 670,
                description = "تطبيقات حركة المقذوفات في بعدين ورسم العلاقات البيانية بين الإزاحة والزمن.",
                isFreePreview = true,
                uploadDate = "2025-02-27"
            ),
            LessonVideo(
                id = "vid_5",
                title = "لغة عربية: مفاتيح إعراب الثوابت النحوية والمشتقات العاملة",
                subject = Subject.ARABIC,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_2",
                teacherName = "د. محمد رضا",
                teacherCode = "ARAB-REDA-2025",
                videoSourceType = VideoSourceType.EXTERNAL_URL,
                videoUrlOrPath = "https://youtube.com/watch?v=arabic_sec3",
                durationMinutes = 50,
                viewsCount = 2100,
                description = "إعمال اسم الفاعل واسم المفعول وصيغ المبالغة وحل تطبيقات الامتحان الشامل.",
                isFreePreview = true,
                uploadDate = "2025-03-02"
            ),
            LessonVideo(
                id = "vid_6",
                title = "لغة عربية ثالثة إعدادي: المنادى وأنواعه وأحكامه الإعرابية",
                subject = Subject.ARABIC,
                grade = SchoolGrade.PREP_3,
                teacherId = "tch_2",
                teacherName = "د. محمد رضا",
                teacherCode = "ARAB-REDA-2025",
                videoSourceType = VideoSourceType.EXTERNAL_URL,
                videoUrlOrPath = "https://youtube.com/watch?v=prep3_arabic",
                durationMinutes = 30,
                viewsCount = 1450,
                description = "المنادى المضاف والشبيه بالمضاف والنكرة المقصورة وغير المقصورة وحل تدريبات كتاب المدرسة.",
                isFreePreview = true,
                uploadDate = "2025-02-25"
            )
        )
    )
    val videos: StateFlow<List<LessonVideo>> = _videos.asStateFlow()

    // --- Content: Books & Booklets with Pricing ---
    private val _books = MutableStateFlow<List<Book>>(
        listOf(
            Book(
                id = "bk_1",
                title = "كتاب القمة في الفيزياء - الجزء الأول (الكهربية والتيار المستمر)",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                priceEgp = 65.0,
                pagesCount = 184,
                description = "المذكرة الشاملة التي تحتوي على ملخص القوانين، بنك أسئلة النظام الحديث (أكثر من 500 سؤال تدرج صعوبة)، وتطبيقات عملية مع إجابات نموذجية.",
                sampleSummary = "يتضمن الباب الأول: التيار الكهربي وفرق الجهد، قانون أوم، المقاومة النوعية والتوصيلية، توصيل المقاومات توالي وتوازي، قانون أوم للدائرة المغلقة، وقانونا كيرشوف.",
                downloadsCount = 312,
                isFree = false
            ),
            Book(
                id = "bk_2",
                title = "كتيب المفاهيم والخرائط الذهنية للفيزياء الحديثة",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                priceEgp = 35.0,
                pagesCount = 76,
                description = "خرائط ذهنية وتلخيص دقيق لظاهرة كومتون، إشعاع الجسم الأسود، التأثير الكهروضوئي، الليزر، والإلكترونيات الحديثة.",
                sampleSummary = "الفيزياء الكلاسيكية وعجزها أمام إشعاع الجسم الأسود، فرضيات بلانك، انبعاث الإلكترونات الضوئية، وأشعة الليزر أحادية الطول الموجي.",
                downloadsCount = 490,
                isFree = false
            ),
            Book(
                id = "bk_3",
                title = "مذكرة التأسيس الفيزيائي لطلاب الصف الأول الثانوي",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_1,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                priceEgp = 0.0,
                pagesCount = 52,
                description = "مذكرة مجانية هدية لكل طلاب أولى ثانوي تحتوي على مهارات التحويلات، صيغة الأبعاد، ورسم المتجهات وقوانين نيوتن.",
                sampleSummary = "صيغة الأبعاد للكميات الفيزيائية، بادئات القياس الدولية، الخطأ المطلق والنسبي في القياس المباشر وغير المباشر.",
                downloadsCount = 890,
                isFree = true
            ),
            Book(
                id = "bk_4",
                title = "موسوعة النحو والبلاغة - المراجعة الشاملة للثانوية العامة",
                subject = Subject.ARABIC,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_2",
                teacherName = "د. محمد رضا",
                teacherCode = "ARAB-REDA-2025",
                priceEgp = 80.0,
                pagesCount = 220,
                description = "شرح كافي ووافي لجميع وحدات النحو السبعة مع حل أكثر من 1000 سؤال وفق أحدث مواصفات امتحانات الثانوية العامة.",
                sampleSummary = "الوحدة الأولى: همزة الوصل والقطع، الوحدة الثانية: المشتقات والمصادر، الوحدة الثالثة: النواسخ، الوحدة الرابعة: المنصوبات.",
                downloadsCount = 560,
                isFree = false
            )
        )
    )
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    // --- Exams & Surprise Tasks ---
    private val _exams = MutableStateFlow<List<Exam>>(
        listOf(
            Exam(
                id = "ex_1",
                title = "امتحان تجريبي شامل: الفصل الأول (الكهربية وقوانين كيرشوف)",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                durationMinutes = 20,
                isSurprise = false,
                maxScore = 20,
                questions = listOf(
                    Question(
                        id = "q1",
                        questionText = "في دائرة كهربية تحتوي على مقاومة 6 أوم موصلة بمصدر كهربي مهمل المقاومة الداخلية، إذا تضاعف طول السلك وقلت مساحة مقطعه إلى النصف، فإن شدة التيار المارة تصبح:",
                        options = listOf(
                            "ربع قيمتها الأصلية",
                            "نصف قيمتها الأصلية",
                            "أربعة أضعاف قيمتها الأصلية",
                            "تظل ثابتة لا تتغير"
                        ),
                        correctOptionIndex = 0,
                        explanationAr = "المقاومة تتناسب طردياً مع الطول وعكسياً مع المساحة: R جديدة = R × (2 ÷ 0.5) = 4R. وبما أن الجهد ثابت، فإن شدة التيار I تصبح الربع (I/4)."
                    ),
                    Question(
                        id = "q2",
                        questionText = "قانون كيرشوف الأول (قانون حفظ الشحنة) ينص رياضياً على أن:",
                        options = listOf(
                            "المجموع الجبري للقوى الدافعة الكهربية يساوي صفر",
                            "المجموع الجبري لشدة التيارات عند أي نقطة تفرع يساوي صفر (Σ I = 0)",
                            "الجهد يتناسب طردياً مع شدة التيار عند ثبوت درجة الحرارة",
                            "القدرة الكهربية المستهلكة تساوي مربع التيار في المقاومة"
                        ),
                        correctOptionIndex = 1,
                        explanationAr = "ينص قانون كيرشوف الأول على أن مجموع التيارات الداخلة إلى عقدة يساوي مجموع التيارات الخارجة منها: Σ I_in = Σ I_out أي Σ I = 0."
                    ),
                    Question(
                        id = "q3",
                        questionText = "بطارية قوتها الدافعة الكهربية 12V ومقاومتها الداخلية 1Ω موصلة بمقاومة خارجية 5Ω. يكون فرق الجهد بين طرفي البطارية مساوياً:",
                        options = listOf(
                            "12 فولت",
                            "10 فولت",
                            "2 فولت",
                            "6 فولت"
                        ),
                        correctOptionIndex = 1,
                        explanationAr = "I = VB / (R + r) = 12 / (5 + 1) = 2A. فرق الجهد بين قطبي البطارية V = VB - I*r = 12 - (2 × 1) = 10V."
                    ),
                    Question(
                        id = "q4",
                        questionText = "أي من الأجهزة التالية يقيس فرق الجهد الكهربي ويوصل دائماً على التوازي في الدوائر الكهربية؟",
                        options = listOf(
                            "الأميتر",
                            "الأوميتر",
                            "الفولتميتر",
                            "الميكروأميتر"
                        ),
                        correctOptionIndex = 2,
                        explanationAr = "الفولتميتر ذو مقاومة عالية جداً ويوصل على التوازي بين النقطتين المراد قياس فرق الجهد بينهما."
                    )
                )
            ),
            Exam(
                id = "ex_2",
                title = "⚡ كويز مفاجئ سريع: أجهزة القياس والجلفانومتر",
                subject = Subject.PHYSICS,
                grade = SchoolGrade.SEC_3,
                teacherId = "tch_1",
                teacherName = "أ. أحمد سمير",
                teacherCode = "PHYS-AHMED-2025",
                durationMinutes = 10,
                isSurprise = true,
                maxScore = 10,
                questions = listOf(
                    Question(
                        id = "q_s1",
                        questionText = "لتحويل الجلفانومتر ذي الملف المتحرك إلى أميتر يقيس تيارات أكبر، يتم توصيل ملفه بـ:",
                        options = listOf(
                            "مقاومة صغيرة على التوازي تسمى مجزئ التيار (Rs)",
                            "مقاومة كبيرة على التوالي تسمى مضاعف الجهد (Rm)",
                            "مقاومة متغيرة على التوالي",
                            "مكثف هوائي على التوازي"
                        ),
                        correctOptionIndex = 0,
                        explanationAr = "مجزئ التيار Rs هي مقاومة صغيرة جداً توصل على التوازي لتقليل المقاومة الكلية للجهاز وتمرير الجزء الأكبر من التيار."
                    ),
                    Question(
                        id = "q_s2",
                        questionText = "شرط استقرار مؤشر الجلفانومتر عند قراءة معينة هو:",
                        options = listOf(
                            "عزم الازدواج المغناطيسي يساوي صفر",
                            "تساوي عزم الازدواج الكهرومغناطيسي مع عزم اللي الناتج من زنبرك الجهاز",
                            "انعدام التيار تماماً في ملف الجلفانومتر",
                            "وصول حساسية الجهاز إلى القيمة العظمى"
                        ),
                        correctOptionIndex = 1,
                        explanationAr = "يتوقف المؤشر عندما يتساوى عزم الازدواج المغناطيسي المؤثر على الملف مع عزم اللي في الملفين اللولبيين."
                    )
                )
            )
        )
    )
    val exams: StateFlow<List<Exam>> = _exams.asStateFlow()

    // --- Attendance & Student Records ---
    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(
        listOf(
            AttendanceRecord(
                id = "rec_1",
                studentId = "std_101",
                studentName = "زياد وليد",
                studentPhone = "01098765432",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (1): قانون أوم وكيرشوف",
                date = "2025-03-01",
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 100,
                lastExamScore = "20/20",
                notes = "حضر في الموعد وأتم مشاهدة الفيديو كاملًا وحل الاختبار بنجاح"
            ),
            AttendanceRecord(
                id = "rec_2",
                studentId = "std_102",
                studentName = "مريم خالد إبراهيم",
                studentPhone = "01122334455",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (1): قانون أوم وكيرشوف",
                date = "2025-03-01",
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 92,
                lastExamScore = "18/20",
                notes = "ممتازة، تفاعلت مع الأسئلة بالمنتدى"
            ),
            AttendanceRecord(
                id = "rec_3",
                studentId = "std_103",
                studentName = "عمر طارق الشريف",
                studentPhone = "01233445566",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (1): قانون أوم وكيرشوف",
                date = "2025-03-01",
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 85,
                lastExamScore = "17/20",
                notes = "تأخر في تسليم الكويز ساعتين"
            ),
            AttendanceRecord(
                id = "rec_4",
                studentId = "std_104",
                studentName = "سما ياسر عبد العزيز",
                studentPhone = "01544556677",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (2): أجهزة القياس",
                date = "2025-03-03",
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 100,
                lastExamScore = "19/20",
                notes = "حاضرة ومنضبطة"
            ),
            AttendanceRecord(
                id = "rec_5",
                studentId = "std_105",
                studentName = "يوسف أحمد عبد القادر",
                studentPhone = "01011223344",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (2): أجهزة القياس",
                date = "2025-03-03",
                status = AttendanceStatus.ABSENT,
                watchPercentage = 0,
                lastExamScore = "لم يؤدِ الامتحان",
                notes = "تم إرسال تنبيه آلي لولي الأمر عبر واتساب"
            ),
            AttendanceRecord(
                id = "rec_6",
                studentId = "std_106",
                studentName = "نور الدين هشام",
                studentPhone = "01199887711",
                grade = SchoolGrade.SEC_3,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "محاضرة (2): أجهزة القياس",
                date = "2025-03-03",
                status = AttendanceStatus.LATE,
                watchPercentage = 45,
                lastExamScore = "12/20",
                notes = "شاهد أقل من نصف المحاضرة"
            ),
            AttendanceRecord(
                id = "rec_7",
                studentId = "std_107",
                studentName = "كريم مصطفى محمود",
                studentPhone = "01288776655",
                grade = SchoolGrade.SEC_1,
                teacherCode = "PHYS-AHMED-2025",
                lessonTitle = "فيزياء أولى ثانوي: معادلات الحركة",
                date = "2025-02-28",
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 100,
                lastExamScore = "19/20",
                notes = "أولى ثانوي مجتهد"
            )
        )
    )
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = _attendanceRecords.asStateFlow()

    // Student completed exam results
    private val _studentExamResults = MutableStateFlow<List<StudentExamResult>>(
        listOf(
            StudentExamResult(
                id = "res_1",
                examId = "ex_1",
                examTitle = "امتحان تجريبي شامل: الفصل الأول",
                studentId = "std_101",
                studentName = "زياد وليد",
                grade = SchoolGrade.SEC_3,
                score = 20,
                maxScore = 20,
                percentage = 100
            )
        )
    )
    val studentExamResults: StateFlow<List<StudentExamResult>> = _studentExamResults.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<String>>(
        listOf(
            "⚡ تنبيه مهم: تم نشر امتحان مفاجئ للفيزياء لطلاب الصف الثالث الثانوي الآن!",
            "🎬 تم إضافة فيديو جديد: محاضرة (3) الحث الكهرومغناطيسي وقاعدة لنز",
            "📚 تم نشر كتاب القمة في الفيزياء 2025 يمكنك الحصول عليه الآن من المتجر",
            "🏆 مبروك! حصلت على شارة 'طالب الشهر المتميز' لحصولك على الدرجة النهائية"
        )
    )
    val notifications: StateFlow<List<String>> = _notifications.asStateFlow()

    // Center Halls data
    val centerHalls = listOf(
        CenterHall(
            id = "hall_1",
            centerName = "سنتر النخبة التعليمي - مدينة نصر",
            hallName = "قاعة أينشتاين (القاعة الكبرى)",
            teacherName = "أ. أحمد سمير",
            subject = Subject.PHYSICS,
            grade = SchoolGrade.SEC_3,
            dayTime = "السبت والثلاثاء: 4:00 م - 6:30 م",
            capacity = 120,
            enrolledCount = 112,
            location = "شارع مصطفى النحاس - الدور الثاني"
        ),
        CenterHall(
            id = "hall_2",
            centerName = "سنتر النخبة التعليمي - مدينة نصر",
            hallName = "قاعة الخوارزمي (القاعة 2)",
            teacherName = "د. محمد رضا",
            subject = Subject.ARABIC,
            grade = SchoolGrade.SEC_3,
            dayTime = "الأحد والأربعاء: 5:00 م - 7:30 م",
            capacity = 90,
            enrolledCount = 88,
            location = "شارع مصطفى النحاس - الدور الثالث"
        ),
        CenterHall(
            id = "hall_3",
            centerName = "أكاديمية المستقبل - المهندسين",
            hallName = "قاعة نيوتن الذكية",
            teacherName = "أ. سارة النجار",
            subject = Subject.MATH,
            grade = SchoolGrade.SEC_2,
            dayTime = "الإثنين والخميس: 3:00 م - 5:00 م",
            capacity = 70,
            enrolledCount = 65,
            location = "ميدان لبنان - برج الأطباء"
        )
    )

    // --- Actions ---

    fun switchRole(newRole: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = newRole)
    }

    fun updateStudentProfile(name: String, grade: SchoolGrade, teacherCode: String) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            grade = grade,
            enrolledTeacherCode = teacherCode
        )
        // Add welcome notification
        addNotification("🎉 تم تسجيلك بنجاح في ${grade.titleAr} مع المعلم بكود ($teacherCode)")
    }

    fun addNotification(message: String) {
        _notifications.value = listOf(message) + _notifications.value
    }

    fun publishVideo(
        title: String,
        subject: Subject,
        grade: SchoolGrade,
        sourceType: VideoSourceType,
        urlOrPath: String,
        durationMinutes: Int,
        description: String,
        isFree: Boolean
    ) {
        val teacher = _activeTeacher.value
        val newVideo = LessonVideo(
            id = "vid_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            grade = grade,
            teacherId = teacher.id,
            teacherName = teacher.name,
            teacherCode = teacher.teacherCode,
            videoSourceType = sourceType,
            videoUrlOrPath = urlOrPath,
            durationMinutes = durationMinutes,
            viewsCount = 1,
            description = description,
            isFreePreview = isFree,
            uploadDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )
        _videos.value = listOf(newVideo) + _videos.value
        addNotification("🎬 أستاذك ${teacher.name} نشر فيديو شرح جديد: '$title' لـ ${grade.titleAr}")
    }

    fun publishBook(
        title: String,
        subject: Subject,
        grade: SchoolGrade,
        priceEgp: Double,
        pages: Int,
        description: String,
        sample: String
    ) {
        val teacher = _activeTeacher.value
        val newBook = Book(
            id = "bk_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            grade = grade,
            teacherId = teacher.id,
            teacherName = teacher.name,
            teacherCode = teacher.teacherCode,
            priceEgp = priceEgp,
            pagesCount = pages,
            description = description,
            sampleSummary = sample,
            downloadsCount = 0,
            isFree = (priceEgp <= 0.0)
        )
        _books.value = listOf(newBook) + _books.value
        addNotification("📚 أصدر ${teacher.name} كتاباً جديداً: '$title' بسعر $priceEgp ج.م")
    }

    fun publishExam(
        title: String,
        subject: Subject,
        grade: SchoolGrade,
        durationMinutes: Int,
        isSurprise: Boolean,
        questions: List<Question>
    ) {
        val teacher = _activeTeacher.value
        val newExam = Exam(
            id = "ex_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            grade = grade,
            teacherId = teacher.id,
            teacherName = teacher.name,
            teacherCode = teacher.teacherCode,
            durationMinutes = durationMinutes,
            questions = questions,
            isSurprise = isSurprise,
            maxScore = questions.size * 5
        )
        _exams.value = listOf(newExam) + _exams.value
        val alert = if (isSurprise) "⚡ امتحان مفاجئ عاجل!" else "📝 امتحان جديد متاح الآن"
        addNotification("$alert: قام ${teacher.name} بإضافة '$title' لـ ${grade.titleAr}")
    }

    fun recordVideoWatched(lesson: LessonVideo) {
        val user = _currentUser.value
        val existing = _attendanceRecords.value.find { it.studentId == user.id && it.lessonTitle == lesson.title }
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        
        if (existing == null) {
            val newRecord = AttendanceRecord(
                id = "att_${System.currentTimeMillis()}",
                studentId = user.id,
                studentName = user.name,
                studentPhone = user.phone,
                grade = user.grade ?: SchoolGrade.SEC_3,
                teacherCode = lesson.teacherCode,
                lessonTitle = lesson.title,
                date = dateStr,
                status = AttendanceStatus.ATTENDED,
                watchPercentage = 100,
                lastExamScore = "تم الحضور"
            )
            _attendanceRecords.value = listOf(newRecord) + _attendanceRecords.value
        }
        
        // Increase student XP & streak
        _currentUser.value = user.copy(
            xpPoints = user.xpPoints + 50
        )
    }

    fun submitExamResult(exam: Exam, score: Int, maxScore: Int) {
        val user = _currentUser.value
        val percentage = ((score.toDouble() / maxScore.toDouble()) * 100).toInt()
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val result = StudentExamResult(
            id = "res_${System.currentTimeMillis()}",
            examId = exam.id,
            examTitle = exam.title,
            studentId = user.id,
            studentName = user.name,
            grade = exam.grade,
            score = score,
            maxScore = maxScore,
            percentage = percentage,
            completedAt = dateStr
        )
        _studentExamResults.value = listOf(result) + _studentExamResults.value

        // Also update attendance record score for this student
        val updatedRecords = _attendanceRecords.value.map { rec ->
            if (rec.studentId == user.id) {
                rec.copy(lastExamScore = "$score/$maxScore ($percentage%)")
            } else rec
        }
        _attendanceRecords.value = updatedRecords

        // Reward student
        _currentUser.value = user.copy(
            xpPoints = user.xpPoints + (score * 10)
        )
    }

    /**
     * Generates a fully formatted CSV Excel export string with UTF-8 BOM
     * so that Microsoft Excel and Google Sheets open it natively in Arabic without corrupted characters.
     */
    fun generateExcelSheetCsv(filterTeacherCode: String? = null): String {
        val records = if (filterTeacherCode != null) {
            _attendanceRecords.value.filter { it.teacherCode == filterTeacherCode }
        } else {
            _attendanceRecords.value
        }

        val sb = StringBuilder()
        // UTF-8 BOM for Excel Arabic compatibility
        sb.append('\uFEFF')
        // Headers
        sb.append("م,اسم الطالب,رقم الهاتف,السنة الدراسية,كود المعلم,اسم الحصة / المحاضرة,تاريخ الحضور,حالة الحضور,نسبة المشاهدة,درجة الامتحان,ملاحظات المعلم\n")

        records.forEachIndexed { index, rec ->
            val num = index + 1
            val name = rec.studentName.replace(",", " ")
            val phone = rec.studentPhone
            val grade = rec.grade.titleAr.replace(",", " ")
            val code = rec.teacherCode
            val lesson = rec.lessonTitle.replace(",", " ")
            val date = rec.date
            val status = rec.status.titleAr
            val watch = "${rec.watchPercentage}%"
            val score = rec.lastExamScore
            val notes = rec.notes.replace(",", " ")

            sb.append("$num,$name,$phone,$grade,$code,$lesson,$date,$status,$watch,$score,$notes\n")
        }

        return sb.toString()
    }

    // --- The Complete 100 Features List ---
    val all100Features: List<FeatureItem> = listOf(
        // الأمان والحماية (1-15)
        FeatureItem(1, "منع تصوير الشاشة (Anti-Screen Record)", "تفعيل حماية FLAG_SECURE التلقائية وحجب تطبيقات الريكورد وتعتيم المحتوى.", FeatureCategory.SECURITY),
        FeatureItem(2, "منع تشغيل التطبيق على المحاكيات (Anti-Emulator)", "كشف بيئات الـ Emulators وروت الأجهزة لحماية المحتوى التعليمي.", FeatureCategory.SECURITY),
        FeatureItem(3, "ربط الحساب بجهاز واحد فقط (Device Fingerprint)", "تسجيل بصمة الجهاز UUID ومنع فتح الحساب من هاتفين في نفس الوقت.", FeatureCategory.SECURITY),
        FeatureItem(4, "تشفير الفيديوهات ومنع التحميل الخارجي", "تشفير AES-256 لروابط وبث الفيديوهات لمنع الاستخراج عبر برامج التحميل.", FeatureCategory.SECURITY),
        FeatureItem(5, "علامة مائية متحركة (Dynamic Watermark)", "ظهور اسم الطالب ورقم هاتفه عشوائياً وبحركة مستمرة فوق شاشة الفيديو.", FeatureCategory.SECURITY),
        FeatureItem(6, "نظام اكتشاف الغش بالذكاء الاصطناعي", "مراقبة تركيز الطالب أثناء الامتحان وتنبيه المعلم عند الخروج من الإطار.", FeatureCategory.SECURITY),
        FeatureItem(7, "وضع كشك الامتحان المقفل (Kiosk Mode)", "قفل أزرار الرجوع وشريط الإشعارات أثناء حل الاختبار لمنع فتح المتصفح.", FeatureCategory.SECURITY),
        FeatureItem(8, "تنبيه المعلم عند الدخول المشبوه", "إشعار فوري في لوحة المعلم عند محاولة طالب تسجيل الدخول من شبكة مختلفة.", FeatureCategory.SECURITY),
        FeatureItem(9, "تتبع الحضور الجغرافي بالسنتر (Geofencing)", "تسجيل حضور الطالب تلقائياً عبر GPS بمجرد وصوله لقاعة السنتر.", FeatureCategory.SECURITY),
        FeatureItem(10, "النسخ الاحتياطي السحابي اليومي", "مزامنة لحظية لكافة درجات الامتحانات والبيانات على خوادم آمنة.", FeatureCategory.SECURITY),
        FeatureItem(11, "استرجاع الحساب بالبصمة والوجه (Biometrics)", "دخول سريع وآمن باستخدام بصمة الإصبع أو مستشعر الوجه المدمج.", FeatureCategory.SECURITY),
        FeatureItem(12, "تشفير بيانات الدفع ومحفظة الطالب", "معاملات آمنة متوافقة مع معايير البنك المركزي المصري ومنصات الدفع.", FeatureCategory.SECURITY),
        FeatureItem(13, "حظر تلقائي للحسابات التي تشارك الأكواد", "خوارزمية ذكية لاكتشاف تداول الكود بين عدة طلاب وحظر الحساب فوراً.", FeatureCategory.SECURITY),
        FeatureItem(14, "سجل عمليات وتعديلات المساعدين (Audit Log)", "تسجيل تفصيلي لكل حركة يقوم بها مساعد المدرس في رصد الدرجات.", FeatureCategory.SECURITY),
        FeatureItem(15, "جدار حماية ضد الهجمات الإلكترونية (Cloudflare Armor)", "حماية السيرفرات من هجمات DDoS وضمان عمل المنصة وقت الذروة.", FeatureCategory.SECURITY),

        // التحفيز والـ Gamification (16-30)
        FeatureItem(16, "لوحة الشرف (Leaderboard) للأوائل", "ترتيب أفضل الطلاب في كل صف دراسي أسبوعياً وشهرياً مع كؤوس رقمية.", FeatureCategory.GAMIFICATION),
        FeatureItem(17, "نظام نقاط الخبرة (XP) التراكمية", "كسب 50 نقطة عند إنهاء كل حصة و 100 نقطة عند الحصول على الدرجة النهائية.", FeatureCategory.GAMIFICATION),
        FeatureItem(18, "متجر مكافآت النقاط", "استبدال نقاط الـ XP بخصومات على المذكرات والكتب المطبوعة والاشتراكات.", FeatureCategory.GAMIFICATION),
        FeatureItem(19, "شارات وأوسمة التميز (Badges)", "منح شارات مثل 'طالب الشهر'، 'المتفوق في النحو'، 'قاهر الفيزياء'.", FeatureCategory.GAMIFICATION),
        FeatureItem(20, "نظام تحديات المبارزة (Student vs Student)", "مبارزة معرفية سريعة من 5 أسئلة بين طالبين على نفس الدرس لرفع الحماس.", FeatureCategory.GAMIFICATION),
        FeatureItem(21, "مستويات الطالب ورتبته (Level Up)", "صعود مستويات الطالب (مبتدئ -> باحث -> عبقري -> علامة).", FeatureCategory.GAMIFICATION),
        FeatureItem(22, "شهادات تقدير ذكية PDF فورية", "توليد شهادة تقدير باسم الطالب ودرجته وتوقيع المعلم قابلة للطباعة.", FeatureCategory.GAMIFICATION),
        FeatureItem(23, "رسائل تشجيعية صوتية من المدرس", "تسجيل صوتي من المدرس يهنئ الطالب فور حصوله على الدرجة النهائية.", FeatureCategory.GAMIFICATION),
        FeatureItem(24, "تحدي 'سؤال اليوم' بفرصة ذهبية", "سؤال فكري متجدد يومياً في المادة يمنح نقاطاً مضاعفة لمن يجيبه أولاً.", FeatureCategory.GAMIFICATION),
        FeatureItem(25, "عملات EduCoins الافتراضية", "عملة داخل المنصة لشراء ثيمات وخلفيات وفتح ملازم حصرية.", FeatureCategory.GAMIFICATION),
        FeatureItem(26, "تخصيص الأفاتار والشخصية الرقمية", "اختيار وتخصيص صورة الطالب الرمزية بأزياء ورموز تعليمية ممتعة.", FeatureCategory.GAMIFICATION),
        FeatureItem(27, "نظام استمرارية المذاكرة (Study Streak)", "عداد يحسب الأيام المتتالية التي يدخل فيها الطالب للدراسة لحثه على الالتزام.", FeatureCategory.GAMIFICATION),
        FeatureItem(28, "مسابقات لايف بين السناتر والمحافظات", "دوري أسبوعي بين طلاب القاهرة والإسكندرية والمحافظات لإشعال التنافس.", FeatureCategory.GAMIFICATION),
        FeatureItem(29, "صندوق المفاجآت (Mystery Box)", "مكافأة عشوائية تفتح للطالب عند إكمال مشاهدة جميع محاضرات الشهر.", FeatureCategory.GAMIFICATION),
        FeatureItem(30, "عرض ترتيب الطالب على مستوى الجمهورية", "إحصائية توضح موقع الطالب المئوي مقارنة بآلاف الطلاب في نفس الصف.", FeatureCategory.GAMIFICATION),

        // الذكاء الاصطناعي والأدوات (31-45)
        FeatureItem(31, "المساعد الذكي (EduMaster AI Tutor)", "بوت ذكاء اصطناعي يشرح النقاط الصعبة ويجيب على أسئلة الطالب في أي وقت.", FeatureCategory.AI_TOOLS),
        FeatureItem(32, "تحليل نقاط الضعف التلقائي", "فحص نتائج امتحانات الطالب وتحديد الفصول والمفاهيم التي تحتاج مراجعة.", FeatureCategory.AI_TOOLS),
        FeatureItem(33, "تفريغ الصوت إلى نصوص مكتوبة (Transcription)", "تحويل صوت المدرس في الفيديو إلى نص مكتوب تحت الفيديو لسهولة المتابعة.", FeatureCategory.AI_TOOLS),
        FeatureItem(34, "محرك البحث داخل محتوى الفيديو", "البحث عن أي كلمة قالها المدرس للقفز مباشرة إلى الدقيقة والثانية في الفيديو.", FeatureCategory.AI_TOOLS),
        FeatureItem(35, "تصحيح الأسئلة المقالية آلياً", "تقييم إجابات الطالب المقالية بمطابقتها مع الإجابة النموذجية بالذكاء الاصطناعي.", FeatureCategory.AI_TOOLS),
        FeatureItem(36, "التلخيص الفوري للمذكرات والكتب", "تلخيص مذكرة 50 صفحة في 5 نقاط جوهرية للمراجعة السريعة ليلة الامتحان.", FeatureCategory.AI_TOOLS),
        FeatureItem(37, "جدول المذاكرة الذكي المقترح", "توليد جدول دراسي مخصص للطالب وفق أوقات فراغه ومستواه في المواد.", FeatureCategory.AI_TOOLS),
        FeatureItem(38, "التذكير الذكي بالحبيسات المتأخرة", "إشعار تفاعلي يذكر الطالب بالحصة التي لم يكملها قبل موعد الاختبار القادم.", FeatureCategory.AI_TOOLS),
        FeatureItem(39, "معجم المصطلحات والمفاهيم المدمج", "قاموس يوضح معنى أي مصطلح فيزيائي أو كيميائي أو لغوي بضغطة زر.", FeatureCategory.AI_TOOLS),
        FeatureItem(40, "الآلة الحاسبة العلمية المدمجة", "آلة حاسبة متطورة لحل المعادلات وتفاضل وتكامل الدوال دون مغادرة التطبيق.", FeatureCategory.AI_TOOLS),
        FeatureItem(41, "قارئ النصوص الصوتي لأصحاب الهمم", "تحويل نصوص الكتب إلى قراءة صوتية طبيعية لمساعدة ذوي الاحتياجات الخاصة.", FeatureCategory.AI_TOOLS),
        FeatureItem(42, "وضع التركيز الخارق (Zen Focus Mode)", "حجب الإشعارات الخارجية ومؤقت بومودورو 25 دقيقة مذاكرة مع أصوات هادئة.", FeatureCategory.AI_TOOLS),
        FeatureItem(43, "مولد أسئلة لا نهائي من بنك الأسئلة", "توليد كويز فوري من 10 أسئلة جديدة في أي درس لاختبار فهم الطالب السريع.", FeatureCategory.AI_TOOLS),
        FeatureItem(44, "تحليل انطباعات الطلاب (Sentiment Analysis)", "تحليل تعليقات الطلاب لمعرفة مدى استيعابهم للشرح ورضاهم عن الحصة.", FeatureCategory.AI_TOOLS),
        FeatureItem(45, "نظام التوصية الذكي بالدروس", "اقتراح فيديوهات إضافية تقوي الطالب في الجزئيات التي أخطأ فيها بالامتحان.", FeatureCategory.AI_TOOLS),

        // الإدارة والتقارير (46-60)
        FeatureItem(46, "لوحة تحكم للمساعدين (Moderators)", "إعطاء صلاحيات محددة للمساعدين لرصد الحضور وتصحيح الواجبات دون الوصول للأرباح.", FeatureCategory.MANAGEMENT),
        FeatureItem(47, "إرسال تقرير ولي الأمر على الواتساب", "ضغطة زر ترسل رسالة واتساب جاهزة للأب بدرجة ابنه وحضوره وغيابه ونسبة التزامه.", FeatureCategory.MANAGEMENT),
        FeatureItem(48, "إدارة المصروفات وأرباح الحصص والكتب", "سجل مالي يوضح إجمالي المبيعات، عمولات المنصة، وصافي ربح المعلم الشهري.", FeatureCategory.MANAGEMENT),
        FeatureItem(49, "جدول الحصص الأسبوعي التفاعلي", "تقويم ينظم مواعيد الحصص المباشرة والمسجلة لجميع السنين الدراسية.", FeatureCategory.MANAGEMENT),
        FeatureItem(50, "رفع الكتب بصيغة تفاعلية (Interactive PDF/EPUB)", "مذكرات تدعم التكبير والبحث والتظليل والروابط التفاعلية الداخلية.", FeatureCategory.MANAGEMENT),
        FeatureItem(51, "نظام الاستراحة والبث السريع (Office Hours)", "بث فيديو مباشر من المدرس للرد على استفسارات الطلاب ليلة الامتحان.", FeatureCategory.MANAGEMENT),
        FeatureItem(52, "الأرشفة التلقائية للمحاضرات القديمة", "تنظيم فصول المنهج في مجلدات سهلة التصفح حسب الترم الأول والترم الثاني.", FeatureCategory.MANAGEMENT),
        FeatureItem(53, "السبورة الذكية داخل التطبيق", "لوح رسم أبيض للمعلم لشرح المسائل باليد وإمكانية حفظ الشاشة كـ PDF.", FeatureCategory.MANAGEMENT),
        FeatureItem(54, "دمج Zoom و Google Meet المباشر", "بدء حصة تفاعلية مباشرة مع الطلاب دون مغادرة واجهة التطبيق.", FeatureCategory.MANAGEMENT),
        FeatureItem(55, "نظام تقسيط الكورسات والباقات", "إتاحة دفع ثمن الكورس على دفعتين أو الاشتراك بالحصة الفردية.", FeatureCategory.MANAGEMENT),
        FeatureItem(56, "إصدار الفواتير الإلكترونية المعتمدة", "فاتورة إلكترونية لكل عملية شراء كتاب أو اشتراك لحفظ حقوق الجميع.", FeatureCategory.MANAGEMENT),
        FeatureItem(57, "إحصائيات حرارية لمشاهدة الفيديو (Heatmap)", "معرفة أكثر دقيقة أعاد الطلاب مشاهدتها لاكتشاف أصعب نقطة بالشرح.", FeatureCategory.MANAGEMENT),
        FeatureItem(58, "جدولة نشر الفيديوهات والامتحانات", "تحديد تاريخ ووقت دقيق لنشر المحتوى تلقائياً في يوم محدد.", FeatureCategory.MANAGEMENT),
        FeatureItem(59, "نظام تتبع مخزون المذكرات الورقية", "إدارة عدد المذكرات المطبوعة المتوفرة بالسناتر ونقاط البيع.", FeatureCategory.MANAGEMENT),
        FeatureItem(60, "طباعة شيتات الحضور والباركود بضغطة زر", "تصدير قوائم الطلاب جاهزة للطباعة مع باركود خاص لكل طالب.", FeatureCategory.MANAGEMENT),

        // تجربة المستخدم والواجهة (61-75)
        FeatureItem(61, "الوضع الليلي الفاخر (Dark Mode)", "تصميم مريح للعينين وموفر لبطارية الهاتف للمذاكرة في المساء.", FeatureCategory.UX_UI),
        FeatureItem(62, "تحميل الفيديوهات والمذكرات بدون إنترنت", "تخزين المحتوى داخل التطبيق مشفراً للمشاهدة بدون الحاجة لاتصال بالنت.", FeatureCategory.UX_UI),
        FeatureItem(63, "مشغل فيديو متقدم يدعم السرعات (0.5x إلى 2.0x)", "تسريع أو إبطاء الشرح مع الحفاظ التام على وضوح نبرة الصوت.", FeatureCategory.UX_UI),
        FeatureItem(64, "ميزة صورة داخل صورة (Picture-in-Picture)", "استمرار تشغيل الشرح في نافذة عائمة أثناء كتابة الملاحظات بالدفتر.", FeatureCategory.UX_UI),
        FeatureItem(65, "دعم كامل للغة العربية والإنجليزية", "واجهة عربية أصيلة مع دعم المصطلحات الإنجليزية للغات والتجريبي.", FeatureCategory.UX_UI),
        FeatureItem(66, "المفضلة والأسئلة المحفوظة (Bookmarks)", "حفظ أهم المسائل والأفكار الصعبة للعودة إليها ومراجعتها قبل الامتحان.", FeatureCategory.UX_UI),
        FeatureItem(67, "منتدى النقاش الطلابي لكل حصة", "مساحة مخصصة لأسئلة الطلاب تحت كل فيديو وإجابة المساعدين والمعلم عليها.", FeatureCategory.UX_UI),
        FeatureItem(68, "ملاحظات مرتبطة بالزمن (Timestamped Notes)", "كتابة ملحوظة وحفظها مرتبطة بالدقيقة 15:20 للرجوع لها بنقرة واحدة.", FeatureCategory.UX_UI),
        FeatureItem(69, "باقات المواد المتكاملة (Bundles)", "شراء باقة الترم كامل مع المذكرات بخصم خاص بضغطة واحدة.", FeatureCategory.UX_UI),
        FeatureItem(70, "أنميشن تفاعلي سلس (Smooth Animations)", "تأثيرات بصرية وانتقالات ناعمة تجعل تجربة التعلم ممتعة وجذابة.", FeatureCategory.UX_UI),
        FeatureItem(71, "دعم كامل للشاشات اللوحية والتابلت", "واجهة مخصصة لشاشات تابلت الثانوية العامة والآيباد بدقة عالية.", FeatureCategory.UX_UI),
        FeatureItem(72, "دفتر المسودات والرسم الحر للطالب", "مساحة بيضاء يمكن للطالب فتحها أثناء الفيديو لحل المعادلات يدوياً.", FeatureCategory.UX_UI),
        FeatureItem(73, "قسم قصص النجاح وأوائل الثانوية", "مقابلات ونصائح ملهمة من أوائل الجمهورية في الأعوام السابقة.", FeatureCategory.UX_UI),
        FeatureItem(74, "نغمات إشعارات خاصة بصوت المدرس", "تنبيهات صوتية تحفيزية بصوت الأستاذ نفسه تجذب انتباه الطالب فوراً.", FeatureCategory.UX_UI),
        FeatureItem(75, "واجهة مينيمال مانعة للتشتت", "تصميم نقي يركز على المحتوى التعليمي ويبعد المشتتات البصرية.", FeatureCategory.UX_UI),

        // التجارة الإلكترونية والدفع (76-85)
        FeatureItem(76, "دعم بوابات الدفع المصرية (فوري، فودافون كاش، كروت الائتمان)", "شحن رصيد سهل عبر المحافظ الإلكترونية والمنافذ بمصر وكافة الدول.", FeatureCategory.COMMERCE),
        FeatureItem(77, "نظام الكروت والأكواد الورقية المشحونة", "توليد أكواد مطبوعة كروت كحت تباع بالمكتبات لشحن الحصص نقدياً.", FeatureCategory.COMMERCE),
        FeatureItem(78, "المحفظة الإلكترونية للطالب (EduWallet)", "رصيد داخل التطبيق يمكن شحنه واستخدامه في شراء الملازم والاختبارات.", FeatureCategory.COMMERCE),
        FeatureItem(79, "إهداء حصة أو مذكرة لصديق", "إمكانية شراء كورس وإرسال كود تفعيله كهدية لزميل دراسة.", FeatureCategory.COMMERCE),
        FeatureItem(80, "برنامج التسويق بالعمولة للطلاب (Affiliate)", "كود دعوة خاص للطالب يمنحه رصيداً مجانياً عند تسجيل أصدقائه بكود المعلم.", FeatureCategory.COMMERCE),
        FeatureItem(81, "عروض الخصومات الموسمية والأعياد", "إمكانية تطبيق كوبونات الخصم والاشتراكات السنوية المخفضة.", FeatureCategory.COMMERCE),
        FeatureItem(82, "سياسة استرجاع الرصيد الشفافة", "إمكانية إلغاء الاشتراك واسترجاع الرصيد خلال 24 ساعة وفق شروط محددة.", FeatureCategory.COMMERCE),
        FeatureItem(83, "خدمة 'ادرس الآن وادفع لاحقاً'", "تسهيل الاشتراك للطلاب المتعثرين والدفع مع بداية الشهر التالي.", FeatureCategory.COMMERCE),
        FeatureItem(84, "خيارات اشتراك مرنة (بالحصة / بالشهر / بالترم)", "حرية كاملة للطالب في اختيار نظام الدفع المناسب لظروفه.", FeatureCategory.COMMERCE),
        FeatureItem(85, "لوحة الإيرادات والأرباح البيانية للمعلم", "رسوم بيانية توضح نمو الإيرادات وتوزيع مبيعات الكتب في كل محافظة.", FeatureCategory.COMMERCE),

        // التواصل والتسويق (86-100)
        FeatureItem(86, "ستوري وقصص المدرس التفاعلية (Stories)", "نشر ستوري قصيرة بالإعلانات الهامة ومواعيد الامتحانات مثل إنستجرام.", FeatureCategory.COMMUNICATION),
        FeatureItem(87, "إرسال رسائل جماعية للطلاب (Broadcast)", "توجيه إشعار وتنبيه بنقرة واحدة لجميع طلاب ثالثة ثانوي المسجلين.", FeatureCategory.COMMUNICATION),
        FeatureItem(88, "تقييمات ومراجعات المدرس المستقلة", "إبداء آراء الطلاب وتقييماتهم بالنجوم حول جودة الشرح والمذكرات.", FeatureCategory.COMMUNICATION),
        FeatureItem(89, "شريط أخبار التعليم ووزارة التربية والتعليم", "تحديثات حية وموثوقة لقرارات الوزارة وجداول امتحانات الثانوية العامة.", FeatureCategory.COMMUNICATION),
        FeatureItem(90, "استطلاعات الرأي والتصويت التفاعلي (Polls)", "استطلاع رأي الطلاب حول موعد الحصة القادمة أو أصعب درس بالمنهج.", FeatureCategory.COMMUNICATION),
        FeatureItem(91, "ربط قنوات التواصل للمدرس (فيسبوك، يوتيوب، تليجرام)", "روابط مباشرة لمنصات المعلم الخارجية لتعزيز التواصل.", FeatureCategory.COMMUNICATION),
        FeatureItem(92, "نظام تذاكر الدعم الفني الذكي", "قسم لحل المشاكل التقنية والرد السريع على استفسارات الطلاب خلال دقائق.", FeatureCategory.COMMUNICATION),
        FeatureItem(93, "البث المباشر التفاعلي مع الشات الحي", "حصة لايف بجودة HD مع إمكانية رفع اليد وطرح الأسئلة الصوتية.", FeatureCategory.COMMUNICATION),
        FeatureItem(94, "فيديوهات قصيرة تعليمية (EduShorts)", "ريلز تعليمي 60 ثانية لشرح خدعة رياضية أو معلومة لغوية سريعة.", FeatureCategory.COMMUNICATION),
        FeatureItem(95, "صفحة المعلم التعريفية الشخصية (Landing Page)", "بروفايل احترافي يوضح سيرة المعلم وإنجازات طلابه ونماذج شرحه.", FeatureCategory.COMMUNICATION),
        FeatureItem(96, "تذكيرات الصلاة والأذكار والمحافظة على الصحة", "تنبيه لطيف للطالب بأداء الصلاة والراحة بعد ساعات المذاكرة الطويلة.", FeatureCategory.COMMUNICATION),
        FeatureItem(97, "بوابة ولي الأمر المنفصلة (Parent Portal)", "واجهة خاصة لولي الأمر لمتابعة سجل الحضور ودرجات الامتحانات دون تشتيت.", FeatureCategory.COMMUNICATION),
        FeatureItem(98, "تصدير البيانات لمنصات التعليم والسناتر", "تكامل البيانات وتصدير ملفات Excel و PDF معتمدة.", FeatureCategory.COMMUNICATION),
        FeatureItem(99, "المسابقات الكبرى وجوائز أوائل المحافظات", "جوائز قيمة (لابتوب، أجهزة تابلت، رحلات) للأول على مستوى التطبيق.", FeatureCategory.COMMUNICATION),
        FeatureItem(100, "خوارزمية الذكاء العاطفي ومراقبة الإجهاد", "تحليل أوقات استيقاظ الطالب وإرسال تنبيه ودود عند السهر لحثه على النوم الصحي.", FeatureCategory.COMMUNICATION)
    )
}
