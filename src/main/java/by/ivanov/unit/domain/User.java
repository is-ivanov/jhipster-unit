package by.ivanov.unit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

	public static final String COLUMN_ID_NAME = "id";
	public static final String COLUMN_LOGIN_NAME = "login";
	public static final String COLUMN_PASSWORD_NAME = "password_hash";
	public static final String COLUMN_FIRSTNAME_NAME = "first_name";
	public static final String COLUMN_LASTNAME_NAME = "last_name";
	public static final String COLUMN_ACTIVATED_NAME = "activated";
	public static final String COLUMN_LANG_KEY_NAME = "lang_key";
	public static final String COLUMN_IMAGE_URL_NAME = "image_url";
	public static final String COLUMN_ACTIVATION_KEY_NAME = "activation_key";
	public static final String COLUMN_RESET_KEY_NAME = "reset_key";
	public static final String COLUMN_RESET_DATE_NAME = "reset_date";

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
	@SequenceGenerator(name = "user_sequence", sequenceName = "user__seq",
		initialValue = 50, allocationSize = 10)
	@Column(name = COLUMN_ID_NAME, nullable = false)
	private Long id;

	@NotNull
	@Pattern(regexp = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$")
	@Size(min = 1, max = 50)
	@Column(name = COLUMN_LOGIN_NAME, nullable = false, unique = true, length = 50)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = COLUMN_PASSWORD_NAME, nullable = false, length = 60)
	private String password;

	@Size(max = 50)
	@Column(name = COLUMN_FIRSTNAME_NAME, length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name = COLUMN_LASTNAME_NAME, length = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 254)
	@Column(length = 254, unique = true)
	private String email;

	@NotNull
	@Column(name = COLUMN_ACTIVATED_NAME, nullable = false)
	private boolean activated = false;

	@Size(min = 2, max = 10)
	@Column(name = COLUMN_LANG_KEY_NAME, length = 10)
	private String langKey;

	@Size(max = 256)
	@Column(name = COLUMN_IMAGE_URL_NAME, length = 256)
	private String imageUrl;

	@Size(max = 20)
	@Column(name = COLUMN_ACTIVATION_KEY_NAME, length = 20)
	@JsonIgnore
	private String activationKey;

	@Size(max = 20)
	@Column(name = COLUMN_RESET_KEY_NAME, length = 20)
	@JsonIgnore
	private String resetKey;

	@Column(name = COLUMN_RESET_DATE_NAME)
	private Instant resetDate = null;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
		name = "rel_users__authorities",
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
		inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
	)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Authority> authorities = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	// Lowercase the login before saving it in database
	public void setLogin(String login) {
		this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public Instant getResetDate() {
		return resetDate;
	}

	public void setResetDate(Instant resetDate) {
		this.resetDate = resetDate;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		return id != null && id.equals(((User) o).id);
	}

	@Override
	public int hashCode() {
		// see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
