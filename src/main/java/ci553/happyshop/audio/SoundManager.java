package ci553.happyshop.audio;

import java.awt.Toolkit;

/**
 * manages all sound effects in the application
 * uses Singleton pattern (only one sound manager needed)
 *
 * currently uses a system beep for simplicity
 * could be extended to play .wav or .mp3 files later but I am leaving it as it is.
 */
public class SoundManager {

    private static SoundManager instance;  // singleton instance
    private boolean soundEnabled = true;   // can toggle on/off

    /**
     * private constructor- use getInstance() instead
     */
    private SoundManager() {
        // Initialize sound system
    }

    /**
     * gets singleton instance
     */
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * plays a sound effect
     * (different sounds for different actions)
     *
     * @param effect Which sound to play
     */
    public void play(SoundEffect effect) {
        if (!soundEnabled) {
            return;  // sound is muted
        }

        // Use system beep for now is simple but effective
        // In production it would load actual sound files
        switch (effect) {
            case ADD_TO_BASKET:
                // Quick single beep
                Toolkit.getDefaultToolkit().beep();
                break;

            case REMOVE_FROM_BASKET:
                // Also a beep, but we know what action triggered it
                Toolkit.getDefaultToolkit().beep();
                break;

            case ORDER_SUCCESS:
                // Double beep for success
                Toolkit.getDefaultToolkit().beep();
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        Toolkit.getDefaultToolkit().beep();
                    } catch (InterruptedException e) {
                    }
                }).start();
                break;

            case PAYMENT_SUCCESS:
                // Triple beep for payment
                for (int i = 0; i < 3; i++) {
                    final int count = i;
                    new Thread(() -> {
                        try {
                            Thread.sleep(count * 100);
                            Toolkit.getDefaultToolkit().beep();
                        } catch (InterruptedException e) {
                        }
                    }).start();
                }
                break;

            case LOGIN_SUCCESS:
                Toolkit.getDefaultToolkit().beep();
                break;

            case ERROR:
                // Longer beep for errors
                Toolkit.getDefaultToolkit().beep();
                break;
        }

        System.out.println("🔊 Sound: " + effect);
    }

    /**
     * Enables or disables sound.
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        System.out.println("🔊 Sound " + (enabled ? "enabled" : "disabled"));
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}