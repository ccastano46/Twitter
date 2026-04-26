import { User } from "../contexts/AuthContext";

export interface PostImage {
    id: string;
    imageKey: string;
    displayOrder: number;
    presignedUrl: string;
}

export interface Post {
    id: string;
    content: string;
    createdAt: string;
    user: User;
    images: PostImage[];
}