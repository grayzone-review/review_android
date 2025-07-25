package com.presentation.archive.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.MyArchiveCompany
import com.domain.entity.MyArchiveReview
import com.example.presentation.designsystem.typography.Typography
import com.presentation.archive.scene.ArchiveViewModel.Action.DismissCrateReviewSheet
import com.presentation.archive.scene.ArchiveViewModel.Action.GetCompanyFollowList
import com.presentation.archive.scene.ArchiveViewModel.Action.GetInterestReviews
import com.presentation.archive.scene.ArchiveViewModel.Action.GetMyReviews
import com.presentation.archive.scene.ArchiveViewModel.Action.OnAppear
import com.presentation.archive.scene.ArchiveViewModel.Action.ShowCreateReviewSheet
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import com.team.common.feature_api.utility.Utility
import create_review_dialog.CreateReviewDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import preset_ui.icons.BackBarButtonIcon
import preset_ui.icons.StarFilled
import preset_ui.icons.StarHalf
import preset_ui.icons.StarOutline

@Composable
fun ArchiveScene(
    viewModel: ArchiveViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    CreateReviewSheet(isShow = uiState.shouldShowCreateReviewSheet, onDismiss = { viewModel.handleAction(DismissCrateReviewSheet) })

    Scaffold(
        topBar = { TopAppBar(title = uiState.user.nickname ?: "", onBackButtonClick = { navController.popBackStack() }) },
        containerColor = CS.Gray.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StatsRow(uiState.stats)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(color = CS.Gray.G10))
            ArchiveCollection(
                modifier = Modifier.weight(1f),
                wroteReviews = uiState.myReviews,
                interestReviews = uiState.interestReviews,
                followCompanies = uiState.followCompanies,
                onClickReview = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                    .replace("{companyId}", it.companyId.toString()))
                },
                onClickCompany = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                    .replace("{companyId}", it.id.toString()))
                },
                onTabChange = {
                    when (it) {
                        CollectionTab.REVIEW -> { viewModel.handleAction(GetMyReviews) }
                        CollectionTab.INTEREST -> { viewModel.handleAction(GetInterestReviews)}
                        CollectionTab.BOOKMARK -> { viewModel.handleAction(GetCompanyFollowList) }
                    }
                }
            )
            WriteReviewButton(onClick = { viewModel.handleAction(ShowCreateReviewSheet) })
        }
    }
}

@Composable
private fun TopAppBar(
    title: String,
    onBackButtonClick: () -> Unit
) {
    DefaultTopAppBar(
        title = title,
        leftNavigationIcon = {
            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90, modifier = Modifier
                .clickable { onBackButtonClick() })
        }
    )
}

@Composable
private fun StatsRow(
    stats: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { (label, count) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = count.toString(), style = Typography.h3, color = CS.PrimaryOrange.O40)
                Spacer(Modifier.height(4.dp))
                Text(text = label, style = Typography.body1Bold, color = CS.Gray.G90)
            }
        }
    }
}

enum class CollectionTab(val rawValue: String) { REVIEW("리뷰"), INTEREST("관심 리뷰"), BOOKMARK("즐겨찾기") }
@Composable
fun ArchiveCollection(
    wroteReviews: List<MyArchiveReview>,
    interestReviews: List<MyArchiveReview>,
    followCompanies: List<MyArchiveCompany>,
    onTabChange: (CollectionTab) -> Unit,
    onClickReview: (MyArchiveReview) -> Unit,
    onClickCompany: (MyArchiveCompany) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = CollectionTab.entries
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { tabs.size },
        initialPage = CollectionTab.REVIEW.ordinal
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page -> onTabChange(tabs[page]) }
    }

    Column(
        modifier = modifier
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(4.dp)
                        .padding(horizontal = 10.dp),
                    color = CS.PrimaryOrange.O40
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(page = index) } },
                    modifier = Modifier.background(CS.Gray.White),
                    text = {
                        Text(
                            text = tab.rawValue,
                            style = if (pagerState.currentPage == index)
                                Typography.body1Bold else Typography.body1Regular,
                            color = if (pagerState.currentPage == index)
                                CS.Gray.G90 else CS.Gray.G50
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1,
        ) { page ->
            when (tabs[page]) {
                CollectionTab.REVIEW   -> { ReviewList(reviews = wroteReviews, onClick = onClickReview) }
                CollectionTab.INTEREST -> { ReviewList(reviews = interestReviews, onClick = onClickReview) }
                CollectionTab.BOOKMARK -> { CompanyList(companies = followCompanies, onClick = onClickCompany) }
            }
        }
    }
}

