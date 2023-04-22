package neu.homework.sunshine.ums;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest extends TestCase {

    @Test
    public void testJWT(){
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        String privateKey = Encoders.BASE64.encode(keyPair.getPrivate().getEncoded());
        String publicKey = Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
        System.out.println(privateKey);
        System.out.println(publicKey);
    }


}
