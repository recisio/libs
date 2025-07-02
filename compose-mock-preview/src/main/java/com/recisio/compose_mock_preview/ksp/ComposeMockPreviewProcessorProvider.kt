package com.recisio.compose_mock_preview.ksp

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.random.Random

const val internalPackage = "com.recisio.compose_mock_preview"

class ComposeMockPreviewProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ComposeMockPreviewProcessor(environment)
    }
}

class ComposeMockPreviewProcessor(env: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = env.codeGenerator
    private val logger = env.logger

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val configAnnotationFqName = "$internalPackage.annotations.ComposeMockPreviewConfiguration"

        val configClassDeclaration = resolver.getSymbolsWithAnnotation(configAnnotationFqName)
            .filterIsInstance<KSClassDeclaration>()
            .firstOrNull()

        val configQualifiedName = configClassDeclaration?.qualifiedName?.asString()

        val symbols = resolver.getSymbolsWithAnnotation("$internalPackage.annotations.ComposeMockPreviewProvider")
            .filterIsInstance<KSClassDeclaration>()

        for (classDeclaration in symbols) {
            val packageName = classDeclaration.packageName.asString()
            val className = classDeclaration.simpleName.asString()
            val fileName = "${className}PreviewProvider"

            val args = generateConstructorWithArguments(resolver, classDeclaration, false)
            val classBuilder = TypeSpec.classBuilder(fileName)
                .addSuperinterface(
                    ClassName("androidx.compose.ui.tooling.preview", "PreviewParameterProvider")
                        .parameterizedBy(ClassName(packageName, className))
                )

            configQualifiedName?.let { configName ->
                classBuilder.primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            ParameterSpec.builder("config", ClassName.bestGuess(configName))
                                .defaultValue("%T()", ClassName.bestGuess(configName))
                                .build()
                        ).build()
                )
                classBuilder.addProperty(
                    PropertySpec.builder(
                        "values",
                        ClassName("kotlin.sequences", "Sequence")
                            .parameterizedBy(ClassName(packageName, className))
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("sequenceOf(%T(%L))", ClassName(packageName, className), args)
                        .build()
                )
            } ?: run {
                classBuilder.addProperty(
                    PropertySpec.builder(
                        "values",
                        ClassName("kotlin.sequences", "Sequence")
                            .parameterizedBy(ClassName(packageName, className))
                    )
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("sequenceOf(%T(%L))", ClassName(packageName, className), args)
                        .build()
                )
            }

            FileSpec.builder(packageName, fileName)
                .addType(classBuilder.build())
                .build()
                .writeTo(codeGenerator, Dependencies(false))

            logger.warn("✅ Generated $fileName for $className")
        }

        return emptyList()
    }
}

private fun generateConstructorWithArguments(resolver: Resolver, classDeclaration: KSClassDeclaration, isChildClass: Boolean): String {
    val constructor = classDeclaration.primaryConstructor ?: return "${classDeclaration.simpleName.asString()}()"

    val args = constructor.parameters.mapNotNull { param ->
        val name = param.name?.asString() ?: return@mapNotNull null
        val type = param.type.resolve().declaration.qualifiedName?.asString() ?: return@mapNotNull null

        val value = when {
            type == "kotlin.String" -> when {
                param.hasAnnotation("$internalPackage.annotations.LongMessagePreview") -> "config.longMessageList.random()"
                param.hasAnnotation("$internalPackage.annotations.ShortMessagePreview") -> "config.shortMessageList.random()"
                else -> "\"$name\""
            }

            type == "kotlin.Int" -> when {
                param.hasAnnotation("androidx.annotation.StringRes") -> "config.resMessageList.random()"
                param.hasAnnotation("androidx.annotation.DrawableRes") -> "config.resDrawableList.random()"
                param.hasAnnotation("androidx.annotation.ColorRes") -> "config.resColorList.random()"
                else -> Random.nextInt()
            }

            resolver.getClassDeclarationByName(param.type.toString())?.classKind == ClassKind.ENUM_CLASS -> {
                val enumClass = resolver.getClassDeclarationByName(param.type.toString())
                val firstConstant = enumClass?.declarations
                    ?.filterIsInstance<KSClassDeclaration>()
                    ?.firstOrNull()
                "${param.type}.${firstConstant?.simpleName?.asString() ?: "UNKNOWN"}"
            }

            type == "kotlin.Boolean" -> Random.nextBoolean()
            resolver.getClassDeclarationByName(type)?.classKind == ClassKind.CLASS -> {
                generateConstructorWithArguments(resolver, resolver.getClassDeclarationByName(type)!!, true)
            }

            else -> "null"
        }

        "$name = $value"
    }

    return if (isChildClass) {
        "${classDeclaration.simpleName.asString()}(${args.joinToString(", ")})"
    } else {
        args.joinToString(", ")
    }
}


fun KSValueParameter.hasAnnotation(fqName: String): Boolean {
    return annotations.any {
        it.annotationType.resolve().declaration.qualifiedName?.asString() == fqName
    }
}
