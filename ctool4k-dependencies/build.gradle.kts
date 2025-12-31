
plugins {
    `java-platform`
}

// 声明版本约束（对应Maven BOM的dependencyManagement）
javaPlatform {
    allowDependencies() // 允许声明依赖约束（默认仅约束，开启后可兼容Maven）
}
