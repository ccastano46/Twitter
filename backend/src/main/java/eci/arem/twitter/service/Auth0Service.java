package eci.arem.twitter.service;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Auth0Service {

    @Value("${AUTH0_DOMAIN}")
    private String domain;

    @Value("${AUTH0_CLIENT_ID}")
    private String clientId;

    @Value("${AUTH0_CLIENT_SECRET}")
    private String clientSecret;

    public String createUser(String email, String password) throws Exception {

        AuthAPI authAPI = AuthAPI.newBuilder(domain, clientId, clientSecret).build();
        TokenRequest tokenRequest = authAPI.requestToken("https://" + domain + "/api/v2/");
        String managementToken = tokenRequest.execute().getBody().getAccessToken();


        ManagementAPI mgmt = ManagementAPI.newBuilder(domain, managementToken).build();

        User auth0User = new User("Username-Password-Authentication");
        auth0User.setEmail(email);
        auth0User.setPassword(password.toCharArray());

        User createdUser = mgmt.users().create(auth0User).execute().getBody();
        return createdUser.getId(); // retorna el auth0Id
    }
}