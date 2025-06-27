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
        maven {
            url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
            url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
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
include(":presentation:feature:search")
include(":common")
include(":data:storage")
include(":presentation:feature:main")
include(":presentation:feature:common")
include(":presentation:feature:login")
include(":data:location")
