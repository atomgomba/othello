package com.ekezet.othello.core.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ekezet.othello.core.ui.theme.OthelloTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.KoinConfiguration

@Composable
fun PreviewBase(
    moduleList: List<Module>? = null,
    content: @Composable () -> Unit,
) {
    if (moduleList.isNullOrEmpty()) {
        PreviewBaseInner(content)
    } else {
        val context = LocalContext.current
        KoinApplication(configuration = KoinConfiguration {
            androidContext(context)
            modules(moduleList)
        }) {
            PreviewBaseInner(content)
        }
    }
}

@Composable
private inline fun PreviewBaseInner(
    crossinline content: @Composable () -> Unit,
) {
    OthelloTheme {
        Surface {
            content()
        }
    }
}
