package by.ivanov.unit.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String MODERATOR = "ROLE_MODERATOR";

    public static final String CUSTOMER = "ROLE_CUSTOMER";

    public static final String COMMISSIONER = "ROLE_COMMISSIONER";

    public static final String GENERAL_CONTRACTOR = "ROLE_GENERAL_CONTRACTOR";

    public static final String CONTRACTOR = "ROLE_CONTRACTOR";

    private AuthoritiesConstants() {}
}
