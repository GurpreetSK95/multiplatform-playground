package com.gurpreetsk.tvdb.shared

import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion

actual object HttpClient {
    actual fun get(): io.ktor.client.HttpClient {
        return io.ktor.client.HttpClient(OkHttp) {
            engine {
                preconfigured?.let {
                    val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                        )
                        .build()

                    OkHttpClient.Builder()
                        .followRedirects(true)
                        .connectionSpecs(listOf(spec))
                        .build()
                }
            }
        }
    }
}