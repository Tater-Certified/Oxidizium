package com.github.tatercertified.oxidizium_tester;

import com.github.tatercertified.oxidizium.Config;
import com.github.tatercertified.oxidizium.utils.MappingTranslator;
import com.github.tatercertified.oxidizium_tester.test.TestingGUI;
import imgui.app.Application;
import net.fabricmc.api.ClientModInitializer;

public class OxidiziumTesterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (Config.getInstance().test() || MappingTranslator.IS_DEV) {
            Application.launch(new TestingGUI());
        }
    }
}
