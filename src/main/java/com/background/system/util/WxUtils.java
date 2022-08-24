package com.background.system.util;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/24 18:42 PM
 */
public class WxUtils {

    public static final String file = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5WdaTNVbSx4m8\n" +
            "1ajiAfzTrclndSL05/6+hEAu6Tc/z/zKQn0E53wdxc4i7094H+LvaEGWlhXqZ2Qz\n" +
            "dlQVjojiVyteFWKZTeIbybDWHIF3oeqFHUWHe2mfrn/8O8dTm7aprtMay27yfWSg\n" +
            "EBNYWqpi1UFmXGmWfLSv9wqh8oHOAk3WI5aHObVqGkD+kA/DtKCJAeDOjc1Fm2WC\n" +
            "d/jDPoK+norK0qaAwuj8TleHi6ZWXaq/6oU4bEPonl8GcD60irk3IyuFlmCq4OQu\n" +
            "kPStcaaer0dMYLmcXVMa8hqb3Ro59FEPwrdsMPNpNnx1mnmUk0ezWwCdDGgD3RoC\n" +
            "Z5TndVL7AgMBAAECggEAdeG2P5+v3ZR8fHXi2ALtmm8v5bWxCtO1x9NywqMeuV39\n" +
            "Z2HMA196J7FTPhK3ly2odhz3gd9ohtFFABnktmUVl+ZatZaiQ+AtywCGkFZG3SBE\n" +
            "eG66dsU90wn3aiNWzUz+aoK+zo7NvDmV0tsGgq3/KaZ51rE1kjkz7aoPwBntBmiH\n" +
            "rhSTKhDfYGH/fuEDHLfgMy0uXeMkvoLXjqX1+HIJuCcyeeOkUlVavYiFCuITJjs3\n" +
            "l+7py70ASIikmnoF6wY7JVLe+CkyoiXXvXwNSNCxeSnZ3ljxyABVkzl+AqtgiR/T\n" +
            "qE4+angkdoeyArFJFJ1sIYZlvftBzIaYLOnGlkiPSQKBgQD1Y1dtgx8sOeQVXFz8\n" +
            "5C5AeTWPDSRYPCCb3tLWCV/kgwAAnnOhKXgDU6Y6ud3aGRFTVjGg1drlJxVm4JOQ\n" +
            "62NoCkLtokVsNdMXOhfZmSO8V0GS5564v2z+lsXCQdLdUCLetmhi7/iApKEg9j7i\n" +
            "PUJknhAQ9uWP2U7JdBnJrJ/GpwKBgQDBXdVrsc7rn27H7Uykd95L/u+xe4hVr1Ap\n" +
            "m/7HYaQu5D7NYirQQ1CjqjilYMt4FNhx835ylYpmIqBee7jFXTo4hLLdGpksJ+is\n" +
            "cBRvfoW0zwI6QszsQE7Dpns8eQLaqJOpHx9KLlA5Vk4L+eFmVFWFA6yuwGPPZAw2\n" +
            "gUiLrenvjQKBgBCmmAUfU5stpnNonAqw9Q586wx06NVv5wqMmSPa6P3ZgcOI/PGf\n" +
            "nbwkDfCIMiydbNaJz75JJJxOBZ7AeGLqBgnGQHpde3Z3RIab84hZYtph1VpBizyg\n" +
            "CKZR1sNysftAd81C9VxZSjR4d1KEQwoY/AtVymNMTZFwPm9fCGzt4L2bAoGBAK+6\n" +
            "feVUfy8FAmFxF9D0GrLH6b9K+9ia7WgTG5TI+LOgvC5cw3nnYxaJ8Nbiw6bKkvXk\n" +
            "CotJubAeUc3r9DxxACMy/XYQ4RkO39YuOXQl8I8j/etCoAcoXixSVPbdJqnev+rX\n" +
            "nNEGi+O/ukgJ/DzJ8bpjX4Ck2VA1pR+3mF1QXSRVAoGAZn1SSpJ42Y+2ZZZLM9g5\n" +
            "0VqM+2m2CuP/9ovtojHH4EZEpobXfIbMPEu4xjEPZZr7bNTJbxLDxr0hilqESoQi\n" +
            "cXI8b28rT4VYTSEh4nQ0b5O9OvnwD5afMlnDYcS9rDQsqLFUt4zeMRkG4ksE3mDW\n" +
            "IoWTnP5XmhS+yD4Unf85PtI=\n" +
            "-----END PRIVATE KEY-----\n";

    public static PrivateKey getPrivateKey() throws IOException {
        try {
            String privateKey = file.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }
}
