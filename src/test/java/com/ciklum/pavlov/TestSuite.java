package com.ciklum.pavlov;

import com.ciklum.pavlov.commands.impl.account.AuthenticationCommandTest;
import com.ciklum.pavlov.commands.impl.account.RegistrationCommandTest;
import com.ciklum.pavlov.commands.impl.account.SignInCommandTest;
import com.ciklum.pavlov.commands.impl.common.LanguagePollCommandTest;
import com.ciklum.pavlov.commands.impl.common.MainMenuCommandTest;
import com.ciklum.pavlov.commands.impl.common.StopCommandTest;
import com.ciklum.pavlov.commands.impl.order.DisplayOrderCommandTest;
import com.ciklum.pavlov.commands.impl.order.UpdateOrderCommandTest;
import com.ciklum.pavlov.commands.impl.product.*;
import com.ciklum.pavlov.jdbc.handler.HandlerFactoryTest;
import com.ciklum.pavlov.jdbc.handler.JDBCUtilTest;
import com.ciklum.pavlov.jdbc.impl.TransactionManagerImplTest;
import com.ciklum.pavlov.services.impl.AccountServiceImplTest;
import com.ciklum.pavlov.services.impl.OrderServiceImplTest;
import com.ciklum.pavlov.services.impl.ProductServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticationCommandTest.class,
        RegistrationCommandTest.class,
        SignInCommandTest.class,
        LanguagePollCommandTest.class,
        MainMenuCommandTest.class,
        StopCommandTest.class,
        DisplayOrderCommandTest.class,
        UpdateOrderCommandTest.class,
        CreateProductCommandTest.class,
        DisplayOrderedProductsCommandTest.class,
        DisplayProductsCommandTest.class,
        RemoveAllProductsCommandTest.class,
        RemoveProductCommandTest.class,
        HandlerFactoryTest.class,
        JDBCUtilTest.class,
        TransactionManagerImplTest.class,
        AccountServiceImplTest.class,
        OrderServiceImplTest.class,
        ProductServiceImplTest.class
})
public class TestSuite {
}
