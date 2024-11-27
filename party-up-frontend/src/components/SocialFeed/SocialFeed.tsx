import React, { useState } from 'react';
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
  const [visiblePosts, setVisiblePosts] = useState<number>(3); // shows only 3 posts at the begining
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleLoadMore = () => {
    if (isLoading) return; // prevent mulitple clicks
    setIsLoading(true);

    setTimeout(() => {
      setVisiblePosts((prev) => prev + 3); // add 3 more posts
      setIsLoading(false);
    }, 1500);
  };

  return (
    <div className={styles.container}>
      <section className={styles['social-feed']}>
        <h2 className={styles.header}>Social Feed</h2>
        <ul className={styles.posts}>
          {posts.slice(0, visiblePosts).map(
            (
              post, // Use slice to control visible posts
            ) => (
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
            ),
          )}
        </ul>
        {visiblePosts < posts.length && ( // show the button if there is more posts
          <button
            className={styles['load-more-button']}
            onClick={handleLoadMore}
            aria-label="Load more posts"
          >
            {isLoading ? (
              <span className={styles.spinner}>Loading...</span>
            ) : (
              'Load More'
            )}
          </button>
        )}
      </section>
    </div>
  );
};

export default SocialFeed;
