import { Toaster } from "sonner";
import { Route, Switch } from "wouter";
import { AuthProvider } from "./contexts/AuthContext";
import Home from "./pages/Home";
import Register from "./pages/Register";
import AuthGuard from "./components/AuthGuard.tsx";

function Router() {
    return (
        <Switch>
            <Route path="/" component={Home} />
            <Route path="/register" component={Register} />
        </Switch>
    );
}

function App() {
    return (
        <AuthProvider>
            <Toaster />
            <AuthGuard>
                <Router />
            </AuthGuard>

        </AuthProvider>
    );
}

export default App;