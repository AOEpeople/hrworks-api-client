package com.aoe.hrworks

import org.junit.Test
import java.nio.charset.Charset

class HashTests {

    @Test
    fun testSha256hex() =
            assert("testing".sha256hex()
                    == "cf80cd8aed482d5d1527d7dc72fceff84e6326592848447d2dc0b0e87dfc9a90"
            )

    @Test
    fun testHmacSHA256hex() =
            assert("testing".hmacSHA256hex("key".toByteArray(Charset.forName(HrWorksClientBuilder.ENCODING)))
                    .hexString()
                    == "5eae75eb283c762604f814960857ae54dde21de96fa0d8f0a6ca3def5b095573"
            )
}