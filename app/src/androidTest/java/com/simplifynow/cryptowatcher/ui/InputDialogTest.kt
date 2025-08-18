package com.simplifynow.cryptowatcher.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.simplifynow.cryptowatcher.R
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

/**
 * Perform common tests for all dialogs here, as individual implementation are thin wrappers
 */
class InputDialogTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun confirm_invokes_onConfirm_and_onDismiss_withProvidedRegex() {
        val onConfirm = mockk<(String) -> Unit>(relaxed = true)
        val onDismiss = mockk<() -> Unit>(relaxed = true)

        val waxRegex = "^[a-z][a-z1-5.]{1,11}$".toRegex()

        composeRule.setContent {
            InputDialog(
                titleRes = R.string.add_a_wax_wallet,
                tag = "TestInputDialog",
                regex = waxRegex,
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }

        val ctx = composeRule.activity
        composeRule.onNodeWithText(ctx.getString(R.string.type_here))
            .performTextInput("abc1")
        composeRule.onNodeWithText(ctx.getString(R.string.ok)).performClick()

        verify(exactly = 1) { onConfirm("abc1") }
        verify(exactly = 1) { onDismiss() }

        // No error is expected
        composeRule.onNodeWithText(ctx.getString(R.string.invalid_address))
            .assertDoesNotExist()
    }

    @Test
    fun confirm_invokes_onDismiss_only_when_cancelling() {
        val onConfirm = mockk<(String) -> Unit>(relaxed = true)
        val onDismiss = mockk<() -> Unit>(relaxed = true)

        val waxRegex = "^[a-z][a-z1-5.]{1,11}$".toRegex()

        composeRule.setContent {
            InputDialog(
                titleRes = R.string.add_a_wax_wallet,
                tag = "TestInputDialog",
                regex = waxRegex,
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }

        val ctx = composeRule.activity
        composeRule.onNodeWithText(ctx.getString(R.string.type_here))
            .performTextInput("abc1")
        composeRule.onNodeWithText(ctx.getString(R.string.cancel)).performClick()

        verify(exactly = 0) { onConfirm(any()) }
        verify(exactly = 1) { onDismiss() }

        // No error is expected
        composeRule.onNodeWithText(ctx.getString(R.string.invalid_address))
            .assertDoesNotExist()
    }

    @Test
    fun invalidInput_showsError_and_doesNotCall_onConfirm_or_onDismiss() {
        val onConfirm = mockk<(String) -> Unit>(relaxed = true)
        val onDismiss = mockk<() -> Unit>(relaxed = true)

        val waxRegex = "^[a-z][a-z1-5.]{1,11}$".toRegex() // invalid if starts with digit

        composeRule.setContent {
            InputDialog(
                titleRes = R.string.add_a_wax_wallet,
                tag = "TestInputDialog",
                regex = waxRegex,
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }

        val ctx = composeRule.activity
        composeRule.onNodeWithText(ctx.getString(R.string.type_here))
            .performTextInput("1bad") // invalid

        composeRule.onNodeWithText(ctx.getString(R.string.ok)).performClick()

        // Should NOT call either callback on invalid input
        verify(exactly = 0) { onConfirm(any()) }
        verify(exactly = 0) { onDismiss() }

        // assert error text is shown (R.string.invalid_address)
        composeRule.onNodeWithText(ctx.getString(R.string.invalid_address))
            .assertExists()
    }
}