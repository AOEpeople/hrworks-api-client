package com.aoe.hrworks

import okio.Buffer
import okio.ByteString
import java.nio.charset.Charset


fun String.sha256hex(): String = Buffer()
        .writeString(this, Charset.forName(HrWorksClientBuilder.ENCODING))
        .sha256().hex()

fun String.hmacSHA256hex(key: ByteArray): ByteArray = Buffer()
        .writeString(this, Charset.forName(HrWorksClientBuilder.ENCODING))
        .hmacSha256(ByteString.of(key, 0, key.size)).toByteArray()

fun ByteArray.hexString(): String = ByteString.of(this,0,this.size).hex()

