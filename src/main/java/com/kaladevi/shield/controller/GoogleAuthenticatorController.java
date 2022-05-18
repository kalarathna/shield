package com.kaladevi.shield.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kaladevi.shield.model.LoginModel;
import com.kaladevi.shield.repositories.CredentialRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class GoogleAuthenticatorController {

    private final GoogleAuthenticator gAuth;

    private final CredentialRepository credentialRepository;

    @SneakyThrows
    @GetMapping("/generate/{username}")
    public void generate(@PathVariable String username, HttpServletResponse response) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("my-demo", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
    }

    @PostMapping("/validate/key")
    public String validateKey(@RequestBody LoginModel body) {
        gAuth.authorizeUser(body.getUsername(), body.getCode());
        return "Success";
    }

    @GetMapping("/scratches/{username}")
    public List<Integer> getScratches(@PathVariable String username) {
        return getScratchCodes(username);
    }

    private List<Integer> getScratchCodes( String username) {
        List<Integer> i= new ArrayList<>();
        i.add(1);
        return i;
        //return credentialRepository.getUser(username).getScratchCodes();
    }

    @PostMapping("/scratches/")
    public String validateScratch(@RequestBody LoginModel body) {
        List<Integer> scratchCodes = getScratchCodes(body.getUsername());
        scratchCodes.contains(body.getCode());
        scratchCodes.remove(body.getCode());
        return "success";
    }
}


