package com.perimeterx.api;

import com.perimeterx.api.blockhandler.templates.TemplateFactory;
import com.perimeterx.api.providers.DefaultHostnameProvider;
import com.perimeterx.api.providers.HostnameProvider;
import com.perimeterx.api.providers.IPProvider;
import com.perimeterx.api.providers.RemoteAddressIPProvider;
import com.perimeterx.models.PXContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by nitzangoldfeder on 05/03/2017.
 */
@Test
public class TemplatesTest {


    private String appId;
    private PXContext pxContext;

    private IPProvider ipProvider;
    private HostnameProvider hostnameProvider;

    @BeforeClass
    public void setUp() throws Exception {
        appId = "PX_APPID";
        this.ipProvider = new RemoteAddressIPProvider();
        this.hostnameProvider = new DefaultHostnameProvider();
        MockHttpServletRequest noCaptchaCookieReq = new MockHttpServletRequest();
        pxContext = new PXContext(noCaptchaCookieReq, this.ipProvider, this.hostnameProvider, appId);
        pxContext.setUuid("PX_UUID");
        pxContext.setVid("PX_VID");
    }

    @Test
    public void testCaptchaPageContainsLogo() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(true)
                .blockingScore(70)
                .customLogo("http://www.google.com/logo.jpg")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"captcha.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/logo.jpg") );
    }

    @Test
    public void testDefaultPageContainsLogo() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(false)
                .blockingScore(70)
                .customLogo("http://www.google.com/logo.jpg")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"block.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/logo.jpg") );
    }


    @Test
    public void testCaptchaPageContainsCSS() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(true)
                .blockingScore(70)
                .cssRef("http://www.google.com/stylesheet.css")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"captcha.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/stylesheet.css") );
    }

    @Test
    public void testDefualtPageContainsCSS() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(false)
                .blockingScore(70)
                .cssRef("http://www.google.com/stylesheet.css")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"block.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/stylesheet.css") );
    }

    @Test
    public void testDefualtPageContainsJS() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(false)
                .blockingScore(70)
                .cssRef("http://www.google.com/script.js")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"block.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/script.js") );
    }

    @Test
    public void testCaptchaPageContainsJS() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(true)
                .blockingScore(70)
                .cssRef("http://www.google.com/script.js")
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"captcha.mustache");
        assertTrue( actualHTML.contains("http://www.google.com/script.js") );
    }

    @Test
    public void testCaptchaPageContainsUUID() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(true)
                .blockingScore(70)
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"captcha.mustache");
        assertTrue( actualHTML.contains( pxContext.getUuid()) );
    }

    @Test
    public void testDefaultPageContainsUUID() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(false)
                .blockingScore(70)
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"block.mustache");
        assertTrue( actualHTML.contains( pxContext.getUuid()) );
    }

    @Test
    public void testCaptchaPageContainsVID() throws Exception {
        PXConfiguration pxConfig = new PXConfiguration.Builder()
                .appId(appId)
                .authToken("AUTH_123")
                .cookieKey("COOKIE_123")
                .captchaEnabled(false)
                .blockingScore(70)
                .build();
        String actualHTML = TemplateFactory.getTemplate(pxContext,pxConfig,"captcha.mustache");
        assertTrue( actualHTML.contains( pxContext.getVid() ) );
    }

}