package org.fullstackcoder.demo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationUnitTest {
    @Test
    public void contextLoads() {
    }
    @Test
    public void test()
    {
        Application.main(new String[]{
                "--spring.main.web-environment=false"
        });
    }
}
