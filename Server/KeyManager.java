
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class KeyManager {

    private static PrivateKey privateKey;
    private static Signature signature;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            char[] password = "biddingCoursework".toCharArray();
            keyStore.load(new FileInputStream("keys\\sender_keystore.jks"), password);
            privateKey = (PrivateKey) keyStore.getKey("senderKeyPair", password);
            signature = Signature.getInstance("SHA256withRSA");
        } catch (Exception e) {
            System.out.println("There was a problem while retrieving the private key.");
        }
    }

    public static byte[] signFile(byte[] dataToSign) throws Exception {
        signature.initSign(privateKey);
        signature.update(dataToSign, 0, dataToSign.length);
        byte[] realSig = signature.sign();
        return realSig;
    }

    public static Certificate exportCertificate() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);

        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(new File("keys\\sender_certificate.cer")));

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = null;
        while (bis.available() > 0) {
            cert = cf.generateCertificate(bis);
            trustStore.setCertificateEntry("receiverKeyPair" + bis.available(), cert);
        }
        return cert;
    }

}