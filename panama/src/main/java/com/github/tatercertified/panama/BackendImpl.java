package com.github.tatercertified.panama;

import com.github.tatercertified.oxidizium.utils.backend.Backend;
import com.github.tatercertified.oxidizium.utils.backend.BackendType;

public class BackendImpl implements Backend {
    @Override
    public BackendType backendType() {
        return BackendType.Panama;
    }
}
