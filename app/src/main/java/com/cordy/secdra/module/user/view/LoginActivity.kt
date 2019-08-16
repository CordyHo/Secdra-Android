@file:Suppress("DEPRECATION")

package com.cordy.secdra.module.user.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.view.MainActivity
import com.cordy.secdra.module.user.interfaces.IUserInterface
import com.cordy.secdra.module.user.model.MUserModel
import com.cordy.secdra.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), IUserInterface, View.OnClickListener {
    private lateinit var etPhone: EditText
    private lateinit var etPw: EditText
    private var shouldShowPw = true
    private val mUserModel = MUserModel(this)
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    override fun loginSuccess(msg: String) {
        progressDialog.dismiss()
        ToastUtil.showToastShort(msg)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun loginFailure(msg: String) {
        progressDialog.dismiss()
        btn_login.isEnabled = true
        ToastUtil.showToastShort(msg)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_login -> {
                if (!etPw.text.isNullOrBlank()) {
                    progressDialog.show()
                    mUserModel.login(etPhone.text.toString(), etPw.text.toString())
                    btn_login.isEnabled = false
                } else if (etPhone.text.isNullOrBlank())
                    ToastUtil.showToastShort(getString(R.string.input_phone))
                else
                    ToastUtil.showToastShort(getString(R.string.input_pw))
            }

            R.id.iv_eye -> {
                if (shouldShowPw) {
                    etPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    etPw.setSelection(etPw.text.toString().length)
                    shouldShowPw = false
                } else {
                    etPw.transformationMethod = PasswordTransformationMethod.getInstance()
                    etPw.setSelection(etPw.text.toString().length)
                    shouldShowPw = true
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        etPhone = et_phone
        etPw = et_pw
        btn_login.setOnClickListener(this)
        iv_eye.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        progressDialog.run {
            setMessage(getString(R.string.please_wait))
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }
    }
}