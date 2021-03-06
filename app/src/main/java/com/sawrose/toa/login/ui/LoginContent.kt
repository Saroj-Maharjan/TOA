package com.sawrose.toa.login.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sawrose.toa.R
import com.sawrose.toa.core.ui.components.PrimaryButton
import com.sawrose.toa.core.ui.components.SecondaryButton
import com.sawrose.toa.core.ui.components.TOATextField
import com.sawrose.toa.core.ui.components.VerticalSpacer
import com.sawrose.toa.core.ui.theme.TOATheme
import com.sawrose.toa.login.domain.model.Credentials
import com.sawrose.toa.login.domain.model.Email
import com.sawrose.toa.login.domain.model.Password

private const val APP_LOGO_WIDTH_PERCENTAGE = 0.75F

/**
 * this composable maintain the entire login screen for handling user login
 * @param[loginViewState] the current state of the screen to render.
 * @param[onEmailChanged] a callback invoked when the user enters their email
 * @param[onPasswordChanged] a callback invoked when the user entered the password
 * @param[onLoginClicked] a callback invoked when the user clicks the login button.
 * @param[onSignUpClicked] a callback invoked when the user clicks the sign up button.
 */
@Composable
fun LoginContent(
    loginViewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginInputColumn(
                loginViewState,
                onEmailChanged,
                onPasswordChanged,
                onLoginClicked,
                onSignUpClicked
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun LoginInputColumn(
    viewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.screen_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1F))

        AppLogo()

        Spacer(modifier = Modifier.weight(1F))

        EmailInput(
            text = viewState.credentials.email.value,
            onTextChanged = onEmailChanged,
            errorMessage = (viewState as? LoginViewState.InputError)?.emailInputErrorMessage
        )

        VerticalSpacer(height = 12.dp)

        PasswordInput(
            text = viewState.credentials.password.value,
            onTextChanged = onPasswordChanged,
            errorMessage = (viewState as? LoginViewState.InputError)?.passwordInputErrorMessage
        )

        if (viewState is LoginViewState.SubmissionError) {
            Text(
                text = viewState.errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }

        VerticalSpacer(height = 48.dp)

        LoginButton(
            onClick = onLoginClicked,
            enabled = viewState.buttonEnabled
        )

        VerticalSpacer(height = 12.dp)

        SignUpButton(
            onClick = onSignUpClicked,
            enabled = viewState.buttonEnabled
        )
    }
}

@Composable
private fun SignUpButton(
    onClick: () -> Unit,
    enabled: Boolean,
) {
    SecondaryButton(
        text = stringResource(R.string.sign_up),
        onClick = onClick,
        enabled = enabled
    )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    PrimaryButton(
        text = stringResource(R.string.log_in),
        onClick = onClick,
        enabled = enabled
    )
}

@Composable
private fun PasswordInput(
    text: String,
    onTextChanged: (String) -> Unit,
    errorMessage: String?,
) {
    TOATextField(
        text = text,
        onTextChanged = onTextChanged,
        labelText = stringResource(R.string.password),
        errorMessage = errorMessage,
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
private fun EmailInput(
    text: String,
    onTextChanged: (String) -> Unit,
    errorMessage: String?,
) {
    TOATextField(
        text = text,
        onTextChanged = onTextChanged,
        labelText = stringResource(R.string.email),
        errorMessage = errorMessage
    )
}

@Composable
private fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(id = R.drawable.ic_toa_checkmark),
        contentDescription = stringResource(R.string.app_logo_container),
        modifier = modifier
            .fillMaxWidth(APP_LOGO_WIDTH_PERCENTAGE)
    )
}

@Preview(
    name = "Night Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Day Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginViewStateProvider::class)
    loginViewState: LoginViewState,
) {

    TOATheme {
        LoginContent(
            loginViewState = loginViewState,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onSignUpClicked = {}
        )
    }
}

class LoginViewStateProvider : PreviewParameterProvider<LoginViewState> {
    override val values: Sequence<LoginViewState>
        get() {
            val activeCredentials = Credentials(
                Email("Test@testface.com"),
                Password("PASSWORD")
            )

            return sequenceOf(
                LoginViewState.Initial,
                LoginViewState.Active(activeCredentials),
                LoginViewState.Submitting(activeCredentials),
                LoginViewState.SubmissionError(
                    credentials = activeCredentials,
                    errorMessage = "Something went wrong.",
                ),
                LoginViewState.InputError(
                    credentials = activeCredentials,
                    emailInputErrorMessage = "Please enter an email.",
                    passwordInputErrorMessage = "Please enter a password",
                ),
            )
        }
}
