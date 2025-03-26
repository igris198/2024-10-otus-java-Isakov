import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("idea")
    id("com.google.protobuf")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("io.grpc:grpc-netty:1.55.3")
    implementation("io.grpc:grpc-api:1.56.1")
    implementation("io.grpc:grpc-protobuf:1.54.2")
    implementation("io.grpc:grpc-stub:1.56.1")
    implementation("com.google.protobuf:protobuf-java:3.21.7")
    implementation("com.google.errorprone:error_prone_annotations:2.18.0")
    implementation("org.apache.tomcat:annotations-api:6.0.53")
}

val protoSrcDir = "$projectDir/build/generated"

idea {
    module {
        sourceDirs = sourceDirs.plus(file(protoSrcDir))
    }
}

sourceSets {
    main {
        proto {
            srcDir(protoSrcDir)
        }
    }
}

protobuf {
    generatedFilesBaseDir = protoSrcDir

    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }

    generateProtoTasks {
        ofSourceSet("main")
    }
}

afterEvaluate {
    tasks {
        getByName("generateProto").dependsOn(processResources)
    }
}

protobuf {
    generatedFilesBaseDir = protoSrcDir

    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.56.1"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

afterEvaluate {
    tasks {
        getByName("generateProto").dependsOn(processResources)
    }
}