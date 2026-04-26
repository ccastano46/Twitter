import { useState, useRef } from "react";
import { useAuth } from "../contexts/AuthContext";
import { Send, Image as ImageIcon, X } from "lucide-react";
import { toast } from "sonner";

const MAX_CHARS = 140;
const MAX_IMAGES = 3;
const API_URL = "http://localhost:8080";

export default function CreatePostForm({ onPostCreated }: { onPostCreated?: () => void }) {
    const { user, getToken } = useAuth();
    const [content, setContent] = useState("");
    const [selectedImages, setSelectedImages] = useState<File[]>([]);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const textareaRef = useRef<HTMLTextAreaElement>(null);

    const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(e.target.files || []);
        if (selectedImages.length + files.length > MAX_IMAGES) {
            toast.error(`Máximo ${MAX_IMAGES} imágenes permitidas`);
            return;
        }
        setSelectedImages(prev => [...prev, ...files]);
    };

    const removeImage = (index: number) => {
        setSelectedImages(prev => prev.filter((_, i) => i !== index));
    };

    const handleTextChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        const text = e.target.value;
        if (text.length <= MAX_CHARS) {
            setContent(text);
            if (textareaRef.current) {
                textareaRef.current.style.height = "auto";
                textareaRef.current.style.height =
                    Math.min(textareaRef.current.scrollHeight, 200) + "px";
            }
        }
    };

    const handleSubmit = async () => {
        if (!content.trim()) {
            toast.error("El post no puede estar vacío");
            return;
        }

        setIsSubmitting(true);

        try {
            const token = await getToken();
            const formData = new FormData();
            formData.append("content", content);
            selectedImages.forEach(img => formData.append("images", img));

            const res = await fetch(`${API_URL}/posts/create`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` },
                body: formData,
            });

            if (!res.ok) throw new Error("Error al publicar");

            toast.success("¡Post publicado!");
            setContent("");
            setSelectedImages([]);
            if (textareaRef.current) textareaRef.current.style.height = "auto";
            onPostCreated?.();
        } catch (error) {
            toast.error(error instanceof Error ? error.message : "Error al publicar");
        } finally {
            setIsSubmitting(false);
        }
    };

    const charCount = content.length;
    const charPercentage = (charCount / MAX_CHARS) * 100;

    return (
        <div className="create-post-form">
            <div className="create-post-inner">
                {/* Avatar */}
                <div className="create-post-avatar">
                    {user?.username.charAt(0).toUpperCase() ?? "?"}
                </div>

                {/* Content */}
                <div className="create-post-content">
                    <textarea
                        ref={textareaRef}
                        value={content}
                        onChange={handleTextChange}
                        placeholder="¿Qué está pasando?"
                        className="create-post-textarea"
                        disabled={isSubmitting}
                    />

                    {/* Image previews */}
                    {selectedImages.length > 0 && (
                        <div className={`create-post-previews create-post-previews-${selectedImages.length}`}>
                            {selectedImages.map((file, i) => (
                                <div key={i} className="create-post-preview">
                                    <img src={URL.createObjectURL(file)} alt={`Preview ${i}`} />
                                    <button
                                        className="create-post-remove-img"
                                        onClick={() => removeImage(i)}
                                    >
                                        <X size={14} />
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}

                    {/* Footer */}
                    <div className="create-post-footer">
                        <button
                            className="create-post-img-btn"
                            onClick={() => fileInputRef.current?.click()}
                            disabled={isSubmitting || selectedImages.length >= MAX_IMAGES}
                        >
                            <ImageIcon size={20} />
                        </button>
                        <input
                            ref={fileInputRef}
                            type="file"
                            multiple
                            accept="image/*"
                            onChange={handleImageSelect}
                            style={{ display: "none" }}
                        />

                        <div className="create-post-actions">
                            <span className={`create-post-char-count ${
                                charPercentage > 90 ? "danger" :
                                    charPercentage > 75 ? "warning" : ""
                            }`}>
                                {charCount}/{MAX_CHARS}
                            </span>
                            <button
                                className="create-post-submit"
                                onClick={handleSubmit}
                                disabled={isSubmitting || !content.trim()}
                            >
                                <Send size={16} />
                                <span>Publicar</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}