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
@file:Suppress("unused")

package com.nopol10.vvalidator.field.seeker

import android.widget.AbsSeekBar
import androidx.annotation.CheckResult
import com.nopol10.vvalidator.ValidationContainer
import com.nopol10.vvalidator.assertion.CustomViewAssertion
import com.nopol10.vvalidator.assertion.seeker.SeekBarAssertions.ProgressAssertion
import com.nopol10.vvalidator.field.FieldValue
import com.nopol10.vvalidator.field.FormField
import com.nopol10.vvalidator.field.IntFieldValue
import com.nopol10.vvalidator.util.onProgressChanged

/**
 * Represents an AbsSeekBar field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class SeekField(
  container: ValidationContainer,
  view: AbsSeekBar,
  name: String?
) : FormField<SeekField, AbsSeekBar, Int>(container, view, name) {

  /** Asserts on the seeker's progress. */
  @CheckResult
  fun progress() = assert(ProgressAssertion())

  /** Adds a custom inline assertion for the seek bar. */
  @CheckResult
  fun assert(
    description: String,
    matcher: (AbsSeekBar) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))

  /** Returns a snapshot of [AbsSeekBar.getProgress]. **/
  @CheckResult
  override fun obtainValue(
    id: Int,
    name: String
  ): FieldValue<Int>? {
    return IntFieldValue(
        id = id,
        name = name,
        value = view.progress
    )
  }

  override fun startRealTimeValidation(debounce: Int) =
    view.onProgressChanged(debounce) { validate() }
}
