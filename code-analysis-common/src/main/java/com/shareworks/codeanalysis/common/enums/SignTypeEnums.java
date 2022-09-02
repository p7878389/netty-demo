package com.shareworks.codeanalysis.common.enums;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import com.shareworks.codeanalysis.common.functional.SignTypeStrategy;
import java.nio.charset.Charset;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 17:37
 */
@AllArgsConstructor
@Getter
public enum SignTypeEnums implements BaseEnums {

    MD5((byte) 0, SignTypeEnums::md5),
    RSA((byte) 1, SignTypeEnums::rsa),
    SHA1((byte) 2, SignTypeEnums::sha1),
    SHA256((byte) 3, SignTypeEnums::sha256),
    MD5_WITH_RSA((byte) 4, SignTypeEnums::md5WithRSA),
    ;

    private final byte type;

    private final SignTypeStrategy<byte[], byte[]> signTypeStrategy;

    @Override
    public String getName() {
        return this.name();
    }

    public static SignTypeEnums get(byte type) {
        for (SignTypeEnums value : SignTypeEnums.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
//        throw new SignTypeException(ExceptionSysConstant.INTERNAL_SERVER_ERROR, "unknown sign type: " + type);
        return null;
    }

    private static byte[] sha(DigestAlgorithm algorithm, String signKey, byte[] data) {
        Digester digester = new Digester(algorithm);
        return digester.digest(data);
    }

    private static byte[] sha1(byte[] data, String signKey) {
        return sha(DigestAlgorithm.SHA1, signKey, data);
    }

    private static byte[] sha256(byte[] data, String signKey) {
        return sha(DigestAlgorithm.SHA256, signKey, data);
    }

    public static byte[] md5(byte[] data, String signKey) {
//        byte[] signKeyByte = new byte[0];
//        if (StringUtils.isNotBlank(signKey)) {
//            signKeyByte = signKey.getBytes(Charset.defaultCharset());
//        }
//        int length = data.length;
//        if (signKeyByte.length > 0) {
//            length += signKeyByte.length;
//        }
//        byte[] signData = new byte[length];
        String md5Hex = DigestUtil.md5Hex(data);
        return HexUtil.decodeHex(md5Hex);
    }

    public static byte[] rsa(byte[] data, String privateKey) {
        RSA rsa = SecureUtil.rsa(privateKey, null);
        return rsa.encrypt(data, KeyType.PrivateKey);
    }

    public static byte[] md5WithRSA(byte[] data, String privateKey) {
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, null);
        String signHex = sign.signHex(data);
        return signHex.getBytes(Charset.defaultCharset());
    }
}
