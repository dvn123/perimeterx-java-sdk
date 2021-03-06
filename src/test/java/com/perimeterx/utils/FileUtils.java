package com.perimeterx.utils;

import com.perimeterx.models.configuration.ModuleMode;
import com.perimeterx.models.configuration.PXConfiguration;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

@PrepareForTest(FilesUtils.class)
public class FileUtils extends PowerMockTestCase {

    @Test
    public void testReadFileConfigAsMap() throws IOException {
        PowerMock.mockStaticPartial(FilesUtils.class, "readFile");
        EasyMock.expect(FilesUtils.readFile("config.json")).andReturn("{\n" +
                "  \"moduleMode\": 0,\n" +
                "  \"remoteConfigurationInterval\": 1\n" +
                "}\n");
        PowerMock.replayAll();
        Map<String, String> result = FilesUtils.readFileConfigAsMap("config.json");
        Assert.assertEquals(result.get("moduleMode"), "0");
        Assert.assertEquals(result.get("remoteConfigurationInterval"), "1");
    }

    @Test
    public void testReadFile() throws IOException {
        PowerMock.mockStaticPartial(FilesUtils.class, "readFile");
        EasyMock.expect(FilesUtils.readFile("config.json")).andReturn("{\n" +
                "  \"px_module_mode\": 0,\n" +
                "  \"px_remote_configuration_interval_ms\": 1\n" +
                "}\n");
        PowerMock.replayAll();
        PXConfiguration pxConfiguration = new PXConfiguration();
        FilesUtils.readFileConfigAsPXConfig(pxConfiguration, "config.json");
        Assert.assertEquals(pxConfiguration.getModuleMode(), ModuleMode.MONITOR);
        Assert.assertEquals(pxConfiguration.getRemoteConfigurationInterval(), 1);
    }

}
