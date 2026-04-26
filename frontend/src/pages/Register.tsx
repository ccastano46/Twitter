import Navigation from "../components/Navigation";
import CardForm from "../components/CardForm";
import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { useLocation } from "wouter";

function Register() {
    const { register } = useAuth();
    const [, navigate] = useLocation();

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async () => {
        if (!username || !email || !password) {
            setError("Todos los campos son obligatorios");
            return;
        }

        setIsLoading(true);
        setError("");

        try {
            await register(username, email, password);
            navigate("/");
        } catch (err) {
            setError("Error al registrarse. El email o username ya existe.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <Navigation />
            <CardForm title="Registrarse" description="Crea una cuenta para seguir usando Twitter">
                <input
                    type="text"
                    placeholder="Nombre de usuario"
                    value={username}
                    onChange={e => setUsername(e.target.value)}
                />
                <input
                    type="email"
                    placeholder="Correo electrónico"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                />
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button onClick={handleSubmit} disabled={isLoading}>
                    {isLoading ? "Creando cuenta..." : "Crear cuenta"}
                </button>
            </CardForm>
        </>
    );
}

export default Register;