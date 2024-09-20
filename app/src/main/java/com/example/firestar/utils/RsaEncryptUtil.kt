package com.example.firestar.utils

import android.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

//RSA加密和Base64编码工具类
object RsaEncryptUtil {

    fun rsaEncryptPassword(password: String, publicKey: String, hash: String): String {
        try {
            val rsaKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
            val keyBytes = Base64.decode(rsaKey, Base64.DEFAULT)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(keySpec)

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            // 拼接 hash 和密码
            val encryptedData = cipher.doFinal((hash + password).toByteArray())

            // 返回 Base64 编码后的加密结果
            return Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error encrypting password")
        }
    }

}