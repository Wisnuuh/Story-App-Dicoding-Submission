package com.dicoding.storyapp.view.signup

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
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.custom.MyButtonSignup
import com.dicoding.storyapp.view.custom.PasswordEditText
import com.dicoding.storyapp.data.response.Result
import com.dicoding.storyapp.view.login.LoginActivity

@Suppress("DEPRECATION")
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var customBtn: MyButtonSignup
    private lateinit var passCustomET: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customBtn = binding.signupButton
        passCustomET = binding.edRegisterPassword

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
                    helperError.isErrorEnabled = true
                    helperError.isPasswordVisibilityToggleEnabled  = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        setupView()
        playAnimation()
        register()
    }

    private fun register() {
        with(binding) {
            signupButton.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                viewModel.register(name, email, password).observe(this@SignupActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setupAction(result.data.message)
                            Log.d("registerSuccess", result.data.message)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            setupFail(result.error)
                            Log.d("registerFail", result.error)
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

    private fun setupAction(message: String) {
        binding.signupButton.setOnClickListener {

            AlertDialog.Builder(this).apply {
                setTitle("Berhasil!")
                setMessage(message)
                setPositiveButton("Lanjut") { _, _ ->
                    startActivity(Intent(context, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun setupFail(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Gagal")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
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
        val nameTv = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameTe = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailTe = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTv = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordTe = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val button = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, nameTv, nameTe, emailTv, emailTe, passwordTv, passwordTe, button)
            start()
        }
    }

    private fun enableButton() {
        val result = passCustomET.text
        customBtn.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}