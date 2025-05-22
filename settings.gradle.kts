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
    }
}

rootProject.name = "Review-Android"
include(":app")
include(":presentation:design_system")
include(":domain:entity")
include(":data:network")
include(":data:dto")
include(":data:repository_implementation")
include(":domain:repository_interface")
include(":domain:usecase")
include(":presentation:feature:company_detail")
include(":presentation:feature:comments")
