package com.mdx.config;

import com.mdx.util.RsaUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ：YangYaNan
 * @date ：Created in 14:38 2022/4/11
 * @description ：
 * @version: 1.0
 */
@Component
public class RsaKeyProperties {

    private String pubKeyFile= "id_key_rsa.pub";
    private String priKeyFile= "id_key_rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void createRsaKey() throws Exception {
//        publicKey = RsaUtils.getPublicKey(URLDecoder.decode(this.getClass().getClassLoader().getResource(pubKeyFile).getPath(), "UTF-8"));
//        privateKey = RsaUtils.getPrivateKey(URLDecoder.decode(this.getClass().getClassLoader().getResource(priKeyFile).getPath(), "UTF-8"));

        publicKey = RsaUtils.getPublicKey(new ClassPathResource(pubKeyFile));
        privateKey = RsaUtils.getPrivateKey(new ClassPathResource(priKeyFile));
    }


    public String getPubKeyFile() {
        return pubKeyFile;
    }
    public void setPubKeyFile(String pubKeyFile) {
        this.pubKeyFile = pubKeyFile;
    }

    public String getPriKeyFile() {
        return priKeyFile;
    }

    public void setPriKeyFile(String priKeyFile) {
        this.priKeyFile = priKeyFile;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
