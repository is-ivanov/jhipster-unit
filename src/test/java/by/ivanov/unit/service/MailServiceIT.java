package by.ivanov.unit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import by.ivanov.unit.IntegrationTest;
import by.ivanov.unit.config.Constants;
import by.ivanov.unit.domain.User;
import by.ivanov.unit.repository.UserRepository;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Integration tests for {@link MailService}.
 */
@IntegrationTest
class MailServiceIT {

	private static final String[] languages = {
		"ru",
		"en",
		// jhipster-needle-i18n-language-constant - JHipster will add/remove languages in this array
	};
	private static final Pattern PATTERN_LOCALE_3 = Pattern.compile("([a-z]{2})-([a-zA-Z]{4})-([a-z]{2})");
	private static final Pattern PATTERN_LOCALE_2 = Pattern.compile("([a-z]{2})-([a-z]{2})");

	@Autowired
	private JHipsterProperties jHipsterProperties;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@MockBean
	private UserRepository userRepository;

	@Value("classpath:/templates/mail/images/handshake.png")
	private Resource handshakeFile;

	@Spy
	private JavaMailSenderImpl javaMailSender;

	@Captor
	private ArgumentCaptor<MimeMessage> messageCaptor;

	private MailService mailService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));
		mailService =
			new MailService(
				jHipsterProperties,
				javaMailSender,
				messageSource,
				templateEngine,
				handshakeFile,
				userRepository
			);
	}

	@Test
	void testSendEmail() throws Exception {
		mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false, null);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getSubject()).isEqualTo("testSubject");
		assertThat(message.getAllRecipients()[0]).hasToString("john.doe@example.com");
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent()).isInstanceOf(String.class);
		assertThat(message.getContent()).hasToString("testContent");
		assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
	}

	@Test
	void testSendHtmlEmail() throws Exception {
		mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true, null);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getSubject()).isEqualTo("testSubject");
		assertThat(message.getAllRecipients()[0]).hasToString("john.doe@example.com");
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent()).isInstanceOf(String.class);
		assertThat(message.getContent()).hasToString("testContent");
		assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testSendMultipartEmail() throws Exception {
		mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false, null);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		MimeMultipart mp = (MimeMultipart) message.getContent();
		MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		part.writeTo(aos);
		assertThat(message.getSubject()).isEqualTo("testSubject");
		assertThat(message.getAllRecipients()[0]).hasToString("john.doe@example.com");
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent()).isInstanceOf(Multipart.class);
		assertThat(aos).hasToString("\r\ntestContent");
		assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
	}

	@Test
	void testSendMultipartHtmlEmail() throws Exception {
		mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, true, null);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		MimeMultipart mp = (MimeMultipart) message.getContent();
		MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		part.writeTo(aos);
		assertThat(message.getSubject()).isEqualTo("testSubject");
		assertThat(message.getAllRecipients()[0]).hasToString("john.doe@example.com");
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent()).isInstanceOf(Multipart.class);
		assertThat(aos).hasToString("\r\ntestContent");
		assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testSendEmailFromTemplate() throws Exception {
		User user = new User();
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		user.setLangKey("en");
		mailService.sendEmailFromTemplate(user, "mail/testEmail", "email.test.title");
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getSubject()).isEqualTo("test title");
		assertThat(message.getAllRecipients()[0]).hasToString(user.getEmail());
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent().toString())
			.isEqualToNormalizingNewlines("<html>test title, http://127.0.0.1:8080, john</html>\n");
		assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testSendActivationEmail() throws Exception {
		User user = new User();
		user.setLangKey(Constants.DEFAULT_LANGUAGE);
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		mailService.sendActivationEmail(user);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		MimeMultipart mp = (MimeMultipart) message.getContent();
		MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
		assertThat(message.getAllRecipients()[0]).hasToString(user.getEmail());
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent()).isInstanceOf(Multipart.class);
		assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testCreationEmail() throws Exception {
		User user = new User();
		user.setLangKey(Constants.DEFAULT_LANGUAGE);
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		mailService.sendCreationEmail(user);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getAllRecipients()[0]).hasToString(user.getEmail());
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent().toString()).isNotEmpty();
		assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testSendPasswordResetMail() throws Exception {
		User user = new User();
		user.setLangKey(Constants.DEFAULT_LANGUAGE);
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		mailService.sendPasswordResetMail(user);
		verify(javaMailSender).send(messageCaptor.capture());
		MimeMessage message = messageCaptor.getValue();
		assertThat(message.getAllRecipients()[0]).hasToString(user.getEmail());
		assertThat(message.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(message.getContent().toString()).isNotEmpty();
		assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	@Test
	void testSendEmailWithException() {
		doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
		try {
			mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false, null);
		} catch (Exception e) {
			fail("Exception shouldn't have been thrown");
		}
	}

	@Test
	void testSendLocalizedEmailForAllSupportedLanguages() throws Exception {
		User user = new User();
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		for (String langKey : languages) {
			user.setLangKey(langKey);
			mailService.sendEmailFromTemplate(user, "mail/testEmail", "email.test.title");
			verify(javaMailSender, atLeastOnce()).send(messageCaptor.capture());
			MimeMessage message = messageCaptor.getValue();

			String propertyFilePath = "i18n/messages_" + getJavaLocale(langKey) + ".properties";
			URL resource = this.getClass().getClassLoader().getResource(propertyFilePath);
			assert resource != null;
			File file = new File(new URI(resource.getFile()).getPath());
			Properties properties = new Properties();
			properties.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

			String emailTitle = (String) properties.get("email.test.title");
			assertThat(message.getSubject()).isEqualTo(emailTitle);
			assertThat(message.getContent().toString())
				.isEqualToNormalizingNewlines("<html>" + emailTitle + ", http://127.0.0.1:8080, john</html>\n");
		}
	}

	@Test
	@DisplayName("sendSuccessfulNotificationEmails")
	void sendSuccessfulNotificationEmails() throws Exception {
		User user = new User();
		user.setLangKey(Constants.DEFAULT_LANGUAGE);
		user.setLogin("john");
		user.setEmail("john.doe@example.com");
		User admin1 = new User();
		admin1.setLangKey(Constants.DEFAULT_LANGUAGE);
		admin1.setLogin("ivan");
		admin1.setEmail("ivan.doe@example.com");
		User admin2 = new User();
		admin2.setLangKey(Constants.DEFAULT_LANGUAGE);
		admin2.setLogin("oleg");
		admin2.setEmail("oleg.doe@example.com");
		List<User> admins = List.of(admin1, admin2);

		when(userRepository.findAllActiveAdmins()).thenReturn(admins);

		mailService.sendSuccessfulNotificationEmails(user);

		verify(javaMailSender, times(3)).send(messageCaptor.capture());
		MimeMessage firstMessage = messageCaptor.getAllValues().get(0);
		MimeMessage secondMessage = messageCaptor.getAllValues().get(1);
		MimeMessage thirdMessage = messageCaptor.getAllValues().get(2);
		MimeMultipart mp = (MimeMultipart) firstMessage.getContent();
		MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
		assertThat(firstMessage.getAllRecipients()[0]).hasToString(user.getEmail());
		assertThat(secondMessage.getAllRecipients()[0]).hasToString(admin1.getEmail());
		assertThat(thirdMessage.getAllRecipients()[0]).hasToString(admin2.getEmail());
		assertThat(firstMessage.getFrom()[0]).hasToString(jHipsterProperties.getMail().getFrom());
		assertThat(firstMessage.getContent()).isInstanceOf(Multipart.class);
		assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
	}

	/**
	 * Convert a lang key to the Java locale.
	 */
	private String getJavaLocale(String langKey) {
		String javaLangKey = langKey;
		Matcher matcher2 = PATTERN_LOCALE_2.matcher(langKey);
		if (matcher2.matches()) {
			javaLangKey = matcher2.group(1) + "_" + matcher2.group(2).toUpperCase();
		}
		Matcher matcher3 = PATTERN_LOCALE_3.matcher(langKey);
		if (matcher3.matches()) {
			javaLangKey = matcher3.group(1) + "_" + matcher3.group(2) + "_" + matcher3.group(3).toUpperCase();
		}
		return javaLangKey;
	}
}
