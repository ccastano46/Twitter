import React from "react";
import { createRoot } from "react-dom/client";
import { Auth0Provider } from "@auth0/auth0-react";
import App from "./App";
import "./styles/index.css";

createRoot(document.getElementById("root")!).render(
    <Auth0Provider
        domain="ccastano41.us.auth0.com"
        clientId="Uc1Cx5wb92F3GZb7CiLgEJ0DoGrJrWJY"
        authorizationParams={{
            redirect_uri: `${window.location.origin}/`,
            audience: "https://twitter-api",
            scope: "read:profile write:profile write:posts",
        }}
        cacheLocation="localstorage" // Agrega esto
    >
        <App />
    </Auth0Provider>
);
