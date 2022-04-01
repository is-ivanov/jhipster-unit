package by.ivanov.unit.service;

import by.ivanov.unit.domain.User;
import by.ivanov.unit.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

	private static final String USER = "user";
	private static final String ADMIN = "admin";
	private static final String BASE_URL = "baseUrl";

	private final Logger log = LoggerFactory.getLogger(MailService.class);

	private final JHipsterProperties jHipsterProperties;
	private final JavaMailSender javaMailSender;
	private final MessageSource messageSource;
	private final SpringTemplateEngine templateEngine;
	private final Resource handshakeFile;
	private final UserRepository userRepository;

	public MailService(
		JHipsterProperties jHipsterProperties,
		JavaMailSender javaMailSender,
		MessageSource messageSource,
		SpringTemplateEngine templateEngine,
		@Value("classpath:/templates/mail/images/handshake.png") Resource handshakeFile,
		UserRepository userRepository
	) {
		this.jHipsterProperties = jHipsterProperties;
		this.javaMailSender = javaMailSender;
		this.messageSource = messageSource;
		this.templateEngine = templateEngine;
		this.handshakeFile = handshakeFile;
		this.userRepository = userRepository;
	}

	@Async
	public void sendEmail(
		String to,
		String subject,
		String content,
		boolean isMultipart,
		boolean isHtml,
		Map<String, Resource> inlineElements
	) {
		log.debug(
			"Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
			isMultipart,
			isHtml,
			to,
			subject,
			content
		);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			message.setText(content, isHtml);
			if (inlineElements != null) {
				for (Map.Entry<String, Resource> entry : inlineElements.entrySet()) {
					message.addInline(entry.getKey(), entry.getValue());
				}
			}
			javaMailSender.send(mimeMessage);
			log.debug("Sent email to User '{}'", to);
		} catch (MailException | MessagingException e) {
			log.warn("Email could not be sent to user '{}'", to, e);
		}
	}

	@Async
	public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
		sendEmailFromTemplate(user, templateName, titleKey, null, null);
	}

	@Async
	public void sendEmailFromTemplate(
		User user,
		String templateName,
		String titleKey,
		Map<String, Resource> inlineElements
	) {
		sendEmailFromTemplate(user, templateName, titleKey, null, inlineElements);
	}

	@Async
	public void sendEmailFromTemplate(
		User user,
		String templateName,
		String titleKey,
		User admin,
		Map<String, Resource> inlineElements
	) {
		boolean isMultipart = false;
		if (user.getEmail() == null) {
			log.debug("Email doesn't exist for user '{}'", user.getLogin());
			return;
		}
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
		String to = user.getEmail();
		if (admin != null) {
			if (admin.getEmail() == null) {
				log.debug("Email doesn't exist for admin '{}'", admin.getLogin());
				return;
			}
			context.setVariable(ADMIN, admin);
			to = admin.getEmail();
		}
		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);
		if (inlineElements != null) {
			isMultipart = true;
		}
		sendEmail(to, subject, content, isMultipart, true, inlineElements);
	}

	@Async
	public void sendActivationEmail(User user) {
		log.debug("Sending activation email to '{}'", user.getEmail());
		Map<String, Resource> inlineElements = new HashMap<>();
		inlineElements.put("handshake.png", handshakeFile);
		sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title", inlineElements);
	}

	@Async
	public void sendCreationEmail(User user) {
		log.debug("Sending creation email to '{}'", user.getEmail());
		sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
	}

	@Async
	public void sendPasswordResetMail(User user) {
		log.debug("Sending password reset email to '{}'", user.getEmail());
		sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
	}

	@Async
	public void sendSuccessfulNotificationEmails(User user) {
		log.debug("Sending notification emails after successful activation to {}", user.getEmail());
		Map<String, Resource> inlineElements = new HashMap<>();
		inlineElements.put("handshake.png", handshakeFile);
		sendEmailFromTemplate(
			user,
			"mail/successfulNotificationUserEmail",
			"email.successful.notification.title",
			inlineElements
		);
		List<User> admins = userRepository.findAllActiveAdmins();
		admins.forEach(admin -> {
			log.debug("Sending email to {} that user {} have activated", admin.getEmail(), user.getLogin());
			sendEmailFromTemplate(
				user,
				"mail/notificationAdminEmail",
				"email.notification.admin.title",
				admin,
				inlineElements
			);
		});
	}
}
