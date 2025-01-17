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
package com.nopol10.vvalidator.field.seeker

import android.widget.AbsSeekBar
import com.nopol10.vvalidator.assertion.CustomViewAssertion
import com.nopol10.vvalidator.assertion.seeker.SeekBarAssertions.ProgressAssertion
import com.nopol10.vvalidator.field.FieldError
import com.nopol10.vvalidator.form
import com.nopol10.vvalidator.form.Form
import com.nopol10.vvalidator.testutil.ID_SEEKER
import com.nopol10.vvalidator.testutil.NoManifestTestRunner
import com.nopol10.vvalidator.testutil.TestActivity
import com.nopol10.vvalidator.testutil.assertEmpty
import com.nopol10.vvalidator.testutil.assertEqualTo
import com.nopol10.vvalidator.testutil.assertSize
import com.nopol10.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class SeekFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: SeekField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    form = activity.get()
        .form {
          seeker(ID_SEEKER, name = "Seeker") {}
        }
    field = form.getFields()
        .single()
        .assertType()
    field.view.assertEqualTo(activity.get().seeker)
  }

  @Test fun progress() {
    val assertion = field.progress()
        .assertType<ProgressAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun assert_custom() {
    val assertion = field.assert("test") { true }
        .assertType<CustomViewAssertion<AbsSeekBar>>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun `real time validation off`() {
    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.view.max = 100
    field.progress()
        .atLeast(50)

    field.view.progress = 50
    errorsList.assertEmpty()

    field.view.progress = 49
    errorsList.assertEmpty()
  }

  @Test fun `real time validation on`() {
    field.startRealTimeValidation(0)

    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.view.max = 100
    field.progress()
        .atLeast(50)

    field.view.progress = 50
    errorsList.assertEmpty()

    field.view.progress = 49
    errorsList.assertSize(1)
    errorsList.single()
        .description.assertEqualTo("progress must be at least 50")
  }
}
