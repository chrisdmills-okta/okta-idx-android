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
package com.okta.idx.android.dynamic

import com.okta.idx.android.network.mock.OktaMockWebServer
import com.okta.idx.android.network.mock.RequestMatchers.path
import com.okta.idx.android.network.mock.mockBodyFromFile
import okhttp3.mockwebserver.SocketPolicy

internal object EnrollProfileMockScenario {
    fun prepare() {
        OktaMockWebServer.dispatcher.clear()

        OktaMockWebServer.dispatcher.enqueue(path("/v1/interact")) { response ->
            response.setBody("""{"interaction_handle": "02X-_R0Y"}""")
        }
        OktaMockWebServer.dispatcher.enqueue(path("/idp/idx/introspect")) { response ->
            response.mockBodyFromFile("enroll_profile/introspect-response.json")
        }
        OktaMockWebServer.dispatcher.enqueue(path("/idp/idx/enroll/new")) { response ->
            response.mockBodyFromFile("enroll_profile/enroll-new-response.json")
        }
        OktaMockWebServer.dispatcher.enqueue(path("/oauth2/v1/token")) { response ->
            response.mockBodyFromFile("enroll_profile/token-response.json")
        }
        OktaMockWebServer.dispatcher.enqueue(path("/idp/idx/cancel")) { response ->
            response.mockBodyFromFile("enroll_profile/cancel-response.json")
        }
        OktaMockWebServer.dispatcher.enqueue(path("idp/idx/identify/select")) { response ->
            response.socketPolicy = SocketPolicy.DISCONNECT_AT_START
        }
    }
}
