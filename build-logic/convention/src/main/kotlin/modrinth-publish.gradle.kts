import com.modrinth.minotaur.ModrinthExtension
import java.util.Locale
import kotlin.toString

plugins {
    id("com.modrinth.minotaur")
}

modrinth {
    detectLoaders = false
}

val modrinthPublishAll by rootProject.tasks.getting

val versionNameFormat = rootProject.ext["mr_version_name_format"].toString()
val versionChangelog = rootProject.ext["mr_version_changelog"].toString()
val modrinthVersionType = rootProject.ext["mr_version_type"].toString()
check(modrinthVersionType != "stable") { "versionType is 'stable'. Did you mean 'release'?" }
check(modrinthVersionType in listOf("release", "beta", "alpha")) { "Invalid version type: $modrinthVersionType" }
val modAbbreviation: String = rootProject.ext["mr_version_mod_abbr"].toString()
val modVersionDisplay: String = if (rootProject.ext.has("mr_mod_version_display")) {
    rootProject.ext["mr_mod_version_display"].toString()
} else {
    rootProject.version.toString()
}
val modrinthProjectId = rootProject.ext["mr_project_id"].toString()

val branchName = project.name

data class SharedMRInfo(
    val mcVersion: String,
    val supportedVersions: List<String>,
    val displayedMcVersion: String
) {
    fun getVersionTitle(loaderName: String): String = String.format(
        Locale.ENGLISH, versionNameFormat,
        // 1: MC version range
        displayedMcVersion,
        // 2: loader name
        loaderName,
        // 3: mod abbreviation
        modAbbreviation,
        // 4: version displayed
        modVersionDisplay
    )
}

data class SubprojectMRInfo(
    val shared: SharedMRInfo,

//    val primaryFile: RegularFileProperty,
    val jarTask: Provider<AbstractArchiveTask>,
    val sourcesJarTask: Provider<Task>,
    val loaders: List<String>,
    val loaderName: String,
    val versionNumber: String,
) {
    val versionTitle get() = shared.getVersionTitle(loaderName)
}

val uploadConfigs: MutableList<Pair<Project, SubprojectMRInfo>> = mutableListOf()

val sharedMRInfo: SharedMRInfo = run {
    val mcVersion: String = project.ext["minecraft_version"].toString()
    val supportedVersions: List<String> = if (project.ext.has("mr_supported_versions")) {
        project.ext["mr_supported_versions"].toString().split(',')
    } else {
        listOf(mcVersion)
    }
    val displayedMcVersion: String = if (project.ext.has("mr_displayed_mc_version")) {
        project.ext["mr_displayed_mc_version"].toString()
    } else {
        supportedVersions.joinToString("/")
    }
    SharedMRInfo(mcVersion, supportedVersions, displayedMcVersion)
}

private fun Project.loaderInfo(): Pair<List<String>, String> {
    val loaders: List<String> = if (this.ext.has("mr_loaders")) {
        this.ext["mr_loaders"].toString().split(',')
    } else {
        listOf(this.name)
    }
    val loaderName: String = if (this.ext.has("mr_loader_name")) {
        this.ext["mr_loader_name"].toString()
    } else {
        loaders.joinToString("/")
    }
    return loaders to loaderName
}

if (project.plugins.hasPlugin("multiplatform-common")) {
    project.subprojects.forEach { p ->
        if (p.tasks.findByName("remapJar") == null) {
            println("No remapJar found in $p")
            return@forEach
        }
//        apply(plugin="com.modrinth.minotaur")

        val subprojectMRInfo: SubprojectMRInfo = run {
            val (loaders, loaderName) = p.loaderInfo()
            val versionNumber = "${project.version}-${project.name}+$branchName"

            SubprojectMRInfo(
                sharedMRInfo,
                p.tasks.named<AbstractArchiveTask>("remapJar"),
                p.tasks.named("remapSourcesJar"),
                loaders, loaderName, versionNumber,
            )
        }

        uploadConfigs.add(p to subprojectMRInfo)
    }
} else {
    val (loaders, loaderName) = project.loaderInfo()
    val versionNumber = "${project.version}-universal+$branchName"

    val subprojectMRInfo = SubprojectMRInfo(
        sharedMRInfo,
        project.tasks.named<AbstractArchiveTask>("jar"), project.tasks.named("sourcesJar"),
        loaders, loaderName, versionNumber,
    )
    uploadConfigs.add(project to subprojectMRInfo)
}

uploadConfigs.forEach { (p, info) ->
    p.apply(plugin="com.modrinth.minotaur")

    p.extensions.configure<ModrinthExtension>("modrinth") {
        detectLoaders = false

        token = providers.environmentVariable("MR_TOKEN").orElse("invalid")
        projectId = modrinthProjectId
        versionNumber = info.versionNumber
        versionName = info.versionTitle
        changelog = versionChangelog
        file.set(info.jarTask.flatMap { it.archiveFile })
        additionalFiles.set(listOf(info.sourcesJarTask))
        versionType = modrinthVersionType
        gameVersions = info.shared.supportedVersions
        loaders = info.loaders

        autoAddDependsOn = false
        dependencies {
            optional.project("remote-resource-pack")
        }

        debugMode = providers.environmentVariable("MR_DEBUG").orElse("").map(String::isNotBlank)
    }
    modrinthPublishAll.dependsOn(p.tasks.modrinth)

    p.tasks.modrinth {
        dependsOn(info.jarTask, info.sourcesJarTask)
    }
}
