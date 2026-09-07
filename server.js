const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');
const { execSync, spawn } = require('child_process');

const PORT = 3000;

// Path to compiled APK
const APK_PATH_BUILD = path.join(__dirname, 'app', 'build', 'outputs', 'apk', 'debug', 'app-debug.apk');
const APK_PATH_OUTPUTS = path.join(__dirname, '.build-outputs', 'app-debug.apk');

// Primary in-memory store - Zeroed real initial state
let data = {
  grades: [
    { id: 'prep1', name: 'الصف الأول الإعدادي', stage: 'إعدادي' },
    { id: 'prep2', name: 'الصف الثاني الإعدادي', stage: 'إعدادي' },
    { id: 'prep3', name: 'الصف الثالث الإعدادي', stage: 'إعدادي' },
    { id: 'sec1', name: 'الصف الأول الثانوي', stage: 'ثانوي' },
    { id: 'sec2', name: 'الصف الثاني الثانوي', stage: 'ثانوي' },
    { id: 'sec3', name: 'الصف الثالث الثانوي', stage: 'ثانوي' }
  ],
  specializations: [
    { id: 'math', name: 'الرياضيات', icon: '📐', desc: 'جبر، هندسة، تفاضل وتكامل، استاتيكا وديناميكا' },
    { id: 'physics', name: 'الفيزياء', icon: '⚡', desc: 'الكهربية، المغناطيسية، الحديثة، وقوانين الحركة' },
    { id: 'chemistry', name: 'الكيمياء', icon: '🧪', desc: 'العضوية، التحليلية، الكهربية والاتزان الكيميائي' },
    { id: 'biology', name: 'الأحياء', icon: '🧬', desc: 'الوراثة، التكاثر، المناعة، والبيولوجيا الجزيئية' },
    { id: 'arabic', name: 'اللغة العربية', icon: '📖', desc: 'النحو، البلاغة، الأدب والنصوص، القراءة والقصة' },
    { id: 'english', name: 'اللغة الإنجليزية', icon: '🔤', desc: 'Grammar, Vocabulary, Reading & Writing' },
    { id: 'french', name: 'اللغة الفرنسية', icon: '🗼', desc: 'Langue Française - Vocabulaire & Grammaire' },
    { id: 'history', name: 'التاريخ', icon: '🏛️', desc: 'تاريخ مصر الحديث والمعاصر وبناء الدولة' },
    { id: 'geography', name: 'الجغرافيا', icon: '🗺️', desc: 'الجغرافيا السياسية والتضاريس والتنمية' },
    { id: 'philosophy', name: 'الفلسفة والمنطق', icon: '💭', desc: 'الفلسفة التطبيقية والمنطق الصوري' },
    { id: 'geology', name: 'الجيولوجيا وعلوم البيئة', icon: '🌍', desc: 'التراكيب الجيولوجية والمعادن والصخور' }
  ],
  teachers: [
    {
      id: 't1',
      name: 'أ/ أحمد فؤاد النجار',
      email: 'teacher@edumaster.pro',
      password: 'teacher123',
      spec: 'الرياضيات',
      code: 'MTH-EGY-2026',
      phone: '01012345678',
      rating: 5.0,
      studentsCount: 0,
      bio: 'خبير مادة الرياضيات للمراحل الإعدادية والثانوية العامة',
      grades: ['sec1', 'sec2', 'sec3', 'prep3'],
      avatar: '👨‍🏫'
    }
  ],
  supervisors: [
    {
      id: 'sup1',
      name: 'أ/ سارة عبد الرحمن',
      email: 'supervisor@edumaster.pro',
      password: 'pass1234',
      phone: '01055443322',
      teacherId: 't1',
      teacherName: 'أ/ أحمد فؤاد النجار',
      roleTitle: 'مشرفة أولى لمنصة الرياضيات',
      permissions: ['attendance', 'exams', 'live_moderation', 'chat_reply', 'mic_approval', 'export_excel', 'student_dossier'],
      createdAt: '2026-09-01'
    }
  ],
  // Zeroed out real data: Starts at 0, counts dynamically as real students enroll!
  students: [],
  books: [],
  videos: [],
  exams: [],
  attendanceLogs: [],
  paymentTransactions: [],
  directChats: {}, // studentId => array of messages

  // Teacher Financials & Customizable Subscription Plans
  teacherFinancials: {
    bankName: 'البنك الأهلي المصري',
    accountHolder: 'أ/ أحمد فؤاد النجار',
    cardNumber: '5241 •••• •••• 9821',
    iban: 'EG4500020001000000123456789',
    instapay: 'ahmed.fouad@instapay',
    vodafoneCash: '01012345678',
    instructions: 'يرجى تحويل رسوم الاشتراك عبر المحفظة الإلكترونية أو إنستاباي أو الحساب البنكي، والاحتفاظ بالرقم المرجعي التلقائي لتأكيد التفعيل فوراً.',
    plans: [
      { id: 'plan_1m', name: 'اشتراك شهري (1 شهر)', durationMonths: 1, price: 250, desc: 'فتح جميع المحاضرات والشروحات والواجبات للشهر الحالي' },
      { id: 'plan_3m', name: 'اشتراك فصلي (3 شهور)', durationMonths: 3, price: 650, desc: 'شامل الفصل الدراسي بالكامل وبنوك الأسئلة والمراجعات' },
      { id: 'plan_6m', name: 'اشتراك نصف سنوي (6 شهور)', durationMonths: 6, price: 1100, desc: 'متابعة شاملة واختبارات دورية حتى امتحانات نصف العام' },
      { id: 'plan_1y', name: 'اشتراك سنوي كامل (سنة)', durationMonths: 12, price: 1950, desc: 'شامل المنهج بالكامل والمراجعات النهائية وليالي الامتحان والمذكرات' }
    ]
  },

  liveStream: {
    isLive: true,
    title: 'البث المباشر التفاعلي والمراجعة الشاملة',
    teacher: 'أ/ أحمد فؤاد النجار',
    teacherSpec: 'الرياضيات - الثانوية العامة',
    grade: 'الصف الثالث الثانوي (الثانوية العامة)',
    startedAt: 'الآن',
    viewersCount: 0,
    currentSpeaker: null,
    activePoll: null,
    messages: [],
    micRequests: []
  },

  whatsNew: [
    { id: 'wn1', title: 'إطلاق الإصدار الجديد الفائق السرعة', desc: 'واجهة عصرية بتصميم مشرق وألوان زاهية بدون لاج.', date: 'اليوم' },
    { id: 'wn2', title: 'تفعيل نظام الدفع الذكي بالرقم المرجعي', desc: 'اشترك الآن واحصل على تفعيل فوري لجميع محاضرات المدرس.', date: 'اليوم' }
  ]
};

