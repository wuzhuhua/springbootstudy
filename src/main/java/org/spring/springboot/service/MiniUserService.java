package org.spring.springboot.service;

import org.spring.springboot.domain.MiniUser;

public interface MiniUserService {

    MiniUser getByOpenId(String openid);

    void save(MiniUser miniUser);
}
