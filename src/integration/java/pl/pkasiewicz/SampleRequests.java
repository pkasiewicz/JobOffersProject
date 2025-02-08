package pl.pkasiewicz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class SampleRequests {

    @Autowired
    MockMvc mockMvc;

    public ResultActions performRegisterPostRequest() throws Exception {
        return mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public ResultActions performSuccessLoginPostRequest() throws Exception {
        return mockMvc.perform(post("/token")
                .content("""
                        {
                            "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
