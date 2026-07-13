package com.github.tatercertified.oxidizium_tester;

import com.github.tatercertified.oxidizium_tester.test.TestingGUI;
import imgui.app.Application;
import net.fabricmc.api.ClientModInitializer;

public class OxidiziumTesterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Application.launch(new TestingGUI());
    }
}
