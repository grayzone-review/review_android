package preset_ui.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import colors.CS

@Composable
fun ChatLine(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.chat_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun ChatFill(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.chat_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun Chat2Fill(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.chat2_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}


@Composable
fun RockClose(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.rock_close),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun RockOpen(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.rock_open),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun Sendable(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.send_able),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun SendDisable(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.send_disable),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun CloseLine(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.close_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun BackBarButtonIcon(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.arrow_left_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun RightArrowIcon(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.arrow_right_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}


@Composable
fun SearchLineIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    tint: Color
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.search_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun CloseFillIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.close_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun CloseFillTint(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.close_fill_tint),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun AroundIcon(
    isOn: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (isOn) com.presentation.design_system.R.drawable.around_on else com.presentation.design_system.R.drawable.around_off
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun InterestIcon(
    isOn: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (isOn)
        com.presentation.design_system.R.drawable.interest_on
    else
        com.presentation.design_system.R.drawable.interest_off

    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun MytownIcon(
    isOn: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (isOn)
        com.presentation.design_system.R.drawable.mytown_on
    else
        com.presentation.design_system.R.drawable.mytown_off

    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun InfoIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.info_fill),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun ClockIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    tint: Color
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.clock_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun FollowPersonOnIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.follow_person_on),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun FollowAddOffIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.follow_add_off),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun ArrowDownIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    tint: Color
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.arrow_down),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun ReviewCheckLine(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.review_check_line),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun CheckCircleFill(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.check_fill_tint),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun KakaoBubble(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.kakao_bubble),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun SignUpRemove(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.signup_remove),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun MapPinTintable(
    width: Dp,
    height: Dp,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mappin_tintable),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = tint
    )
}

@Composable
fun CheckBoxIcon(
    state: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (state) com.presentation.design_system.R.drawable.checkbox_true else com.presentation.design_system.R.drawable.checkbox_false
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun BottomBarHomeIcon(
    state: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (state) com.presentation.design_system.R.drawable.bottombar_home_on else com.presentation.design_system.R.drawable.bottombar_home_off
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun BottomBarMyIcon(
    state: Boolean,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (state) com.presentation.design_system.R.drawable.bottombar_my_on else com.presentation.design_system.R.drawable.bottombar_my_off
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun BottomBarAddIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.bottombar_add),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun MainMapPinIcon(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.main_mappin),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun LocationBanner(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.location_banner),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun OnboardFirst(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.onboard_first),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun OnboardSecond(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.onboard_second),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun OnboardThird(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.onboard_third),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun MyPageBell(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_bell),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G90
    )
}

@Composable
fun MyPageLogOut(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_logout),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G90
    )
}

@Composable
fun MyPagePen(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_pen),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G90
    )
}

@Composable
fun MyPagePerson(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_person),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G90
    )
}

@Composable
fun MyPageSmile(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_smlie),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = Color.Unspecified
    )
}

@Composable
fun MyPageWithdraw(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(com.presentation.design_system.R.drawable.mypage_withdraw),
        contentDescription = null,
        modifier = modifier.size(width = width, height = height),
        tint = CS.Gray.G90
    )
}