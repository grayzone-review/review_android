pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/") }
    }
}

rootProject.name = "Review-Android"
include(":app")
include(":presentation:design-system")
include(":domain:entity")
include(":data:network")
include(":data:dto")
include(":data:repository-implementation")
include(":domain:repository-interface")
include(":domain:usecase")
include(":presentation:feature:company-detail")
include(":presentation:feature:comments")
include(":common")
