package com.changjiang.lowcode.domain.service;

import com.changjiang.lowcode.interfaces.dto.GeneratorRequest;
import com.changjiang.lowcode.interfaces.dto.Result;

public interface GeneratorService {
    Result generate(GeneratorRequest request);
} 