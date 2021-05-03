/*
 * Copyright 2021-Present Okta, Inc.
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
package com.okta.idx.android.directauth.sdk.forms

import com.okta.idx.android.directauth.sdk.Form
import com.okta.idx.android.directauth.sdk.FormAction
import com.okta.idx.android.directauth.sdk.models.AuthenticatorType
import com.okta.idx.sdk.api.model.IDXClientContext

class SelectAuthenticatorForm internal constructor(
    val viewModel: ViewModel,
    private val formAction: FormAction,
) : Form {
    class ViewModel internal constructor(
        val options: List<AuthenticatorType>,
        val canSkip: Boolean,
        internal val idxClientContext: IDXClientContext,
    )

    fun authenticate(type: AuthenticatorType) {
        formAction.proceed {
            val response = authenticationWrapper.selectAuthenticator(
                viewModel.idxClientContext,
                type.authenticatorTypeText
            )
            handleTerminalTransitions(response)?.let { return@proceed it }

            when (type) {
                AuthenticatorType.EMAIL -> {
                    FormAction.ProceedTransition.FormTransition(
                        AuthenticateVerifyCodeForm(
                            AuthenticateVerifyCodeForm.ViewModel(idxClientContext = response.idxClientContext),
                            formAction
                        )
                    )
                }
                AuthenticatorType.PASSWORD -> {
                    // This should be done automatically in the authenticate SDK wrapper.
                    unsupportedPolicy()
                }
                AuthenticatorType.SMS -> {
                    FormAction.ProceedTransition.FormTransition(
                        AuthenticateVerifyCodeForm(
                            AuthenticateVerifyCodeForm.ViewModel(idxClientContext = response.idxClientContext),
                            formAction
                        )
                    )
                }
                AuthenticatorType.VOICE -> {
                    FormAction.ProceedTransition.FormTransition(
                        AuthenticateVerifyCodeForm(
                            AuthenticateVerifyCodeForm.ViewModel(idxClientContext = response.idxClientContext),
                            formAction
                        )
                    )
                }
            }
        }
    }

    fun skip() {
        formAction.skip(viewModel.idxClientContext)
    }

    fun signOut() {
        formAction.signOut()
    }
}
