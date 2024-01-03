import androidx.compose.ui.window.ComposeUIViewController
import com.genaku.reduce.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
