import { useEffect, useState, useCallback } from "react";
import Navigation from "../components/Navigation";
import PostCard from "../components/PostCard";
import CreatePostForm from "../components/CreatePostForm";
import { useAuth } from "../contexts/AuthContext";
import { Post } from "../types/Post";

const API_URL = "http://localhost:8080";

export default function Home() {
    const { isAuthenticated } = useAuth();
    const [posts, setPosts] = useState<Post[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    const fetchPosts = useCallback(() => {
        fetch(`${API_URL}/posts/stream`)
            .then(res => res.json())
            .then(data => setPosts(data))
            .finally(() => setIsLoading(false));
    }, []);

    useEffect(() => {
        fetchPosts();
    }, [fetchPosts]);

    return (
        <>
            <Navigation />
            <div className="home-container">
                {isAuthenticated && (
                    <CreatePostForm onPostCreated={fetchPosts} />
                )}
                {isLoading ? (
                    <p className="home-loading">Cargando posts...</p>
                ) : posts.length === 0 ? (
                    <p className="home-empty">No hay posts aún.</p>
                ) : (
                    posts.map(post => <PostCard key={post.id} post={post} />)
                )}
            </div>
        </>
    );
}