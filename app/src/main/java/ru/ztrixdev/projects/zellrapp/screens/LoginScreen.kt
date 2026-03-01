package ru.ztrixdev.projects.zellrapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ru.ztrixdev.projects.zellrapp.PrimaryZellrButton
import ru.ztrixdev.projects.zellrapp.R
import ru.ztrixdev.projects.zellrapp.SecondaryZellrButton
import ru.ztrixdev.projects.zellrapp.Utils
import ru.ztrixdev.projects.zellrapp.commons.ZellrTextField
import ru.ztrixdev.projects.zellrapp.stateManagers.LoginStateManager

@Composable
fun LoginScreen(stateManager: LoginStateManager = koinViewModel()) {
    var screenState by remember { mutableStateOf(LoginScreenState.INITIAL) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = screenState != LoginScreenState.INITIAL) {
        screenState = LoginScreenState.INITIAL
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                WelcomeHeader(isInitial = screenState == LoginScreenState.INITIAL)

                Spacer(modifier = Modifier.height(48.dp))


                var loginToContinue by remember { mutableStateOf(false) }
                AnimatedContent(
                    targetState = screenState,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { it / 2 }))
                            .togetherWith(fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { it / 2 }))
                    },
                    label = "LoginScreenStateTransition"
                ) { state ->
                    when (state) {
                        LoginScreenState.INITIAL -> {
                            InitialButtons(
                                onLoginClick = { screenState = LoginScreenState.LOGIN },
                                onSignUpClick = { screenState = LoginScreenState.SIGNUP }
                            )
                        }
                        LoginScreenState.LOGIN -> {
                            LoginForm(loginToContinue, onLoginClick = { email, password ->
                                coroutineScope.launch {
                                    val res = stateManager.onLoginClick(email, password)
                                    (res)
                                }
                            })
                        }
                        LoginScreenState.SIGNUP -> {
                            SignUpForm(onSignUpClick = { displayName, email, password ->
                                coroutineScope.launch {
                                    val res = stateManager.onSignUpClick(displayName, email, password)
                                    if (res.first) {
                                        screenState = LoginScreenState.LOGIN
                                        loginToContinue = true
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

enum class LoginScreenState {
    INITIAL, LOGIN, SIGNUP
}


@Composable
private fun SignUpForm(onSignUpClick: (String, String, String) -> Unit) {
    var displayName by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var passwordValid by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )
        ZellrTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = stringResource(R.string.display_name),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
        ZellrTextField(
            value = email,
            onValueChange = { email = it; emailValid = Utils.validateEmail(it) },
            label = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = (!emailValid && email.isNotBlank()),
            errorText = stringResource(R.string.email_invalid)
        )
        ZellrTextField(
            value = password,
            onValueChange = { password = it; passwordValid = Utils.validatePassword(it)},
            label = stringResource(R.string.password),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = { IconButton(
                onClick = { passwordVisibility = !passwordVisibility }
            ) {
                if (passwordVisibility) Icon(imageVector = Icons.Filled.VisibilityOff, contentDescription = null)
                else Icon(imageVector = Icons.Filled.Visibility, contentDescription = null)
            } },
            isError = (!passwordValid && password.isNotBlank()),
            errorText = stringResource(R.string.password_reqs),
            height = if (!passwordValid && password.isNotBlank()) 114 else 81
        )
        ZellrTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(R.string.confirmPassword),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        PrimaryZellrButton(
            text = stringResource(R.string.signup),
            onClick = { onSignUpClick(displayName, email, password) },
            enabled = (email.isNotBlank()
                    && password.isNotBlank()
                    && (password == confirmPassword)
                    && (passwordValid && emailValid)
                    && displayName.isNotBlank())
        )
    }
}

@Composable
private fun LoginForm(loginToContinue: Boolean = false, onLoginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.login_headline),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (loginToContinue) {
            Text(
                text = stringResource(R.string.login_to_continue),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        ZellrTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        ZellrTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        PrimaryZellrButton(
            text = stringResource(R.string.login_headline),
            onClick = { onLoginClick(email, password) },
            enabled = email.isNotBlank() && password.isNotBlank()
        )
    }
}

@Composable
private fun InitialButtons(onLoginClick: () -> Unit, onSignUpClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PrimaryZellrButton(text = stringResource(R.string.signup), onClick = onSignUpClick)
        SecondaryZellrButton(text = stringResource(R.string.login), onClick = onLoginClick)
    }
}


@Composable
private fun WelcomeHeader(isInitial: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        horizontalAlignment = if (isInitial) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.welcome_to_zellr),
            style = if (isInitial) MaterialTheme.typography.displayMedium else MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (isInitial) {
            Text(
                text = stringResource(R.string.welcome_to_zellr_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
