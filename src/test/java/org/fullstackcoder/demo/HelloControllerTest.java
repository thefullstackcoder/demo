package org.fullstackcoder.demo;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HelloControllerTest {
    @Test
    public void testGreeting() {
        HelloController c = new HelloController();
        Hello t = c.hello();
        assertThat(t.getContent(), equalTo("Hello world!"));
        assertThat(t.getId(), equalTo(new Long(1)));
    }
}
