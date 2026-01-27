package ci553.happyshop.audio;

/**
 * Enum defining all sound effects in the application
 * (using enum ensures only valid sound types can be played)
 * easy to add new sounds
 */
public enum SoundEffect {
    ADD_TO_BASKET,        // When product added to cart
    REMOVE_FROM_BASKET,   // When product removed from cart
    ORDER_SUCCESS,        // When order placed successfully
    PAYMENT_SUCCESS,      // When payment completes
    LOGIN_SUCCESS,        // When user logs in
    ERROR                 // When an error occurs
}