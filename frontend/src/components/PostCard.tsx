import { Heart, MessageCircle, Share } from "lucide-react";
import { formatDistanceToNow } from "date-fns";
import { es } from "date-fns/locale";
import { Post } from "../types/Post";

interface PostCardProps {
    post: Post;
}

export default function PostCard({ post }: Readonly<PostCardProps>) {
    const timeAgo = formatDistanceToNow(new Date(post.createdAt), {
        addSuffix: true,
        locale: es,
    });

    const initial = post.user.username.charAt(0).toUpperCase();

    return (
        <div className="post-card">
            {/* Header */}
            <div className="post-header">
                <div className="post-avatar">{initial}</div>
                <div className="post-user-info">
                    <span className="post-username">{post.user.username}</span>
                    <span className="post-time">{timeAgo}</span>
                </div>
            </div>

            {/* Content */}
            <p className="post-content">{post.content}</p>

            {/* Images */}
            {post.images.length > 0 && (
                <div className={`post-images post-images-${post.images.length}`}>
                    {post.images
                        .sort((a, b) => a.displayOrder - b.displayOrder)
                        .map(img => (
                            <img
                                key={img.id}
                                src={img.presignedUrl}
                                alt="post image"
                                className="post-image"
                            />
                        ))}
                </div>
            )}

            {/* Actions */}
            <div className="post-actions">
                <button className="post-action-btn">
                    <Heart size={18} />
                    <span>Me gusta</span>
                </button>
                <button className="post-action-btn">
                    <MessageCircle size={18} />
                    <span>Comentar</span>
                </button>
                <button className="post-action-btn">
                    <Share size={18} />
                    <span>Compartir</span>
                </button>
            </div>
        </div>
    );
}