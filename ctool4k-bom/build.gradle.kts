
// 声明版本约束（对应Maven BOM的 dependencyManagement ）
javaPlatform {
    allowDependencies() // 允许声明依赖约束（默认仅约束，开启后可兼容Maven）
}

dependencies {

    constraints {

        rootProject.subprojects.forEach {
            if(project.name != it.name) {
                api(it)
            }
        }

    }

}
