import org.gradle.api.JavaVersion

object AppConfig {
    const val applicationId = "com.team.review_android"
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val compileSdk = 35
    const val minSdk = 24
    const val targetSdk = 35
    const val jvmTarget = "11"
    val javaVersion = JavaVersion.VERSION_11

    object NameSpace {
        const val app = "com.data.review_android"
        const val dto = "com.data.dto"
        const val network = "com.data.network"
        const val repository_impl = "com.data.repository_implementation"

        const val entity = "com.domain.entity"
        const val repository_interface = "com.domain.repository_interface"
        const val usecase = "com.domain.usecase"

        const val design_system = "com.presentation.design_system"
        const val comments = "com.feature.comments"
        const val company_detail = "com.presentation.company_detail"
    }
}