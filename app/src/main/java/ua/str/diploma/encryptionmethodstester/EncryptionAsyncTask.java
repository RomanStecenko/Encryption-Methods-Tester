package ua.str.diploma.encryptionmethodstester;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dd.processbutton.ProcessButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
    public static final int MODE_VERY_SMALL = 0;
    public static final int MODE_SMALL = 1;
    public static final int MODE_MIDDLE = 2;
    public static final int MODE_BIG = 3;
    public static final int MODE_VERY_BIG = 4;

    private static final String FILE_VERY_SMALL = "text_1_mb.txt";
    private static final String FILE_SMALL = "text_3_mb.txt";
    private static final String FILE_MIDDLE = "text_6_mb.txt";
    private static final String FILE_BIG = "text_9_mb.txt";
    private static final String FILE_VERY_BIG = "text_30_mb.txt";

    private static final String PASSWORD = "password";
    private static final String LOG_TAG = "LOG_TAG";
    private static final String[] algorithms = {"AES"/*128,192,256*/, "AES", "AES", "DESede"/*192*/, "Blowfish"/*32-448 (256)*/, "RC4"/*0-2040 (256)*/};
    private static final int keys[] = {128, 192, 256, 192, 256, 256};
    //if need to show result of encryption/decryption use Base64. example:
    //Base64.decode(decryptedText, BASE64_FLAGS)
//    private static final int BASE64_FLAGS = Base64.NO_WRAP;

    private SecureRandom mRandom;
    private IvParameterSpec mIvParams;
    private SecretKey mKey;
    private DbHelper dbHelper;
    private TextView tvProgressDescription;
    private Context context;
    private ProcessButton button;
    private byte[] fileBytes;

    public EncryptionAsyncTask(Context context, ProcessButton button, TextView textView, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.button = button;
        this.tvProgressDescription = textView;
        mRandom = new SecureRandom();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        button.setProgress(10);
        tvProgressDescription.setText("");
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean result = true;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            switch (params[0]) {
                case MODE_VERY_SMALL:
                    inputStream = assetManager.open(FILE_VERY_SMALL);
                    break;
                case MODE_SMALL:
                    inputStream = assetManager.open(FILE_SMALL);
                    break;
                case MODE_MIDDLE:
                    inputStream = assetManager.open(FILE_MIDDLE);
                    break;
                case MODE_BIG:
                    inputStream = assetManager.open(FILE_BIG);
                    break;
                case MODE_VERY_BIG:
                    inputStream = assetManager.open(FILE_VERY_BIG);
                    break;
            }
            if (inputStream != null) {
                fileBytes = new byte[inputStream.available()];
                Log.d(LOG_TAG, "file size: " + fileBytes.length);
                inputStream.read(fileBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < algorithms.length; j++) {
            publishProgress(j);
            try {
                performCompleteProcess(algorithms[j], keys[j]);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        tvProgressDescription.setText(algorithms[values[0]] + " " + keys[values[0]]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            button.setProgress(100);
            button.setEnabled(true);
            tvProgressDescription.setText(R.string.done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, EncryptionSpeedChartActivity.class));
                }
            });
        } else {
            button.setProgress(-1);
            tvProgressDescription.setText("");
        }
    }

    private void performCompleteProcess(String alg, int kLength) throws UnsupportedEncodingException,
            NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
//        Log.d(LOG_TAG, "Before: " + new String(fileBytes, "UTF-8"));
        byte[] encryptedText = encrypt(alg, kLength, fileBytes);
//        Log.d(LOG_TAG, "Encrypted: " /*+ new String(encryptedText, "UTF-8") + ", or base64 encoded variant: "*/ + Base64.encodeToString(encryptedText, BASE64_FLAGS));
//        byte[] decryptedText = decrypt(alg, encryptedText);
//        Log.d(LOG_TAG, "Decrypted: " + new String(decryptedText, "UTF-8") + "\n");
//        boolean resultOfCompare = Arrays.equals(fileBytes, decryptedText);
//        Log.d(LOG_TAG, "resultOfCompare: " + resultOfCompare);
    }

    private byte[] encrypt(String algorithm, int keyLength, byte[] input) throws
            InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException {

        int iterationCount = 10000;
        int saltLength = keyLength / 8;
        byte[] mSalt = new byte[saltLength];
        mRandom.nextBytes(mSalt);
        KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), mSalt, iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        mKey = new SecretKeySpec(keyBytes, algorithm);

        String algorithmWithMode = algorithm;
        if (!algorithm.equals(algorithms[5])) {
            algorithmWithMode = algorithm + "/CBC/PKCS5Padding";
        }
        Cipher cipher = Cipher.getInstance(algorithmWithMode);
        byte[] iv = new byte[cipher.getBlockSize()];
        mRandom.nextBytes(iv);
        mIvParams = new IvParameterSpec(iv);
        if (!algorithm.equals(algorithms[5])) {
            cipher.init(Cipher.ENCRYPT_MODE, mKey, mIvParams);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, mKey);
        }
        long startTime = System.currentTimeMillis();
        byte[] cipherText = cipher.doFinal(input);
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        Log.d(LOG_TAG, "algorithm: " + algorithm + ", key length= " + keyLength);
        Log.d(LOG_TAG, "Encryption time: " + diff);
        Result result = new Result(algorithm + "_" + keyLength, diff, fileBytes.length, System.currentTimeMillis());
        dbHelper.addEncryptionResult(result);
        return cipherText;
    }

    private byte[] decrypt(String algorithm, byte[] encryptedBytes) throws UnsupportedEncodingException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String algorithmWithMode = algorithm;
        if (!algorithm.equals(algorithms[5])) {
            algorithmWithMode = algorithm + "/CBC/PKCS5Padding";
        }
        Cipher cipher = Cipher.getInstance(algorithmWithMode);
        if (!algorithm.equals(algorithms[5])) {
            cipher.init(Cipher.DECRYPT_MODE, mKey, mIvParams);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, mKey);
        }
        long startTime = System.currentTimeMillis();
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        Log.d(LOG_TAG, "Decryption result:" + diff);
        return decryptedBytes;
    }
}
