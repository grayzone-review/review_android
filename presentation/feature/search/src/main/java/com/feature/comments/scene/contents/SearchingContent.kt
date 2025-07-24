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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import colors.CS
import com.domain.entity.CompactCompany
import com.domain.entity.Company
import com.example.presentation.designsystem.typography.Typography
import com.feature.comments.scene.SearchUIState
import com.feature.comments.scene.contents.SearchingContent.Recent
import com.feature.comments.scene.contents.SearchingContent.Searched
import com.feature.comments.scene.contents.SearchingContentViewModel.Action.DidTapRemoveRecentCompanyButton
import com.feature.comments.scene.contents.SearchingContentViewModel.Action.DidTapSearchedCompanyItem
import com.feature.comments.scene.contents.SearchingContentViewModel.Action.DidUpdateSearchBarValue
import com.feature.comments.scene.contents.SearchingContentViewModel.Action.LoadRecentCompanies
import preset_ui.icons.ClockIcon
import preset_ui.icons.CloseLine
import preset_ui.icons.InfoIcon
import preset_ui.icons.SearchLineIcon
import preset_ui.icons.StarFilled

@Composable
fun SearchingContent(
    viewModel: SearchingContentViewModel = hiltViewModel(),
    searchUIState: SearchUIState,
    onRecentCompanyClick: (Company) -> Unit,
    onSearchedCompanyClick: (CompactCompany) -> Unit
) {
    val autocompletedCompanies by viewModel.autocompletedCompanies.collectAsState()
    val recentCompanies by viewModel.recentCompany.collectAsState()
    val currentQuery = searchUIState.searchBarValue

    LaunchedEffect(currentQuery) {
        viewModel.handleAction(DidUpdateSearchBarValue, text = currentQuery.text)
        viewModel.handleAction(LoadRecentCompanies)
    }

    if (searchUIState.searchBarValue.text.isEmpty()) {
        if (recentCompanies.isEmpty()) {
            EmptyView(searchingContent = Recent)
        } else {
            RecentViewedCompanyList(
                recentCompanies = recentCompanies,
                onRecentCompanyClick = { onRecentCompanyClick(it) },
                onRemoveClick = { viewModel.handleAction(DidTapRemoveRecentCompanyButton, recentCompany = it) }
            )
        }
    } else {
        if (autocompletedCompanies.isEmpty()) {
            EmptyView(searchingContent = Searched)
        } else {
            SearchedCompanyList(
                searchUIState = searchUIState,
                searchedCompanies = autocompletedCompanies,
                onSearchedCompanyClick = {
                    viewModel.handleAction(DidTapSearchedCompanyItem, compactCompany = it)
                    onSearchedCompanyClick(it)
                }
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
            SearchingContent.Recent -> InfoIcon(48.dp, 48.dp, CS.Gray.G30)
            SearchingContent.Searched -> SearchLineIcon(48.dp, 48.dp, tint = CS.Gray.G30)
        }
        Spacer(Modifier.height(12.dp))
        Text(text = textRes, color = CS.Gray.G50, style = Typography.body1Regular)
    }
}

@Composable
private fun RecentViewedCompanyList(
    recentCompanies: List<Company>,
    onRecentCompanyClick: (Company) -> Unit,
    onRemoveClick: (Company) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(recentCompanies) { recentCompany ->
            RecentCompanyItem(
                company = recentCompany,
                onRecentCompanyClick = { onRecentCompanyClick(recentCompany) },
                onRemoveClick = { onRemoveClick(recentCompany) }
            )
        }
    }
}

@Composable
fun RecentCompanyItem(
    company: Company,
    onRecentCompanyClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val companyAddress = company.siteFullAddress
        ?.takeIf { it.isNotEmpty() }
        ?: company.roadNameAddress?.takeIf { it.isNotEmpty() }
        ?: ""

    val companyName = company.companyName
        ?.takeIf { it.isNotEmpty() }
        ?: ""

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRecentCompanyClick() }
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val yTop = 0f
                val yBottom = size.height

                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, yTop),
                    end = Offset(size.width, yTop),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, yBottom),
                    end = Offset(size.width, yBottom),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClockIcon(width = 20.dp, height = 20.dp, tint = CS.Gray.G50)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = companyName, style = Typography.body1Bold, color = CS.Gray.G90)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = companyAddress, style = Typography.body2Regular, color = CS.Gray.G50)
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
    searchedCompanies: List<CompactCompany>,
    onSearchedCompanyClick: (CompactCompany) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(searchedCompanies) { searchedCompany ->
            SearchedCompanyItem(
                searchUIState = searchUIState,
                compactCompany = searchedCompany,
                onSearchedCompanyClick = { onSearchedCompanyClick(searchedCompany) }
            )
        }
    }
}

@Composable
private fun SearchedCompanyItem(
    searchUIState: SearchUIState,
    compactCompany: CompactCompany,
    onSearchedCompanyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchedCompanyClick() }
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val yTop = 0f
                val yBottom = size.height

                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, yTop),
                    end = Offset(size.width, yTop),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = CS.Gray.G20,
                    start = Offset(0f, yBottom),
                    end = Offset(size.width, yBottom),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SearchLineIcon(width = 20.dp, height = 20.dp, tint = CS.Gray.G50)
                HighlightedCompanyName(companyName = compactCompany.companyName, keyword = searchUIState.searchBarValue.text, modifier = Modifier.weight(1f, fill = false))
                Row(
                    modifier = Modifier
                        .widthIn(min = 49.dp)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StarFilled(20.dp, 20.dp)
                    Text(text = "${compactCompany.totalRating}", style = Typography.body1Bold, color = CS.Gray.G90, maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = compactCompany.companyAddress, style = Typography.body2Regular, color = CS.Gray.G50)
        }
    }
}

@Composable
private fun HighlightedCompanyName(
    companyName: String,
    keyword: String,
    modifier: Modifier = Modifier
) {
    // 키워드가 비어있으면 기본 텍스트 출력
    if (keyword.isBlank()) {
        Text(text = companyName, style = Typography.body1Bold, color = CS.Gray.G90, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = modifier)
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

    Text(text = annotatedString, style = Typography.body1Bold, color = CS.Gray.G90, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = modifier,)
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