import React from 'react';
import styles from './SuccessMessageModal.module.scss';

interface SuccessMessageModalProps {
  isOpen: boolean;
  onClose: () => void;
  onResendEmail: () => void;
}

const SuccessMessageModal: React.FC<SuccessMessageModalProps> = ({
  isOpen,
  onResendEmail,
}) => {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} data-testid="success-modal">
      <div className={styles.modal}>
        <div className={styles.icon}>🎉</div>
        <h2 className={styles.title}>Thanks for registering!</h2>
        <p className={styles.subtitle}>
          Check your email to activate your account. We have sent you an email
          with an activation link.
        </p>
        <button className={styles['resend-button']} onClick={onResendEmail}>
          Didn&apos;t get an email? Send again.
        </button>
        <a
          href="mailto:support@partyup.com"
          className={styles['support-button']}
        >
          Got a problem? Contact us.
        </a>
        <a href="/login" className={styles['close-button']}>
          Return to login page
        </a>
      </div>
    </div>
  );
};

export default SuccessMessageModal;
