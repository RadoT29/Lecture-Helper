package nl.tudelft.oopp.app.models;

public final class AdminSession {

    private static AdminSession instance;

    private String password;

    public AdminSession(String password) {
        this.password = password;
    }

    /**
     * Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     *
     * @param password The admin password needed for all admin requests
     * @return singleton class instance
     */
    public static AdminSession getInstance(String password) {
        if (instance == null) {
            instance = new AdminSession(password);
        }
        return instance;
    }

    /**
     * Get AdminSession instance.
     *
     * @return singleton class instance
     */
    public static AdminSession getInstance() {
        return instance;
    }

    public String getPassword() {
        return password;
    }

    /**
     * This method resets the session by making it null for tests.
     */
    public static void clearAdminSessionTest() {
        instance = null;
    }
}
