package exceptions;

public class CustomExceptions {
    public static class UserAlreadyExists extends Exception {
        private final String username;

        public UserAlreadyExists(String message, String username) {
            super(message);
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class UserNotExists extends Exception {

        public UserNotExists(String message) {
            super(message);
        }

    }

    public static class ConfirmPasswordMismatch extends Exception {
        public ConfirmPasswordMismatch(String message) {
            super(message);
        }
    }

    public static class EmptyInput extends Exception {
        public EmptyInput(String message) {
            super(message);
        }
    }

    public static class WrongPassword extends Exception {
        public WrongPassword(String message) {
            super(message);
        }
    }

    public static class AssetNotExists extends Exception {
        public AssetNotExists(String message) {
            super(message);
        }
    }

    public static class NotEnoughBalance extends Exception {
        public NotEnoughBalance(String message) {
            super(message);
        }
    }

    public static class NotEnoughAssetQuantity extends Exception {
        public NotEnoughAssetQuantity(String message) {
            super(message);
        }
    }

    public static class Unauthorized extends Exception {
        public Unauthorized(String message) {
            super(message);
        }
    }
}
