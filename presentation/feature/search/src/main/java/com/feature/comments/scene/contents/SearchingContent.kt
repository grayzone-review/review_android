package com.feature.comments.scene.contents

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import colors.CS
import com.domain.entity.RecentCompany
import com.domain.entity.SearchedCompany
import com.example.presentation.designsystem.typography.Typography
import com.feature.comments.scene.contents.SearchingContentViewModel.Action.*
import com.feature.comments.scene.SearchUIState
import com.feature.comments.scene.contents.SearchingContent.*
import preset_ui.icons.ClockIcon
import preset_ui.icons.CloseLine
import preset_ui.icons.InfoIcon
import preset_ui.icons.SearchLineIcon
import preset_ui.icons.StarFilled
import javax.inject.Inject
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchingContent(
    viewModel: SearchingContentViewModel = hiltViewModel(),
    searchUIState: SearchUIState
) {
    val autocompletedCompanies by viewModel.autocompletedCompanies.collectAsState()
    val currentQuery = searchUIState.searchBarValue

    LaunchedEffect(currentQuery) {
        viewModel.handleAction(DidUpdateSearchBarValue, text = currentQuery.text)
    }

    val recentCompanies = listOf(
        RecentCompany(
            id = 1,
            companyName = "스타벅스 석촌역점",
            companyAddress = "서울특별시 송파구 백제고분로 358 1층"
        ),
        RecentCompany(
            id = 2,
            companyName = "버거킹 강남역점",
            companyAddress = "서울특별시 강남구 테헤란로 101"
        ),
        RecentCompany(
            id = 3,
            companyName = "투썸플레이스 건대입구점",
            companyAddress = "서울특별시 광진구 아차산로 200"
        )
    )

    if (searchUIState.searchBarValue.text.isEmpty()) {
        if (recentCompanies.isEmpty()) {
            EmptyView(searchingContent = Recent)
        } else {
            RecentViewedCompanyList(
                recentCompanies = recentCompanies,
                onRemoveClick = { }
            )
        }
    } else {
        if (autocompletedCompanies.isEmpty()) {
            EmptyView(searchingContent = Searched)
        } else {
            SearchedCompanyList(
                searchUIState = searchUIState,
                searchedCompanies = autocompletedCompanies,
                onClickSearchedCompanyItem = { }
            )
        }
    }
}


enum class SearchingContent { Recent, Searched }
@Composable
private fun EmptyView(searchingContent: SearchingContent) {
    val textRes: String = if (searchingContent == Recent) "최근 검색한 기록이 없습니다." else "검색 결과를 찾을 수 없습니다."
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(95.dp))
        when (searchingContent) {
            SearchingContent.Recent -> InfoIcon(48.dp, 48.dp)
            SearchingContent.Searched -> SearchLineIcon(48.dp, 48.dp, tint = CS.Gray.G30)
        }
        Spacer(Modifier.height(12.dp))
        Text(text = textRes, color = CS.Gray.G50, style = Typography.body1Regular)
    }
}

@Composable
private fun RecentViewedCompanyList(
    recentCompanies: List<RecentCompany>,
    onRemoveClick: (RecentCompany) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(recentCompanies) { recentCompany ->
            RecentCompanyItem(
                company = recentCompany,
                onRemoveClick = { onRemoveClick(recentCompany) }
            )
        }
    }
}

@Composable
fun RecentCompanyItem(
    company: RecentCompany,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // ← 나머지 공간 모두 사용
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClockIcon(width = 20.dp, height = 20.dp, tint = CS.Gray.G50)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = company.companyName,
                    style = Typography.body1Bold,
                    color = CS.Gray.G90
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = company.companyAddress,
                style = Typography.body2Regular,
                color = CS.Gray.G50
            )
        }

        CloseLine(
            width = 20.dp,
            height = 20.dp,
            tint = CS.Gray.G50,
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable { onRemoveClick() }
        )
    }
}

@Composable
private fun SearchedCompanyList(
    searchUIState: SearchUIState,
    searchedCompanies: List<SearchedCompany>,
    onClickSearchedCompanyItem: (SearchedCompany) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(searchedCompanies) { company ->
            SearchedCompanyItem(
                searchUIState = searchUIState,
                company = company,
                onClickSearchedCompanyItem = { onClickSearchedCompanyItem(company) }
            )
        }
    }
}

@Composable
private fun SearchedCompanyItem(
    searchUIState: SearchUIState,
    company: SearchedCompany,
    onClickSearchedCompanyItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SearchLineIcon(width = 20.dp, height = 20.dp, tint = CS.Gray.G50)
                HighlightedCompanyName(companyName = company.companyName, keyword = searchUIState.searchBarValue.text)
                StarFilled(20.dp, 20.dp)
                Text(text = company.totalRating.toString(), style = Typography.body1Bold, color = CS.Gray.G90)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = company.companyAddress, style = Typography.body2Regular, color = CS.Gray.G50)
        }
    }
}

@Composable
private fun HighlightedCompanyName(
    companyName: String,
    keyword: String
) {
    // 키워드가 비어있으면 기본 텍스트 출력
    if (keyword.isBlank()) {
        Text(text = companyName, style = Typography.body1Bold, color = CS.Gray.G90)
        return
    }

    // 조합 중인 한글 처리된 매칭용 키워드 생성
    val matchKeyword = getMatchableKeyword(keyword)

    val annotatedString = buildAnnotatedString {
        if (matchKeyword.isNotBlank()) {
            val startIndex = companyName.indexOf(matchKeyword, ignoreCase = true)
            if (startIndex != -1) {
                val endIndex = startIndex + matchKeyword.length

                // 매칭된 부분 앞
                append(companyName.substring(0, startIndex))

                // 하이라이트된 매칭 부분
                withStyle(style = SpanStyle(color = CS.PrimaryOrange.O40)) {
                    append(companyName.substring(startIndex, endIndex))
                }

                // 매칭된 부분 뒤
                append(companyName.substring(endIndex))
                return@buildAnnotatedString
            }
        }

        // 매칭되지 않으면 원본 텍스트 그대로
        append(companyName)
    }

    Text(text = annotatedString, style = Typography.body1Bold, color = CS.Gray.G90)
}

private fun getMatchableKeyword(keyword: String): String {
    return if (keyword.isNotEmpty() && isIncompleteHangul(keyword.last())) {
        // 마지막 문자가 조합 중이면 제거
        keyword.dropLast(1)
    } else {
        keyword
    }
}

private fun isIncompleteHangul(char: Char): Boolean {
    return when (char.code) {
        // 완성된 한글 (가-힣)
        in 0xAC00..0xD7A3 -> {
            Log.d("IncompleteHangul", "완성된 한글: '${char}' (${char.code})")
            false
        }
        // 한글 관련 유니코드 범위 (조합 중인 문자들)
        in 0x1100..0x318F -> {
            Log.d("IncompleteHangul", "조합 중인 한글: '${char}' (${char.code})")
            true
        }
        // 그 외 문자 (영어, 숫자, 특수문자 등)
        else -> {
            Log.d("IncompleteHangul", "기타 문자: '${char}' (${char.code})")
            false
        }
    }
}