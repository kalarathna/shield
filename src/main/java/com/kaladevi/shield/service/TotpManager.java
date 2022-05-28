package com.kaladevi.shield.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;


@Service
@Slf4j
public class TotpManager {

    private static Base64 base64Codec = new Base64();

    public String generateSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }

    public String getUriForImage(String secret) {
        QrData data = new QrData.Builder()
                .label("Two-factor-auth-test")
                .secret(secret)
                .issuer("exampleTwoFactor")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];

        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
           log.error("unable to generate QrCode");
        }

        String mimeType = generator.getImageMimeType();

        return getDataUriForImageTOTP(imageData, mimeType);
    }

    public static String getDataUriForImageTOTP(byte[] data, String mimeType) {
        String encodedData = new String(base64Codec.encode(data));

        return String.format(encodedData);
    }
    public boolean verifyCode(String code, String secret) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}
