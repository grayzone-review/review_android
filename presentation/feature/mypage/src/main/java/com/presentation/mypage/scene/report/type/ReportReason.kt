package com.presentation.mypage.scene.report.type

enum class ReportReason(val rawValue: String) {
    HARMFUL_CONTENT("음란/불법/청소년 유해"),
    PROMOTIONAL_CONTENT("홍보성"),
    HARASSMENT("비방/비하/욕설"),
    PERSONAL_INFO_LEAK("개인정보노출"),
    BUG("앱 사용 중 버그 발생");
}