package create_review_dialog.type

enum class CreateReviewPhase {
    First,
    Second,
    Third
}

val CreateReviewPhase.step: Int
    get() = when (this) {
        CreateReviewPhase.First -> 1
        CreateReviewPhase.Second -> 2
        CreateReviewPhase.Third -> 3
    }

fun CreateReviewPhase.next(): CreateReviewPhase? = when (this) {
    CreateReviewPhase.First  -> CreateReviewPhase.Second
    CreateReviewPhase.Second -> CreateReviewPhase.Third
    CreateReviewPhase.Third  -> null
}

fun CreateReviewPhase.prev(): CreateReviewPhase? = when (this) {
    CreateReviewPhase.First  -> null
    CreateReviewPhase.Second -> CreateReviewPhase.First
    CreateReviewPhase.Third  -> CreateReviewPhase.Second
}