package com.ciklum.pavlov;

import com.ciklum.pavlov.commands.ICommand;
import com.ciklum.pavlov.commands.impl.GlobalCommand;
import com.ciklum.pavlov.commands.impl.account.AuthenticationCommand;
import com.ciklum.pavlov.commands.impl.account.RegistrationCommand;
import com.ciklum.pavlov.commands.impl.account.SignInCommand;
import com.ciklum.pavlov.commands.impl.common.LanguagePollCommand;
import com.ciklum.pavlov.commands.impl.common.MainMenuCommand;
import com.ciklum.pavlov.commands.impl.common.StopCommand;
import com.ciklum.pavlov.commands.impl.order.OrderProductCommand;
import com.ciklum.pavlov.commands.impl.order.DisplayOrderCommand;
import com.ciklum.pavlov.commands.impl.order.UpdateOrderCommand;
import com.ciklum.pavlov.commands.impl.product.*;
import com.ciklum.pavlov.constants.ApplicationInitConstants;
import com.ciklum.pavlov.constants.UIKeys;
import com.ciklum.pavlov.context.ApplicationContext;
import com.ciklum.pavlov.dao.OrderDao;
import com.ciklum.pavlov.dao.ProductDao;
import com.ciklum.pavlov.dao.UserDao;
import com.ciklum.pavlov.dao.impl.OrderDaoImpl;
import com.ciklum.pavlov.dao.impl.ProductDaoImpl;
import com.ciklum.pavlov.dao.impl.UserDaoImpl;
import com.ciklum.pavlov.jdbc.TransactionManager;
import com.ciklum.pavlov.jdbc.impl.TransactionManagerImpl;
import com.ciklum.pavlov.models.OptionDescription;
import com.ciklum.pavlov.services.AccountService;
import com.ciklum.pavlov.services.OrderService;
import com.ciklum.pavlov.services.ProductService;
import com.ciklum.pavlov.services.impl.AccountServiceImpl;
import com.ciklum.pavlov.services.impl.OrderServiceImpl;
import com.ciklum.pavlov.services.impl.ProductServiceImpl;
import com.ciklum.pavlov.util.io.CustomReader;
import com.ciklum.pavlov.util.io.CustomWriter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

import static com.ciklum.pavlov.constants.ApplicationInitConstants.JDBC_PROPERTIES_FILE;
import static com.ciklum.pavlov.constants.ApplicationInitConstants.SQL_QUERIES_PROPERTY_FILE;

@Slf4j
public class Application {
    private ApplicationContext applicationContext;

    private BasicDataSource basicDataSource;
    private Properties configurationProperties;
    private Properties sqlQueriesProperties;
    private AccountService accountService;
    private ProductService productService;
    private OrderService orderService;
    private LanguagePollCommand languageCommand;
    private AuthenticationCommand authenticationCommand;
    private MainMenuCommand mainMenuCommand;
    private  CustomWriter writer;
    private  CustomReader reader;

    public void start() {
        loadProperties();
        initIO();
        initDataSource();
        initApplicationContext();
        initServices();
        initLanguageCommand();
        initAuthorizationCommand();
        initMeinMenuCommand();
        initMeinMenuCommand();
        GlobalCommand executor = new GlobalCommand(authenticationCommand, languageCommand, mainMenuCommand);
        executor.execute();
    }

    private void initApplicationContext() {
        log.info("Application context initializing...");
        applicationContext = ApplicationContext.getInstance();
        applicationContext.setReader(reader);
        applicationContext.setWriter(writer);
        applicationContext.setResourceBundle(ResourceBundle.getBundle("application"));
    }

    private void initLanguageCommand() {
        log.info("Initializing LanguagePollCommand...");
        LinkedHashMap<OptionDescription, Locale> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(new OptionDescription(UIKeys.ONE, UIKeys.LANGUAGE_ENGLISH), new Locale("en"));
        linkedHashMap.put(new OptionDescription(UIKeys.TWO, UIKeys.LANGUAGE_RUSSIAN), new Locale("ru"));
        languageCommand = new LanguagePollCommand(applicationContext, linkedHashMap);
    }

