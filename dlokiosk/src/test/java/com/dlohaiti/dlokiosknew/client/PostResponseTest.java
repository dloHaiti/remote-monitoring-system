package com.dlohaiti.dlokiosknew.client;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PostResponseTest {

    @Test
    public void shouldBeSuccessfulIfErrorsEmpty() {
        PostResponse response = new PostResponse();

        assertThat(response.getErrors().isEmpty(), is(true));
        assertThat(response.isSuccess(), is(true));
    }

    @Test
    public void shouldNotBeSuccessfulIfErrors() {
        PostResponse response = new PostResponse(asList("SERVER_ERROR"));

        assertThat(response.getErrors().isEmpty(), is(false));
        assertThat(response.isSuccess(), is(false));
    }
}
