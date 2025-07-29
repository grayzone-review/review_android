package com.team.common.feature_api.error

enum class ErrorAction {
    Home,       // 메인
    Login,      // 로그인
    Back,       // 이전 화면
    Display,    // 얼럿만
    None        // 행동 없음
}

fun Int.toErrorAction(): ErrorAction = when (this) {
    2010                                            -> ErrorAction.Home
    3000,3001,3002,3003,3101                        -> ErrorAction.Login
    3004,4001,4002,4101                             -> ErrorAction.Back
    3102,4102,4103,4201,4202,4301,4302, 4400,5000   -> ErrorAction.Display
    2011                                            -> ErrorAction.None
    else                                            -> ErrorAction.Display
}