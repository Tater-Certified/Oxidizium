package com.github.tatercertified.oxidizium_tester;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.github.tatercertified.oxidizium_tester.test.ParityManager;

import java.util.List;

public class OxidiziumTestMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        ParityManager.collectMixins(targetClassNames.getFirst(), mixinClassName);

        if (!mixinClassName.equals("com.github.tatercertified.oxidizium_tester.mixin.WindowMixin") && !mixinClassName.equals("com.github.tatercertified.panama.mixin.LinuxFixMixin") ) {
            OxidiziumTester.TEST_LOGGER.warn("Cancelling {} for testing!", mixinClassName);
            return true;
        }
        return false;
    }
}