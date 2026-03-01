package ru.ztrixdev.projects.zellrapp

object Utils {
    fun validateEmail(email: String): Boolean = (
        (email.contains("@")
                && !email.contains(" ")
                && email.length > 8
                && email.contains("."))
                || (email.isBlank()
                || email.length < 8)
        )

    fun validatePassword(password: String): Boolean = (
            (password.length > 8
                    && password.contains(Regex("[A-Z]"))
                    && password.contains(Regex("[a-z]"))
                    && password.contains(Regex("[0-9]")))
            || (password.isBlank())
            )
}