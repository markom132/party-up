import React from 'react';
import styles from './SocialFeed.module.scss';

interface Post {
  id: number;
  username: string;
  profilePicture: string;
  content: string;
  image?: string;
  likes: number;
  comments: number;
}

interface SocialFeedProps {
  posts: Post[];
  // eslint-disable-next-line no-unused-vars
  onLike: (postId: number) => void;
  // eslint-disable-next-line no-unused-vars
  onComment: (postId: number) => void;
}

const SocialFeed: React.FC<SocialFeedProps> = ({
  posts,
  onLike,
  onComment,
}) => {
  return (
    <div className={styles.container}>
      <section className={styles['social-feed']}>
        <h2 className={styles.header}>Social Feed</h2>
        <ul className={styles.posts}>
          {posts.map((post) => (
            <li key={post.id} className={styles.post}>
              <div className={styles['post-header']}>
                <img
                  src={post.profilePicture}
                  alt={`${post.username}'s profile`}
                  className={styles['profile-picture']}
                />
                <span className={styles.username}>{post.username}</span>
              </div>
              {post.image && (
                <img
                  src={post.image}
                  alt="Post content"
                  className={styles['post-image']}
                />
              )}
              <p className={styles.content}>{post.content}</p>
              <div className={styles.actions}>
                <button
                  className={styles['like-button']}
                  onClick={() => onLike(post.id)}
                >
                  Like ({post.likes})
                </button>
                <button
                  className={styles['comment-button']}
                  onClick={() => onComment(post.id)}
                >
                  Comment ({post.comments})
                </button>
              </div>
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
};

export default SocialFeed;
