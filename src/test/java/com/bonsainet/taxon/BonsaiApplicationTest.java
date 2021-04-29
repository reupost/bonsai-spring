package com.bonsainet.taxon;

import com.bonsainet.bonsai.BonsaiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BonsaiApplication.class)
public class BonsaiApplicationTest {

    @Test
    public void contextLoadsTest() {
    }

    @Test
    public void applicationRunsTest() {
        BonsaiApplication.main(new String[] {});
    }
}
