package gr.aueb.domain;

import jakarta.enterprise.context.ApplicationScoped;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class Dice {

    private int diceResult;
    private String rServer;
    private String rClient;
    private String serverHash;


    public void setRServer(String rServer) {
        this.rServer = rServer;
    }

    public void setRClient(String rClient) {
        this.rClient = rClient;
    }

    public String getRServer() {
        return rServer;
    }

    public String getRClient() {
        return rClient;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public String getServerHash() {
        return serverHash;
    }

    public void rollDice() {
        this.diceResult = 1 + (int)(Math.random() * ((6 - 1) + 1));
    }

    public void createRandomString() {
        this.rServer = RandomString.make(10);
    }

    public void createHash() throws NoSuchAlgorithmException {
        this.serverHash = DigestUtils.sha256Hex(diceResult + rServer + rClient );
    }

}
