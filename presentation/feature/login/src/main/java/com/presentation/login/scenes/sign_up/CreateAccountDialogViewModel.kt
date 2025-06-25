package com.presentation.login.scenes.sign_up

data class CreateAccountUIState(
    val nickname: String,
    val myTown: String,
    val interestTowns: List<String> = emptyList()
)


class CreateAccountDialogViewModel {

}