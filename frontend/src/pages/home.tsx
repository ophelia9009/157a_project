import React, { useState } from "react";
import Navbar from "../components/Navbar";
import { Link } from "react-router-dom";
import Searchbar from "../components/Searchbar";
import Login from "../components/Login";
import SignUp from "../components/SignUp";


interface Post { // I'm creating the functionality for getting posts in a diff file
    id: number;
    creator: string;
    title: string;
    body: string;
    comments: number;
}

const posts: Post[] = [ 
    {
        id: 1,
        creator: "user1",
        title: "First Post",
        body: "This is the body of the first post...",
        comments: 10,
    },
    {
        id: 2,
        creator: "user2",
        title: "Second Post",
        body: "This is the body of the second post...",
        comments: 5,
    },
];

const Home: React.FC = () => {
    const [dropdownVisible, setDropdownVisible] = useState(false);

    return (
        <div> 
            <Navbar></Navbar> 
            <Searchbar></Searchbar>

            <h3> i'll be moving this when the other stuff works </h3>
            <Login></Login>
            <SignUp></SignUp>

            <div className="post-container"> // TODO: THIS WILL USE A METHOD FOR LOADING POSTS (written in a diff file) 
                {posts.map((post) => (
                    <div className="post">
                        <h2>{post.title}</h2>
                        <p>{post.body}</p>
                        <p>
                            <strong>Creator:</strong> {post.creator}
                        </p>
                        <p>
                            <strong>Comments:</strong> {post.comments}
                        </p>
                        <Link to={`/post/${post.id}`}>Read more</Link>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Home;