/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui

import androidx.ui.engine.geometry.Size
import androidx.ui.engine.window.Window
import androidx.ui.engine.window.WindowPadding

fun Window.updateWindowMetrics(
    devicePixelRatio: Float,
    width: Float,
    height: Float,
    paddingTop: Float,
    paddingRight: Float,
    paddingBottom: Float,
    paddingLeft: Float,
    viewInsetTop: Float,
    viewInsetRight: Float,
    viewInsetBottom: Float,
    viewInsetLeft: Float
) {
    this.devicePixelRatio = devicePixelRatio
    this.physicalSize = Size(width, height)
    this.padding = WindowPadding(
            top = paddingTop,
            right = paddingRight,
            bottom = paddingBottom,
            left = paddingLeft)
    this.viewInsets = WindowPadding(
            top = viewInsetTop,
            right = viewInsetRight,
            bottom = viewInsetBottom,
            left = viewInsetLeft)
    this.onMetricsChanged.offer(Unit)
}