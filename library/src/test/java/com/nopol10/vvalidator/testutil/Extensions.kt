/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nopol10.vvalidator.testutil

import android.widget.EditText
import org.robolectric.Shadows

fun EditText.triggerTextChanged(newText: CharSequence) {
  Shadows.shadowOf(this)
      .watchers.forEach { it.onTextChanged(newText, 0, text.length, newText.length) }
  setText(newText)
}
