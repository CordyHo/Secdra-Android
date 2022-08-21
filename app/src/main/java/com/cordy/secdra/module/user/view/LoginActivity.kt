@file:Suppress("DEPRECATION")

package com.cordy.secdra.module.user.view

import android.app.ProgressDialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.databinding.ActivityLoginBinding
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.module.user.interfaces.IUserInterface
import com.cordy.secdra.module.user.model.MUserModel
import com.cordy.secdra.utils.ToastUtil

class LoginActivity : BaseActivity<ActivityLoginBinding>(), IUserInterface, View.OnClickListener {

    private lateinit var etPhone: EditText
    private lateinit var etPw: EditText
    private var shouldShowPw = true
    private val mUserModel = MUserModel(this)
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun loginSuccess(msg: String) {  //登录
        // 登录成功继续请求用户信息
        mUserModel.getUserInfo()
    }

    override fun getUserInfoSuccess(jsonBeanUser: JsonBeanUser) {  //得到用户信息成功
        progressDialog.dismiss()
        finish()
    }

    override fun getUserInfoFailure(msg: String) {
        progressDialog.dismiss()
        vBinding.btnLogin.isEnabled = true
        ToastUtil.showToastShort(msg)
    }

    override fun loginFailure(msg: String) {
        progressDialog.dismiss()
        vBinding.btnLogin.isEnabled = true
        ToastUtil.showToastShort(msg)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_login -> {
                if (!etPhone.text.isNullOrBlank() && !etPw.text.isNullOrBlank()) {
                    progressDialog.show()
                    mUserModel.login(etPhone.text.toString(), etPw.text.toString())
                    vBinding.btnLogin.isEnabled = false
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
        etPhone = vBinding.etPhone
        etPhone.requestFocus()
        etPw = vBinding.etPw
        vBinding.btnLogin.setOnClickListener(this)
        vBinding.ivEye.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        progressDialog.run {
            setMessage(getString(R.string.please_wait))
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }
    }
}