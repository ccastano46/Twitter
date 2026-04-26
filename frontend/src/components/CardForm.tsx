import React from "react";

interface CardFormProps {
    children: React.ReactNode;
    title: string;
    description: string;
}

function CardForm({ children, title, description }: Readonly<CardFormProps>) {
    return (
        <div className="card-form">
            <div className="header">
                <h1>{title}</h1>
                <p>{description}</p>
            </div>

            <div className="container">
                {children}
            </div>
        </div>

    );
}

export default CardForm;