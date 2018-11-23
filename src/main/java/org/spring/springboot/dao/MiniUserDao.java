package org.spring.springboot.dao;

import org.spring.springboot.domain.MiniUser;

public interface MiniUserDao {
    /**
     * 获取小程序用户信息
     * @param openid
     * @return
     */
    MiniUser getByOpenId(String openid);
}