@Composable
private fun ReviewList(
    reviews: List<MyArchiveReview>,
    onClick: (MyArchiveReview) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        items(reviews, key = { it.id }) { item ->
            ReviewCard(review = item, modifier = Modifier
                .clickable { onClick(item) }
            )
        }
    }
}

@Composable
private fun ReviewCard(
    review: MyArchiveReview,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            val cleanTitle = review.title.trim().replace("\n", "")

            Text(
                text = cleanTitle,
                style = Typography.body1Bold,
                color = CS.Gray.G80,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            /* ───── 상호 · 직무 · 날짜 ───── */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = review.companyName, style = Typography.captionRegular, color = CS.Gray.G50)
                Spacer(modifier = Modifier.width(1.dp).height(18.dp).background(CS.Gray.G20))
                Text(text = review.jobRole, style = Typography.captionRegular, color = CS.Gray.G50)
                Spacer(modifier = Modifier.width(1.dp).height(18.dp).background(CS.Gray.G20))
                Text(
                    text = review.createdAt?.substring(0, 7)?.replace("-", ".") + " 작성",
                    style = Typography.captionRegular,
                    color = CS.Gray.G50
                )
            }
            Spacer(Modifier.height(16.dp))
            /* ───── 평점 숫자 + 별 ★ ───── */
            val avgRating = review.totalRating
            val starCounts = Utility.calculateStarCounts(totalScore = avgRating)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =  avgRating.toString(),
                    style = Typography.h3,
                    color = CS.Gray.G90
                )
                Spacer(Modifier.width(8.dp))
                repeat(starCounts.full) {
                    StarFilled(width = 20.dp, height = 20.dp)
                }
                repeat(starCounts.half) {
                    StarHalf(width = 20.dp, height = 20.dp)
                }
                repeat(starCounts.empty) {
                    StarOutline(width = 20.dp, height = 20.dp)
                }
            }
            Spacer(Modifier.height(20.dp))
            /* ───── 좋아요 · 댓글 카운트 ───── */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "좋아요 ${review.likeCount}", style = Typography.captionRegular, color = CS.Gray.G90)
                Text(text = "댓글 ${review.commentCount}", style = Typography.captionRegular, color = CS.Gray.G90)
            }
        }
        Spacer(modifier = Modifier.background(color = CS.Gray.G20).height(1.dp).fillMaxWidth())
    }
}

@Composable
private fun CompanyList(
    companies: List<MyArchiveCompany>,
    onClick: (MyArchiveCompany) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(companies, key = { it.id }) { item ->
            CompanyCard(company = item, modifier = Modifier
                .clickable { onClick(item) }
            )
        }
    }
}

@Composable
private fun CompanyCard(
    company: MyArchiveCompany,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {

        /* ───────── 카드 본문 ───────── */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            /* 1) 회사명 */
            Text(
                text = company.companyName,
                style = Typography.body1Bold,
                color = CS.Gray.G90,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            /* 2) 업종 · 주소 */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 업종이 CompactCompany에 없다면 하드코딩(예: 음식점)
                Text("음식점", style = Typography.captionRegular, color = CS.Gray.G50)

                Spacer(
                    Modifier
                        .width(1.dp)
                        .height(18.dp)
                        .background(CS.Gray.G20)
                )

                Text(
                    text = company.companyAddress,
                    style = Typography.captionRegular,
                    color = CS.Gray.G50,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(16.dp))

            /* 3) 평점(숫자 + 별) */
            val starCounts = Utility.calculateStarCounts(totalScore = company.totalRating)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "%.1f".format(company.totalRating),
                    style = Typography.h3,
                    color = CS.Gray.G90
                )
                Spacer(Modifier.width(8.dp))
                repeat(starCounts.full) { StarFilled(20.dp, 20.dp) }
                repeat(starCounts.half) { StarHalf(20.dp, 20.dp) }
                repeat(starCounts.empty) { StarOutline(20.dp, 20.dp) }
            }

            Spacer(Modifier.height(20.dp))

            /* 4) 한줄평 배지 + 내용 */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(CS.Gray.G10, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("한줄평", style = Typography.captionSemiBold, color = CS.Gray.G90)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = company.reviewTitle ?: "",
                    style = Typography.captionRegular,
                    color = CS.Gray.G90,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.background(color = CS.Gray.G20).height(1.dp).fillMaxWidth())
    }
}

@Composable
private fun WriteReviewButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 5.dp)
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White,
            disabledContainerColor = CS.PrimaryOrange.O20,
            disabledContentColor = CS.Gray.White
        ),
        elevation = null
    ) {
        Text(text = "리뷰 작성하러 가기", style = Typography.body1Bold)
    }
}

@Composable
private fun CreateReviewSheet(isShow: Boolean, onDismiss: () -> Unit) {
    if (isShow) { CreateReviewDialog(onDismiss = onDismiss) }
}