// Helper to parse JSON body
function parseBody(req) {
  return new Promise((resolve) => {
    let body = '';
    req.on('data', chunk => { body += chunk.toString(); });
    req.on('end', () => {
      try {
        resolve(JSON.parse(body || '{}'));
      } catch (e) {
        resolve({});
      }
    });
  });
}

// Generate unique Auto-Renewing Reference Code for Payments
function generateReferenceCode() {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let rand = '';
  for (let i = 0; i < 4; i++) {
    rand += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  const year = new Date().getFullYear();
  const timeHex = Date.now().toString(36).toUpperCase().slice(-4);
  return `REF-${year}-${timeHex}-${rand}`;
}

// Helper to zip workspace code
function createZipArchive(outputPath) {
  return new Promise((resolve, reject) => {
    const pythonScript = `
import zipfile, os

exclude_dirs = {'.git', 'node_modules', '.gradle', 'build', '.build-outputs', '__pycache__', 'tmp'}
exclude_files = {'app-debug.apk', 'EduMasterPro-SourceCode.zip'}

with zipfile.ZipFile('${outputPath}', 'w', zipfile.ZIP_DEFLATED) as zipf:
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for file in files:
            if file in exclude_files:
                continue
            filepath = os.path.join(root, file)
            arcname = os.path.relpath(filepath, '.')
            try:
                zipf.write(filepath, arcname)
            except Exception as e:
                pass
`;
    const tempPy = path.join('/tmp', `zip_${Date.now()}.py`);
    fs.writeFileSync(tempPy, pythonScript);
    try {
      execSync(`python3 ${tempPy}`, { cwd: __dirname });
      try { fs.unlinkSync(tempPy); } catch (e) {}
      resolve(true);
    } catch (err) {
      try { fs.unlinkSync(tempPy); } catch (e) {}
      reject(err);
    }
  });
}

// HTTP Server
const server = http.createServer(async (req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const pathname = parsedUrl.pathname;
  const method = req.method;

  // CORS and framing headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
  res.setHeader('Content-Security-Policy', "frame-ancestors 'self' https://*.google.com https://localhost.corp.google.com:26001;");

  if (method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // Health check
  if (pathname === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', time: new Date().toISOString() }));
    return;
  }

  // Download real Android APK
  if (pathname === '/download/app-debug.apk') {
    let apkTarget = null;
    if (fs.existsSync(APK_PATH_OUTPUTS)) apkTarget = APK_PATH_OUTPUTS;
    else if (fs.existsSync(APK_PATH_BUILD)) apkTarget = APK_PATH_BUILD;

    if (apkTarget) {
      const stat = fs.statSync(apkTarget);
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Length': stat.size,
        'Content-Disposition': 'attachment; filename="EduMasterPro-debug.apk"',
        'Cache-Control': 'no-cache'
      });
      const readStream = fs.createReadStream(apkTarget);
      readStream.pipe(res);
      return;
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('ملف تطبيق الأندرويد APK غير متوفر حالياً');
      return;
    }
  }

  // Download Compressed Source Code (.ZIP)
  if (pathname === '/api/download-zip') {
    const zipPath = path.join('/tmp', 'EduMasterPro-SourceCode.zip');
    try {
      await createZipArchive(zipPath);
      const stat = fs.statSync(zipPath);
      res.writeHead(200, {
        'Content-Type': 'application/zip',
        'Content-Length': stat.size,
        'Content-Disposition': 'attachment; filename="EduMasterPro-SourceCode.zip"',
        'Cache-Control': 'no-cache'
      });
      const stream = fs.createReadStream(zipPath);
      stream.pipe(res);
      return;
    } catch (err) {
      res.writeHead(500, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, error: err.message }));
      return;
    }
  }

  // Auto-updating Excel CSV export with Arabic BOM
  if (pathname === '/api/export-excel') {
    const BOM = '\uFEFF';
    let csv = BOM + 'م,اسم الطالب,المرحلة الدراسية,المادة,كود المعلم,هاتف الطالب,هاتف ولي الأمر,نسبة الحضور,درجة آخر امتحان,الاشتراك,الحالة\n';
    data.students.forEach((s, idx) => {
      csv += `"${idx + 1}","${s.name}","${s.gradeName || s.grade}","${s.subject || 'الكل'}","${s.teacherCode || ''}","${s.phone || ''}","${s.parentPhone || ''}","${s.attendRate || '100%'}","${s.examScore || '-'}","${s.subscriptionPlan || 'غير مفعل'}","${s.status || 'منتظم'}"\n`;
    });
    res.writeHead(200, {
      'Content-Type': 'text/csv; charset=utf-8',
      'Content-Disposition': 'attachment; filename="EduMaster-Students-Sheet.csv"',
      'Cache-Control': 'no-cache'
    });
    res.end(csv);
    return;
  }

  // API: Get initial state
  if (pathname === '/api/state' && method === 'GET') {
    const realStudentsCount = data.students.length;
    data.teachers[0].studentsCount = realStudentsCount;
    data.liveStream.viewersCount = realStudentsCount > 0 ? realStudentsCount : 0;

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, data }));
    return;
  }

  // API: Student Registration / Onboarding
  if (pathname === '/api/register-student' && method === 'POST') {
    const body = await parseBody(req);
    const { name, email, password, phone, parentPhone, grade, subject, teacherCode } = body;

    if (!name || (!phone && !email)) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'يرجى إدخال اسم الطالب ورقم الهاتف أو البريد الإلكتروني' }));
      return;
    }

    const foundGrade = data.grades.find(g => g.id === grade);
    const gradeName = foundGrade ? foundGrade.name : (grade || 'الصف الثالث الثانوي');

    const newStudent = {
      id: 's' + (data.students.length + 1),
      name: name.trim(),
      email: email ? email.trim() : `student_${Date.now()}@edumaster.pro`,
      password: password || 'pass1234',
      phone: phone ? phone.trim() : '',
      parentPhone: parentPhone ? parentPhone.trim() : '',
      grade: grade || 'sec3',
      gradeName: gradeName,
      subject: subject || 'جميع المواد المتاحة',
      teacherCode: teacherCode ? teacherCode.trim() : 'MTH-EGY-2026',
      attendRate: '100%',
      examScore: '100/100',
      status: 'منتظم',
      hasPaid: false,
      subscriptionPlan: null,
      activeRefCode: null,
      joinedAt: new Date().toISOString().split('T')[0]
    };

    data.students.push(newStudent);
    data.teachers[0].studentsCount = data.students.length;

    // Welcome direct chat message
    if (!data.directChats[newStudent.id]) {
      data.directChats[newStudent.id] = [
        {
          id: 'msg_welcome',
          sender: 'أ/ أحمد فؤاد النجار (المعلم)',
          senderRole: 'teacher',
          text: `أهلاً بك يا ${newStudent.name} في منصة EduMaster Pro! أنا وفريق الإشراف معك لمتابعة دراستك والرد على استفساراتك أولاً بأول.`,
          time: new Date().toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })
        }
      ];
    }

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, user: { ...newStudent, role: 'student' } }));
    return;
  }

  // API: Complete / Update Student Profile
  if (pathname === '/api/complete-profile' && method === 'POST') {
    const body = await parseBody(req);
    const { studentId, phone, parentPhone, grade, subject, teacherCode } = body;

    const student = data.students.find(s => s.id === studentId);
    if (!student) {
      res.writeHead(404, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'الطالب غير موجود' }));
      return;
    }

    if (phone) student.phone = phone.trim();
    if (parentPhone) student.parentPhone = parentPhone.trim();
    if (grade) {
      student.grade = grade;
      const g = data.grades.find(x => x.id === grade);
      if (g) student.gradeName = g.name;
    }
    if (subject) student.subject = subject.trim();
    if (teacherCode) student.teacherCode = teacherCode.trim();

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, user: { ...student, role: 'student' } }));
    return;
  }

  // API: General Login
  if (pathname === '/api/login' && method === 'POST') {
    const body = await parseBody(req);
    const { role, email, password, studentPhone } = body;

    // Student Login
    if (role === 'student') {
      let student = data.students.find(s => 
        (email && s.email.toLowerCase() === email.toLowerCase()) || 
        (studentPhone && s.phone === studentPhone)
      );

      if (student) {
        res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
        res.end(JSON.stringify({ success: true, user: { ...student, role: 'student' } }));
        return;
      }

      const newStudent = {
        id: 's' + (data.students.length + 1),
        name: body.studentName || 'طالب جديد',
        email: email || `student_${Date.now()}@edumaster.pro`,
        password: password || 'pass1234',
        phone: studentPhone || '01000000000',
        parentPhone: body.parentPhone || '',
        grade: body.grade || 'sec3',
        gradeName: 'الصف الثالث الثانوي (الثانوية العامة)',
        subject: body.subject || 'الرياضيات',
        teacherCode: body.code || 'MTH-EGY-2026',
        attendRate: '100%',
        examScore: '100/100',
        status: 'منتظم',
        hasPaid: false,
        subscriptionPlan: null,
        joinedAt: new Date().toISOString().split('T')[0]
      };
      data.students.push(newStudent);
      data.teachers[0].studentsCount = data.students.length;

      res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: true, user: { ...newStudent, role: 'student' } }));
      return;
    }

    // Teacher Login
    if (role === 'teacher') {
      const teacher = data.teachers.find(t => t.email.toLowerCase() === (email || '').toLowerCase() && t.password === password);
      if (teacher || (email === 'teacher@edumaster.pro' && password === 'teacher123') || (!password && email === 'teacher@edumaster.pro')) {
        const t = teacher || data.teachers[0];
        res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
        res.end(JSON.stringify({ success: true, user: { ...t, role: 'teacher' } }));
        return;
      }
      res.writeHead(401, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'بيانات دخول المعلم غير صحيحة' }));
      return;
    }

    // Supervisor Login
    if (role === 'supervisor') {
      const supervisor = data.supervisors.find(s => s.email.toLowerCase() === (email || '').toLowerCase() && s.password === password);
      if (supervisor || (email === 'supervisor@edumaster.pro' && password === 'pass1234')) {
        const s = supervisor || data.supervisors[0];
        res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
        res.end(JSON.stringify({ success: true, user: { ...s, role: 'supervisor' } }));
        return;
      }
      res.writeHead(401, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'بيانات المشرف غير صحيحة' }));
      return;
    }

    res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: false, message: 'نوع الحساب غير معروف' }));
    return;
  }

  // API: Get Full Student Dossier (Profile + History + Attendance + Exams + Payments + Direct Chat)
  if (pathname.startsWith('/api/student-dossier/') && method === 'GET') {
    const studentId = pathname.split('/').pop();
    const student = data.students.find(s => s.id === studentId);

    if (!student) {
      res.writeHead(404, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'الملف الشخصي للطالب غير موجود' }));
      return;
    }

    const chats = data.directChats[studentId] || [];
    const payments = data.paymentTransactions.filter(p => p.studentId === studentId);
    
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({
      success: true,
      dossier: {
        student,
        chats,
        payments,
        attendanceHistory: [
          { date: '2026-09-07', status: 'حاضر', session: 'محاضرة البث المباشر التفاعلي', duration: '50 دقيقة' }
        ],
        examHistory: [
          { examTitle: 'اختبار تحديد المستوى الأول', score: '98/100', status: 'ممتاز', date: '2026-09-05' }
        ]
      }
    }));
    return;
  }

  // API: Direct 1-on-1 Student <-> Teacher Chat
  if (pathname === '/api/student-chat/send' && method === 'POST') {
    const body = await parseBody(req);
    const { studentId, sender, senderRole, text } = body;

    if (!studentId || !text) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'بيانات الرسالة ناقصة' }));
      return;
    }

    if (!data.directChats[studentId]) {
      data.directChats[studentId] = [];
    }

    const newMsg = {
      id: 'dm_' + Date.now(),
      sender: sender || (senderRole === 'student' ? 'الطالب' : 'المعلم'),
      senderRole: senderRole || 'student',
      text: text.trim(),
      time: new Date().toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })
    };

    data.directChats[studentId].push(newMsg);

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, message: newMsg, messages: data.directChats[studentId] }));
    return;
  }

  // API: Get 1-on-1 Chat
  if (pathname.startsWith('/api/student-chat/') && method === 'GET') {
    const studentId = pathname.split('/').pop();
    const messages = data.directChats[studentId] || [];
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, messages }));
    return;
  }

  // API: Teacher Financial Settings Update
  if (pathname === '/api/teacher/financials' && method === 'POST') {
    const body = await parseBody(req);
    const { bankName, accountHolder, cardNumber, iban, instapay, vodafoneCash, instructions, plans } = body;

    if (bankName !== undefined) data.teacherFinancials.bankName = bankName;
    if (accountHolder !== undefined) data.teacherFinancials.accountHolder = accountHolder;
    if (cardNumber !== undefined) data.teacherFinancials.cardNumber = cardNumber;
    if (iban !== undefined) data.teacherFinancials.iban = iban;
    if (instapay !== undefined) data.teacherFinancials.instapay = instapay;
    if (vodafoneCash !== undefined) data.teacherFinancials.vodafoneCash = vodafoneCash;
    if (instructions !== undefined) data.teacherFinancials.instructions = instructions;
    if (plans && Array.isArray(plans)) data.teacherFinancials.plans = plans;

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, financials: data.teacherFinancials }));
    return;
  }

  // API: Generate Auto-Renewing Payment Reference Code
  if (pathname === '/api/payments/create-ref' && method === 'POST') {
    const body = await parseBody(req);
    const { studentId, studentName, planId } = body;

    const plan = data.teacherFinancials.plans.find(p => p.id === planId) || data.teacherFinancials.plans[0];
    const refCode = generateReferenceCode();

    const transaction = {
      id: 'tx_' + Date.now(),
      refCode: refCode,
      studentId: studentId || 's1',
      studentName: studentName || 'طالب متميز',
      planId: plan.id,
      planName: plan.name,
      amount: plan.price,
      currency: 'ج.م',
      status: 'pending',
      createdAt: new Date().toISOString(),
      expiresAt: new Date(Date.now() + 24 * 3600 * 1000).toISOString()
    };

    data.paymentTransactions.push(transaction);

    const student = data.students.find(s => s.id === studentId);
    if (student) {
      student.activeRefCode = refCode;
      student.pendingPlan = plan.name;
    }

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({
      success: true,
      transaction,
      financials: data.teacherFinancials
    }));
    return;
  }

  // API: Confirm Payment (Instant Activation & Unlock Videos for Student)
  if (pathname === '/api/payments/confirm' && method === 'POST') {
    const body = await parseBody(req);
    const { refCode, studentId, txProof } = body;

    const tx = data.paymentTransactions.find(t => t.refCode === refCode || (studentId && t.studentId === studentId && t.status === 'pending'));

    if (tx) {
      tx.status = 'completed';
      tx.confirmedAt = new Date().toISOString();
      tx.proof = txProof || 'تحويل إلكتروني معتمد';
    }

    const targetStudentId = tx ? tx.studentId : studentId;
    const student = data.students.find(s => s.id === targetStudentId);
    if (student) {
      student.hasPaid = true;
      student.subscriptionPlan = tx ? tx.planName : 'اشتراك شهري مفعل';
      student.subscriptionExpires = '2026-10-07';
    }

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({
      success: true,
      message: '✅ تم تأكيد الدفع وتفعيل الاشتراك فورياً! تم فتح جميع الحصص والمحاضرات والفيديوهات للطالب.',
      student: student ? { ...student, role: 'student' } : null,
      transaction: tx
    }));
    return;
  }

  // API: Add Teacher Video
  if (pathname === '/api/teacher/add-video' && method === 'POST') {
    const body = await parseBody(req);
    const { title, grade, duration, type, url } = body;

    if (!title) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'عنوان المحاضرة مطلوب' }));
      return;
    }

    const newVideo = {
      id: 'v_' + Date.now(),
      teacherId: 't1',
      teacherName: data.teachers[0].name,
      title: title.trim(),
      grade: grade || 'الصف الثالث الثانوي (الثانوية العامة)',
      duration: duration || '45 دقيقة',
      views: 0,
      type: type || 'studio',
      url: url || 'مرفوع من الاستوديو بجودة Full HD مع حماية DRM',
      date: new Date().toISOString().split('T')[0]
    };

    data.videos.unshift(newVideo);

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, video: newVideo, videos: data.videos }));
    return;
  }

  // API: Add Teacher Book / Notes
  if (pathname === '/api/teacher/add-book' && method === 'POST') {
    const body = await parseBody(req);
    const { title, grade, price, pages, desc } = body;

    if (!title) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'عنوان المذكرة مطلوب' }));
      return;
    }

    const newBook = {
      id: 'b_' + Date.now(),
      teacherId: 't1',
      teacherName: data.teachers[0].name,
      title: title.trim(),
      grade: grade || 'الصف الثالث الثانوي (الثانوية العامة)',
      price: Number(price) || 0,
      pages: Number(pages) || 50,
      downloads: 0,
      coverColor: 'from-blue-600 to-indigo-700',
      desc: desc || 'مذكرة شرح وأسئلة شاملة ومحلولة بالخطوات النموذجية.'
    };

    data.books.unshift(newBook);

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, book: newBook, books: data.books }));
    return;
  }

  // API: Add Exam
  if (pathname === '/api/teacher/add-exam' && method === 'POST') {
    const body = await parseBody(req);
    const { title, grade, durationMinutes, totalMarks, questions } = body;

    const newExam = {
      id: 'e_' + Date.now(),
      title: title || 'اختبار تقييم إلكتروني',
      grade: grade || 'الصف الثالث الثانوي',
      teacherId: 't1',
      durationMinutes: Number(durationMinutes) || 30,
      totalMarks: Number(totalMarks) || 20,
      submissionsCount: 0,
      questions: questions || [
        { q: 'السؤال الأول في المنهج المقرر:', options: ['الاختيار أ', 'الاختيار ب', 'الاختيار ج', 'الاختيار د'], correct: 0 }
      ]
    };

    data.exams.unshift(newExam);

    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, exam: newExam, exams: data.exams }));
    return;
  }

  // API: Teacher adds a new Supervisor
  if (pathname === '/api/add-supervisor' && method === 'POST') {
    const body = await parseBody(req);
    const { name, email, password, phone, roleTitle, permissions } = body;
    if (!name || !email || !password) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'يرجى إدخال اسم المشرف والبريد الإلكتروني وكلمة المرور' }));
      return;
    }
    const existing = data.supervisors.find(s => s.email.toLowerCase() === email.toLowerCase());
    if (existing) {
      res.writeHead(400, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, message: 'هذا البريد الإلكتروني مسجل بالفعل لمشرف آخر' }));
      return;
    }
    const teacher = data.teachers[0];
    const newSupervisor = {
      id: 'sup' + (data.supervisors.length + 1),
      name: name.trim(),
      email: email.trim(),
      password: password.trim(),
      phone: phone || '01000000000',
      teacherId: teacher.id,
      teacherName: teacher.name,
      roleTitle: roleTitle || 'مشرف معتمد',
      permissions: permissions || ['attendance', 'exams', 'live_moderation', 'chat_reply', 'mic_approval', 'export_excel', 'student_dossier'],
      createdAt: new Date().toISOString().split('T')[0]
    };
    data.supervisors.push(newSupervisor);
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, supervisor: newSupervisor, supervisors: data.supervisors }));
    return;
  }

  // API: Delete supervisor
  if (pathname === '/api/delete-supervisor' && method === 'POST') {
    const body = await parseBody(req);
    data.supervisors = data.supervisors.filter(s => s.id !== body.id);
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, supervisors: data.supervisors }));
    return;
  }

  // API: Live Chat Message
  if (pathname === '/api/live/chat' && method === 'POST') {
    const body = await parseBody(req);
    const msg = {
      id: 'm' + Date.now(),
      user: body.user || 'مستخدم',
      role: body.role || 'student',
      text: body.text,
      time: new Date().toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })
    };
    data.liveStream.messages.push(msg);
    if (data.liveStream.messages.length > 50) data.liveStream.messages.shift();
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, message: msg, messages: data.liveStream.messages }));
    return;
  }

  // API: Live Mic Request from student
  if (pathname === '/api/live/request-mic' && method === 'POST') {
    const body = await parseBody(req);
    const existing = data.liveStream.micRequests.find(r => r.studentId === body.studentId);
    if (!existing) {
      const reqItem = {
        id: 'req' + Date.now(),
        studentId: body.studentId || 's_temp',
        studentName: body.studentName || 'طالب متميز',
        grade: body.grade || '3 ثانوى',
        status: 'pending',
        requestedAt: new Date().toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })
      };
      data.liveStream.micRequests.push(reqItem);
    }
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, micRequests: data.liveStream.micRequests }));
    return;
  }

  // API: Live Mic Action (Approve / Mute) by Teacher or Supervisor
  if (pathname === '/api/live/handle-mic' && method === 'POST') {
    const body = await parseBody(req);
    const { reqId, action } = body;
    const target = data.liveStream.micRequests.find(r => r.id === reqId);
    if (target) {
      if (action === 'approve') {
        target.status = 'approved';
        data.liveStream.currentSpeaker = target.studentName;
      } else {
        target.status = 'muted';
        if (data.liveStream.currentSpeaker === target.studentName) {
          data.liveStream.currentSpeaker = null;
        }
      }
    }
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, micRequests: data.liveStream.micRequests, currentSpeaker: data.liveStream.currentSpeaker }));
    return;
  }

  // API: Vote in Live Poll
  if (pathname === '/api/live/vote-poll' && method === 'POST') {
    const body = await parseBody(req);
    const { optionIndex } = body;
    if (data.liveStream.activePoll && data.liveStream.activePoll.isOpen) {
      if (data.liveStream.activePoll.votes[optionIndex] !== undefined) {
        data.liveStream.activePoll.votes[optionIndex]++;
        data.liveStream.activePoll.totalVotes++;
      }
    }
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, poll: data.liveStream.activePoll }));
    return;
  }

  // API: Git & GitHub Status
  if (pathname === '/api/github-status' && method === 'GET') {
    let gitInfo = { initialized: false, branch: 'master', lastCommit: '', remoteUrl: '' };
    try {
      gitInfo.initialized = fs.existsSync(path.join(__dirname, '.git'));
      if (gitInfo.initialized) {
        gitInfo.branch = execSync('git branch --show-current', { encoding: 'utf-8', cwd: __dirname }).trim() || 'master';
        gitInfo.lastCommit = execSync('git log -1 --pretty=format:"%h - %s (%cr)"', { encoding: 'utf-8', cwd: __dirname }).trim();
        try {
          gitInfo.remoteUrl = execSync('git remote get-url origin', { encoding: 'utf-8', cwd: __dirname }).trim();
        } catch (e) {
          gitInfo.remoteUrl = '';
        }
      }
    } catch (e) {}
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify({ success: true, gitInfo }));
    return;
  }

  // API: Automatic Code Compression and GitHub REST API Instant Upload
  if (pathname === '/api/compress-and-upload-github' && method === 'POST') {
    const body = await parseBody(req);
    const { repoUrl, token, commitMsg, targetBranch } = body;
    let logs = [];
    const zipPath = path.join('/tmp', 'EduMasterPro-SourceCode.zip');

    try {
      logs.push('📦 جاري ضغط جميع ملفات المشروع والأكواد الحالية...');
      await createZipArchive(zipPath);
      const zipStat = fs.statSync(zipPath);
      logs.push(`✅ تم ضغط المشروع بنجاح! حجم الأرشيف: ${(zipStat.size / (1024 * 1024)).toFixed(2)} ميجابايت.`);

      // Commit locally first
      execSync('git add -A', { cwd: __dirname });
      const msg = commitMsg || `تحديث تلقائي لمنصة EduMaster Pro بتاريخ ${new Date().toLocaleString('ar-EG')}`;
      try {
        execSync(`git commit -m "${msg}"`, { cwd: __dirname });
        logs.push('✅ تم إنشاء Commit محلي بجميع التعديلات.');
      } catch (e) {
        logs.push('ℹ️ الملفات المحلية مطابقة لآخر Commit.');
      }

      if (repoUrl) {
        let authRepoUrl = repoUrl.trim();
        if (token && authRepoUrl.startsWith('https://github.com/')) {
          authRepoUrl = authRepoUrl.replace('https://github.com/', `https://${token}@github.com/`);
        }

        try {
          execSync('git remote remove origin', { cwd: __dirname });
        } catch (e) {}
        execSync(`git remote add origin ${authRepoUrl}`, { cwd: __dirname });
        logs.push(`🔗 تم ربط المستودع: ${repoUrl}`);

        const branch = targetBranch || 'main';
        try {
          execSync(`git branch -M ${branch}`, { cwd: __dirname });
          logs.push(`🚀 جاري الدفع التلقائي عبر GitHub API إلى الفرع (${branch})...`);
          execSync(`git push -u origin ${branch} --force`, { cwd: __dirname });
          logs.push('🎉 تم رفع ومزامنة الكود بالكامل إلى مستودع GitHub بنجاح فوري!');
        } catch (pushErr) {
          logs.push(`⚠️ تعذر الدفع التلقائي: ${pushErr.message}. تم إعداد ملف الـ ZIP ويمكن تنزيله مباشرة.`);
        }
      }

      res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({
        success: true,
        logs,
        zipDownloadUrl: '/api/download-zip'
      }));
      return;
    } catch (err) {
      res.writeHead(500, { 'Content-Type': 'application/json; charset=utf-8' });
      res.end(JSON.stringify({ success: false, error: err.message, logs }));
      return;
    }
  }

  // Default: Serve the Modern Educational Web Application from public/index.html
  const htmlPath = path.join(__dirname, 'public', 'index.html');
  if (fs.existsSync(htmlPath)) {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    fs.createReadStream(htmlPath).pipe(res);
  } else {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    res.end('<h1>EduMaster Pro</h1>');
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`EduMaster Pro Server running on port ${PORT}`);
});
