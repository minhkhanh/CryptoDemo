package com.fantageek.cryptodemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by khanh on 8/15/15.
 */
public class EncryptionDecryptionFragment extends Fragment {

    private TextView txtInput;
    private TextView txtOutput;
    private TextView txtPassword;
    private RadioGroup radioGroupMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encryption_decryption_layout, container, false);

        txtInput = (TextView) view.findViewById(R.id.txtInput);
        txtOutput = (TextView) view.findViewById(R.id.txtOutput);
        txtPassword = (TextView) view.findViewById(R.id.txtPassword);
        radioGroupMode = (RadioGroup) view.findViewById(R.id.radio_mode);

        view.findViewById(R.id.btnEncrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = txtInput.getText().toString();
                String pwd = txtPassword.getText().toString();
                String out = encrypt(input, pwd);
                txtOutput.setText(out);
            }
        });

        view.findViewById(R.id.btnDecrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = txtInput.getText().toString();
                String pwd = txtPassword.getText().toString();
                String out = decrypt(input, pwd);
                txtOutput.setText(out);
            }
        });

        view.findViewById(R.id.btnCopyOutput2Input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput.setText(txtOutput.getText());
                /*String input = txtInput.getText().toString();
                String pwd = txtPassword.getText().toString();
                String out = encrypt(input, pwd);
                out = decrypt(out, pwd);
                txtOutput.setText(out);*/
            }
        });
        return view;
    }


    private byte[] md5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return digest.digest(text.getBytes("UTF-8"));
    }

    private String decrypt(String input, String pwd) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] pwdBytes = md5(pwd);
            SecretKey secretKey = new SecretKeySpec(pwdBytes, "AES");
            IvParameterSpec iv = new IvParameterSpec(pwdBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] inBytes = Base64.decode(input.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] outBytes = cipher.doFinal(inBytes);
            return new String(outBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private String encrypt(String input, String pwd) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] pwdBytes = md5(pwd);
            SecretKey secretKey = new SecretKeySpec(pwdBytes, "AES");
            IvParameterSpec iv = new IvParameterSpec(pwdBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] inBytes = input.getBytes("UTF-8");
            byte[] outBytes = cipher.doFinal(inBytes);
            return new String(Base64.encode(outBytes, Base64.DEFAULT), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

}
