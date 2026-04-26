import { Home, User, LogOut, LogIn, UserPlus, XIcon } from "lucide-react";
import { useLocation } from "wouter";
import { useAuth } from "../contexts/AuthContext";
import "../styles/index.css";
import Twitter from "../assets/twitter.png";

function Navigation() {
    const { user, isAuthenticated, login, logout } = useAuth();
    const [location, navigate] = useLocation();


    const initial = user?.username?.charAt(0).toUpperCase() ?? "?";
    const handleNavigation = (path: string) => {
        navigate(path);
    };

    return (
        <nav className="nav">
            <div className="nav-container">
                <img src={Twitter} width={28} height={28} alt="X" className="nav-logo" />



                <div className="nav-links">
                    {isAuthenticated ? (
                        <>
                            <button className="nav-btn" title="Inicio" onClick={() => handleNavigation('/')}>
                                <Home size={20} />
                                <span>Inicio</span>
                            </button>

                            <button className="nav-user-avatar" onClick={() => handleNavigation('/')}>
                                {initial}
                            </button>

                            <button onClick={logout} className="nav-btn-logout" title="Cerrar sesión">
                                <LogOut size={18} />
                                <span>Salir</span>
                            </button>
                        </>
                    ) : (
                        <>
                            <button onClick={login} className="nav-btn-login">
                                <LogIn size={18} />
                                <span>Iniciar sesión</span>
                            </button>

                            <button onClick={() => handleNavigation('/register')} className="secondary-btn">
                                <UserPlus size={18} />
                                <span>Registrarse</span>
                            </button>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navigation;