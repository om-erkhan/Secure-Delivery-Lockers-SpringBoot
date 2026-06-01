import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.ClientConfiguration;
public class TestS3Builder {
    public static void main(String[] args) {
        ClientConfiguration config = new ClientConfiguration();
        config.setSignerOverride("AWSS3V4SignerType");
        AmazonS3ClientBuilder.standard()
            .withClientConfiguration(config)
            .withPayloadSigningEnabled(false)
            .build();
    }
}
