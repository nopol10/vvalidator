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
package com.nopol10.vvalidator.assertion

import android.view.View

/** @author Aidan Follestad (@afollestad) */
class CustomViewAssertion<T>(
  assertionDescription: String,
  private val assertion: (T) -> Boolean
) : Assertion<T, CustomViewAssertion<T>>() where T : View {
  init {
    if (assertionDescription.trim().isEmpty()) {
      error("Custom assertion descriptions should not be empty.")
    }
    description(assertionDescription)
  }

  override fun isValid(view: T) = assertion(view)

  override fun defaultDescription() = "no description set"
}
