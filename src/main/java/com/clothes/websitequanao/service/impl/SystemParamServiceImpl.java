package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.entity.SystemParam;
import com.clothes.websitequanao.repository.SystemParamRepo;
import com.clothes.websitequanao.service.SystemParamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemParamServiceImpl implements SystemParamService {

    private final SystemParamRepo systemParamRepository;
    @Override
    public String getSystemByKey(String key) {
        SystemParam sys = systemParamRepository.findByKey(key);
        if (sys != null) {
            return sys.getValue();
        }
        return "";
    }
}
