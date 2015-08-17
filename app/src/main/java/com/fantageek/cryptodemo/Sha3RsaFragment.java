package com.fantageek.cryptodemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.kocakosm.pitaya.security.Digest;
import org.kocakosm.pitaya.security.Digests;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by khanh on 8/15/15.
 */
public class Sha3RsaFragment extends Fragment {

    private TextView txtMessage;
    private TextView txtOutput;
    private TextView txtSha3Digest;
    private KeyPair keyPair;
    private View btnComputeSHA3;
    private View btnEncryptDigest;
    private byte[] digest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sha3_rsa_layout, container, false);

        txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        txtOutput = (TextView) view.findViewById(R.id.txtOutput);
        txtSha3Digest = (TextView) view.findViewById(R.id.txtSha3Digest);
        btnComputeSHA3 = view.findViewById(R.id.btnComputeSHA3);
        btnEncryptDigest = view.findViewById(R.id.btnEncryptDigest);

        view.findViewById(R.id.btnGenKeyPair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
                    btnComputeSHA3.setEnabled(true);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.btnComputeSHA3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String input = txtMessage.getText().toString();
                    Digest digestKeccak = Digests.keccak256();
                    digest = digestKeccak.digest(input.getBytes("UTF-8"));
                    txtSha3Digest.setText(new String(Base64.encode(digest, Base64.DEFAULT), "UTF-8"));
                    btnEncryptDigest.setEnabled(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.btnEncryptDigest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
                    byte[] outBytes = cipher.doFinal(digest);
                    txtOutput.setText(new String(Base64.encode(outBytes, Base64.DEFAULT), "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

}
