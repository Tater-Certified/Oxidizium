package com.github.tatercertified.oxidizium_tester;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class OxidiziumTestMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (!mixinClassName.equals("com.github.tatercertified.oxidizium_tester.mixin.WindowMixin")) {
            OxidiziumTester.TEST_LOGGER.warn("Cancelling {} for testing!", mixinClassName);
            return true;
        }
        return false;
    }
}