package com.presentation.company_detail.Scene.review_detail_scene

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.domain.entity.Ratings
import com.domain.entity.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DetailUIState(
    val reviews: List<Review> = emptyList(),
    val isFullModeList: List<Boolean> = emptyList(),
    val isFollowingCompany: Boolean = false,
    val showBottomSheet: Boolean = false
)

@HiltViewModel
class ReviewDetailViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidTapFollowCompanyButton,
        DidTapWriteReviewButton,
        DidTapLikeReviewButton,
        DidTapReviewCard,
        DidTapCommentButton,
        DidCloseBottomSheet
    }

    private var _uiState by mutableStateOf(DetailUIState())
    val uiState: DetailUIState get() = _uiState

    fun handleAction(action: Action, index: Int? = null) {
        when (action) {
            Action.DidTapFollowCompanyButton -> {
                _uiState = _uiState.copy(
                    isFollowingCompany = !_uiState.isFollowingCompany
                )
            }
            Action.DidTapWriteReviewButton -> {
                Log.d("버튼탭", "리뷰 버튼이 탭 되었음")
            }
            Action.DidTapLikeReviewButton -> {
                index?.let {
                    val updatedReviews = _uiState.reviews.toMutableList()
                    updatedReviews[index] = updatedReviews[index].copy(
                        liked = !updatedReviews[index].liked,
                        likeCount = (if (updatedReviews[index].liked)
                            updatedReviews[index].likeCount - 1
                        else
                            updatedReviews[index].likeCount + 1).coerceAtLeast(0)
                    )
                    _uiState = _uiState.copy(reviews = updatedReviews)
                }
            }
            Action.DidTapReviewCard -> {
                index?.let {
                    val updatedFullModeList = _uiState.isFullModeList.toMutableList()
                    updatedFullModeList[index] = !updatedFullModeList[index]
                    _uiState = _uiState.copy(isFullModeList = updatedFullModeList)
                }
            }
            Action.DidTapCommentButton, Action.DidCloseBottomSheet -> {
                _uiState = _uiState.copy(showBottomSheet = !_uiState.showBottomSheet)
            }
        }
    }

    init {
        val mockReviews = generateMockReviews()
        _uiState = DetailUIState(
            reviews = mockReviews,
            isFullModeList = List(mockReviews.size) { false }
        )
    }

    private fun generateMockReviews(): List<Review> = List(15) { idx ->
        val ratings = Ratings(
            COMPANY_CULTURE   = 2.5 + (idx % 3) * 0.5,
            MANAGEMENT        = 2.0 + (idx % 2) * 0.5,
            SALARY            = 3.5 + (idx % 4) * 0.5,
            WELFARE           = 2.5 + (idx % 3) * 0.5,
            WORK_LIFE_BALANCE = 2.0 + (idx % 2) * 0.5
        )
        val totalRating = with(ratings) {
            listOf(
                COMPANY_CULTURE, MANAGEMENT, SALARY, WELFARE, WORK_LIFE_BALANCE
            ).average().let { "%.1f".format(it).toDouble() }
        }

        Review(
            id                 = idx + 1,
            title              = "좋은 회사입니다. #${idx + 1}",
            nickName           = "사용자${idx + 1}",
            jobRole            = "백엔드 개발자",
            employmentPeriod   = "1년 이상",
            createdAt          = "2025-05-${"%02d".format(23 + idx)}T17:40:33",
            advantagePoint     = advantageSamples[idx % advantageSamples.size],
            disadvantagePoint  = disadvantageSamples[idx % disadvantageSamples.size],
            managementFeedback = feedbackSamples[idx % feedbackSamples.size],
            commentCount       = (10..30).random(),
            likeCount          = (0..20).random(),
            liked              = idx % 3 == 0,
            ratings            = ratings,
            totalRating        = totalRating
        )
    }

    companion object {
        /* 200~300자 샘플 문단들 */
        private val advantageSamples = listOf(
            "사내 복지 포인트로 카페테리아 포인트와 자기계발비를 넉넉하게 지원받을 수 있습니다. "
                    + "선배들이 멘토링을 열심히 해 주어 적응 기간이 짧고, 회식이 강제가 아니라는 점도 장점입니다. "
                    + "최신 장비 지급, 자율 출퇴근제, 원격 근무 선택제가 있어 워라밸을 체감하기 좋습니다.",
            "프로젝트마다 코드 리뷰 문화가 잘 정착되어 있어 성장 속도가 빠릅니다. "
                    + "디자인 시스템이 사내에 구축되어 있어 개발·디자이너 협업이 효율적이고, 성과급 지급이 투명합니다. "
                    + "업무 관련 온라인 세미나·컨퍼런스 참석비를 회사가 전액 지원해 줍니다.",
            "수평적인 분위기라 직급 구분 없이 자유롭게 의견을 제시할 수 있습니다. "
                    + "사내 헬스장과 샤워실, 마사지실이 있어 근무 중에도 건강 관리를 할 수 있고, "
                    + "직원 전용 카페에서 커피를 무료로 제공해 주어 작은 만족감이 큽니다."
        )

        private val disadvantageSamples = listOf(
            "신규 인력 충원이 늦어 주요 서비스의 담당 업무가 과중되는 시기가 종종 있습니다. "
                    + "프로세스 개선이 진행 중이라지만 아직 문서화나 지식 공유 체계가 부족해 온보딩이 길어지는 편입니다. "
                    + "구형 레거시 코드가 남아 있어 유지 보수 난도가 높습니다.",
            "개발자 대비 기획·디자인 인력이 적어 요구 사항이 자주 변경됩니다. "
                    + "연말에 목표 매출을 맞추기 위해 야근이 몰리는 경향이 있어 일정 관리 스트레스가 있습니다. "
                    + "회의가 많아 실 코딩 시간이 줄어드는 시기가 있습니다.",
            "프로덕트 라인업이 다양하다 보니 팀 간 커뮤니케이션이 느릴 때가 많습니다. "
                    + "사내 포상 제도가 있으나 실질 인센티브가 크지 않아 동기 부여가 약하다고 느껴집니다. "
                    + "전반적인 연봉 수준은 동종 업계 평균보다 약간 낮습니다."
        )

        private val feedbackSamples = listOf(
            "OKR을 적용한다면 목표와 성과가 보다 명확해져 불필요한 회의가 줄어들 것 같습니다. "
                    + "또한 레거시 서비스에 대한 단계적 리팩터링 로드맵을 수립해 주시면 업무 집중도가 높아질 것입니다. "
                    + "개발 조직 전용 기술 세미나를 분기별로 열어 주셨으면 합니다.",
            "프로젝트 관리 툴을 통일해 커뮤니케이션 채널을 단순화해 주셨으면 좋겠습니다. "
                    + "야근 수당 대신 유연 근무제를 확대 도입하면 워라밸 만족도가 크게 올라갈 것 같습니다. "
                    + "사내 카페테리아 메뉴를 주기적으로 개선해 주시면 더욱 만족할 것 같습니다.",
            "사내 교육비 지원 상한을 높여 최신 기술 스터디를 활성화해 주시면 좋겠습니다. "
                    + "성과급 산정 기준을 사전에 투명하게 공유해 주신다면 동기 부여가 한층 강화될 것 같습니다. "
                    + "직군·직급별 멘토링 제도를 공식화해 주시면 좋겠습니다."
        )
    }
}