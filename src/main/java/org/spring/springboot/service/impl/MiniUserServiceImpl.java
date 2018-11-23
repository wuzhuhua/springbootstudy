package org.spring.springboot.service.impl;

import org.spring.springboot.dao.MiniUserDao;
import org.spring.springboot.dao.MiniUserMapper;
import org.spring.springboot.domain.MiniUser;
import org.spring.springboot.domain.MiniUserExample;
import org.spring.springboot.service.MiniUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiniUserServiceImpl implements MiniUserService {

    @Autowired
    private MiniUserMapper miniUserMapper;

    @Override
    public MiniUser getByOpenId(String openid) {
        MiniUserExample example = new MiniUserExample();
        example.createCriteria().andOpenIdEqualTo(openid);
        List<MiniUser> list = miniUserMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void save(MiniUser miniUser) {
        miniUserMapper.upsert(miniUser);
        miniUser = getByOpenId(miniUser.getOpenId());
    }
}