    private void initDataSource() {
        log.debug("Initializing connection pool...");
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(configurationProperties.getProperty(ApplicationInitConstants.DB_DRIVER));
        basicDataSource.setUrl(configurationProperties.getProperty(ApplicationInitConstants.DB_URL));
        basicDataSource.setUsername(configurationProperties.getProperty(ApplicationInitConstants.DB_USERNAME));
        basicDataSource.setPassword(configurationProperties.getProperty(ApplicationInitConstants.DB_PASSWORD));
        basicDataSource.setInitialSize(NumberUtils.createInteger(configurationProperties.getProperty(ApplicationInitConstants.DB_INIT_POOL_SIZE)));
        basicDataSource.setMaxTotal(NumberUtils.createInteger(configurationProperties.getProperty(ApplicationInitConstants.DB_MAX_POOL_SIZE)));
    }

    private void loadProperties() {
        log.info("Loading all application properties...");
        configurationProperties = loadApplicationProperties(JDBC_PROPERTIES_FILE);
        sqlQueriesProperties = loadApplicationProperties(SQL_QUERIES_PROPERTY_FILE);
    }

    private static Properties loadApplicationProperties(String propertiesFileName) {
        log.info(String.format("Loading %s...", propertiesFileName));
        Properties properties = new Properties();
        ClassLoader classLoader = Application.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(propertiesFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("The loading local resources has been fail!");
        }
        log.info("Local application init resources loaded!");
        return properties;
    }

    private void initMeinMenuCommand() {
        log.info("Initializing MeinMenuCommand...");
        Map<OptionDescription, ICommand> iCommandMap = new LinkedHashMap<>();
        iCommandMap.put(new OptionDescription(UIKeys.ONE, UIKeys.SHOW_ALL_PRODUCTS), new com.ciklum.pavlov.commands.impl.product.DisplayProductsCommand(applicationContext, productService));
        iCommandMap.put(new OptionDescription(UIKeys.TWO, UIKeys.SHOW_ALL_ORDERED_PRODUCTS), new DisplayOrderedProductsCommand(applicationContext, productService));
        iCommandMap.put(new OptionDescription(UIKeys.THREE, UIKeys.CREATE_PRODUCT), new CreateProductCommand(applicationContext, productService));
        iCommandMap.put(new OptionDescription(UIKeys.FOUR, UIKeys.SHOW_ORDER), new DisplayOrderCommand(applicationContext, orderService));
        iCommandMap.put(new OptionDescription(UIKeys.FIVE, UIKeys.UPDATE_ORDER), new UpdateOrderCommand(applicationContext, orderService));
        iCommandMap.put(new OptionDescription(UIKeys.SIX, UIKeys.ADD_PRODUCT_TO_ORDER), new OrderProductCommand(applicationContext, orderService));
        iCommandMap.put(new OptionDescription(UIKeys.SEVEN, UIKeys.REMOVE_PRODUCT), new RemoveProductCommand(applicationContext, productService));
        iCommandMap.put(new OptionDescription(UIKeys.EIGHT, UIKeys.REMOVE_ALL_PRODUCT), new RemoveAllProductsCommand(applicationContext, productService));
        iCommandMap.put(new OptionDescription(UIKeys.ZERO, UIKeys.EXIT), new StopCommand());
        mainMenuCommand = new MainMenuCommand(applicationContext, iCommandMap);
    }

    private void initServices() {
        log.info("Initializing services...");
        TransactionManager transactionManager = new TransactionManagerImpl(basicDataSource);
        UserDao userDao = new UserDaoImpl(sqlQueriesProperties);
        accountService = new AccountServiceImpl(userDao, transactionManager);
        ProductDao productDao = new ProductDaoImpl(sqlQueriesProperties);
        productService = new ProductServiceImpl(transactionManager, productDao);
        OrderDao orderDao = new OrderDaoImpl(sqlQueriesProperties);
        orderService = new OrderServiceImpl(orderDao, productService, transactionManager);
    }

    private void initAuthorizationCommand() {
        log.info("Initializing AuthenticationCommand...");
        SignInCommand signInCommand = new SignInCommand(applicationContext, accountService);
        RegistrationCommand registrationCommand = new RegistrationCommand(applicationContext, accountService);
        Map<OptionDescription, ICommand> commandMap = new HashMap<>();
        commandMap.put(new OptionDescription(UIKeys.ONE, UIKeys.SIGN_IN_HEADER), signInCommand);
        commandMap.put(new OptionDescription(UIKeys.TWO, UIKeys.REGISTRATION_HEADER), registrationCommand);
        authenticationCommand = new AuthenticationCommand(applicationContext, commandMap);
    }

    private void initIO() {
        reader = new CustomReader(System.in, Charset.forName(configurationProperties.getProperty("console.charset")));
        writer = new CustomWriter(System.out, Charset.forName(configurationProperties.getProperty("console.charset")));
    }
}
