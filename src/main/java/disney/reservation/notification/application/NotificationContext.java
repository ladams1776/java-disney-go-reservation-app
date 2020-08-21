package disney.reservation.notification.application;

import com.gargoylesoftware.htmlunit.WebClient;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import config.AppConfig;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reference.HtmlElementReferrer;
import disney.reservation.notification.infrastructure.WebPageEssentials.Requestor.PageRequestor;
import disney.reservation.notification.infrastructure.WebPageEssentials.Requestor.PageRequestorInterface;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.DataMapper.Parser.ReservationParser;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.DataMapper.Parser.ReservationParserImpl;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.DataMapper.ReservationDataMapper;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.DataMapper.ReservationDataMapperImpl;
import disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.Date.DataMapper.DateDataMapperImpl;
import disney.reservation.notification.infrastructure.WebPageEssentials.ReservationResolver;
import disney.reservation.notification.infrastructure.WebPageEssentials.ReservationResolverImpl;
import disney.reservation.notification.domain.log.Logger;
import disney.reservation.notification.domain.mail.Mailer;
import disney.reservation.notification.domain.reservations.assemblers.EventAssembler;
import disney.reservation.notification.domain.reservations.assemblers.EventAssemblerFactory;
import disney.reservation.notification.domain.reservations.utils.DateDifference;
import disney.reservation.notification.domain.reservations.utils.GetNextDate;
import disney.reservation.notification.infrastructure.log.SystemLogger;
import disney.reservation.notification.infrastructure.mail.LogMailer;
import disney.reservation.notification.infrastructure.mongo.MongoDatabaseConnectionFactory;
import disney.reservation.notification.infrastructure.reservations.ReservationRepository;
import java.io.IOException;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NotificationContext {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Logger registerLoggerAdapter() {

//        return new FileLogger();
        return new SystemLogger();
    }

    @Bean
    public AppConfig registerAppConfig() throws IOException {
        return new AppConfig();
    }

    @Bean
    public MongoClient registerMongoClient() throws Exception {
        final AppConfig appConfig = registerAppConfig();
        return new MongoDatabaseConnectionFactory(appConfig).getObject();
    }

    @Bean
    public MongoDatabase registerMongoDatabase() throws Exception {
        return registerMongoClient().getDatabase("test");
    }

    @Bean
    public ReservationRepository registerReservationRepository() throws Exception {
        return new ReservationRepository(registerMongoDatabase());
    }

    @Bean
    public DateDifference registerDateDifference() {
        return new DateDifference(registerLoggerAdapter());
    }

    //@TODO: Need to test this factory/egistry
    @Bean
    public GetNextDate registerGetNextDate() {
        return new GetNextDate(this.registerLoggerAdapter());
    }

    @Bean
    public EventAssembler registerReservationEventAssembler() throws Exception {
        final DateDifference dateDifference = registerDateDifference();
        final GetNextDate getNextDate = registerGetNextDate();

        return new EventAssemblerFactory(dateDifference, getNextDate).getObject();
    }

    @Bean
    public Mailer createMailer() throws MessagingException, IOException {
        //@TODO: Could make this toggle-able, based on if this is dev environment.
//        AppConfig appConfig = registerAppConfig();
//        return new GmailMailer(appConfig.getDatabaseUser(),
//            appConfig.getDbPassword(),
//            appConfig.getRecipient());
        return new LogMailer();
    }

    @Bean("disney.reservation.notification.infrastructure.WebPageEssentials.Reservation.DataMapper.ReservationDataMapperImpl")
    public ReservationDataMapper ReservationDataMapperImpl() {
        Logger logger = applicationContext.getBean(Logger.class, "Logger");
        ReservationParser parser = new ReservationParserImpl(logger);
        return new ReservationDataMapperImpl(parser, new DateDataMapperImpl());
    }

    @Bean()
    public PageRequestorInterface PageRequestor() {
        WebClient webClient = new WebClient();
        return new PageRequestor(webClient);
    }

    @Bean()
    public ReservationResolver ReservationResolver() throws MessagingException {
        Logger logger = applicationContext.getBean(Logger.class);
        Mailer mailer = applicationContext.getBean(Mailer.class);
        HtmlElementReferrer htmlElementReferrer = new HtmlElementReferrer();
        return new ReservationResolverImpl(mailer, htmlElementReferrer, logger);
    }

    @Bean()
    public ReservationParser ReservationParser() {
        Logger logger = applicationContext.getBean(Logger.class);
        return new ReservationParserImpl(logger);
    }
}
