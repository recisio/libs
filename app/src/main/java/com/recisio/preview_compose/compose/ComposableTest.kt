package com.recisio.preview_compose.compose

import ChildTestComposable
import ChildTestComposablePreviewProvider
import TestComposable
import TestComposablePreviewProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Composable
fun TestCompose(parameter: TestComposable) {
    Column(Modifier.background(color = colorResource(parameter.color))) {
        Text(text = parameter.title)
        Text(text = parameter.description)
        Text(text = parameter.enabled.toString())
        Text(text = parameter.position.toString())
        Icon(painter = painterResource(parameter.drawable), contentDescription = "", modifier = Modifier.size(24.dp))
        ChildTestComposable(parameter.child)
    }
}

@PreviewLightDark
@Composable
fun TestComposablePreview(@PreviewParameter(TestComposablePreviewProvider::class) parameter: TestComposable) {
    TestCompose(parameter)
}

@Composable
fun ChildTestComposable(parameter: ChildTestComposable) {
    Row {
        Text(text = parameter.title)
        Text(text = parameter.description)
    }
}

@PreviewLightDark
@Composable
fun ChildTestComposablePreview(@PreviewParameter(ChildTestComposablePreviewProvider::class) parameter: ChildTestComposable) {
    ChildTestComposable(parameter)
}