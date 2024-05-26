package com.dicoding.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.custom.MyButton
import com.dicoding.storyapp.view.custom.PasswordEditText
import com.dicoding.storyapp.view.main.MainActivity
import com.dicoding.storyapp.data.response.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var customBtn: MyButton
    private lateinit var passCustomET: PasswordEditText

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customBtn = binding.loginButton
        passCustomET = binding.edLoginPassword

        val helperError = binding.passwordEditTextLayout

        passCustomET.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 7) {
                    enableButton()
                    helperError.isHelperTextEnabled = false
                    helperError.isErrorEnabled = false
                    helperError.isPasswordVisibilityToggleEnabled  = true
                } else {
                    customBtn.isEnabled = false
                    helperError.helperText = "Password tidak boleh kurang dari 8 karakter"
                    helperError.isErrorEnabled = true
                    helperError.isPasswordVisibilityToggleEnabled  = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        setupView()
        playAnimation()
        login()
    }

    private fun login() {
        with(binding) {
            loginButton.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                viewModel.login(email, password).observe(this@LoginActivity) {
                    when (it) {
                        is Result.Loading -> {
                            loading(true)
                        }

                        is Result.Error -> {
                            loading(false)
                            setupFail(it.error)
                            Log.e("loginError", it.error)
                        }

                        is Result.Success -> {
                            loading(false)
                            it.data.loginResult.token.let { it1 ->
                                setupAction(
                                    it.data.message,
                                    it1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(message: String, token: String) {

        val email = binding.edLoginEmail.text.toString()
        viewModel.saveSession(UserModel(email, token))

        AlertDialog.Builder(this).apply {
            setTitle("Berhasil!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun setupFail(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Gagal!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailTe = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passTe = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, message, emailTv, emailTe, passTv, passTe, button)
            start()
        }
    }

    private fun enableButton() {
        val result = passCustomET.text
        customBtn.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun loading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}