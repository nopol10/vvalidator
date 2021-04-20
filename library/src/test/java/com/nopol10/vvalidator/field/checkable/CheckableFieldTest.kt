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
package com.nopol10.vvalidator.field.checkable

import android.widget.CompoundButton
import com.nopol10.vvalidator.assertion.CustomViewAssertion
import com.nopol10.vvalidator.assertion.checkable.CompoundButtonAssertions.CheckedStateAssertion
import com.nopol10.vvalidator.field.FieldError
import com.nopol10.vvalidator.form
import com.nopol10.vvalidator.form.Form
import com.nopol10.vvalidator.testutil.ID_CHECKABLE
import com.nopol10.vvalidator.testutil.NoManifestTestRunner
import com.nopol10.vvalidator.testutil.TestActivity
import com.nopol10.vvalidator.testutil.assertEmpty
import com.nopol10.vvalidator.testutil.assertEqualTo
import com.nopol10.vvalidator.testutil.assertFalse
import com.nopol10.vvalidator.testutil.assertNull
import com.nopol10.vvalidator.testutil.assertSize
import com.nopol10.vvalidator.testutil.assertTrue
import com.nopol10.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class CheckableFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: CheckableField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    form = activity.get()
        .form {
          checkable(ID_CHECKABLE, name = "Checkable") {}
        }
    field = form.getFields()
        .single()
        .assertType()
    field.view.assertEqualTo(activity.get().checkable)
  }

  @Test fun isChecked() {
    val assertion = field.isChecked()
        .assertType<CheckedStateAssertion>()
    assertion.checked.assertTrue()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun isNotChecked() {
    val assertion = field.isNotChecked()
        .assertType<CheckedStateAssertion>()
    assertion.checked.assertFalse()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun assert_custom() {
    val assertion = field.assert("test") { true }
        .assertType<CustomViewAssertion<CompoundButton>>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun `real time validation off`() {
    field.isChecked()

    field.view.isChecked = true
    field.view.error.assertNull()

    field.view.isChecked = false
    field.view.error.assertNull()
  }

  @Test fun `real time validation on`() {
    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.startRealTimeValidation(0)
    field.isChecked()

    field.view.isChecked = true
    errorsList.assertEmpty()

    field.view.isChecked = false
    errorsList.assertSize(1)
    errorsList.single()
        .description.assertEqualTo("should be checked")
  }
}
