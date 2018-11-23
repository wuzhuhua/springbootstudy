package org.spring.springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.spring.springboot.domain.MiniUser;
import org.spring.springboot.domain.MiniUserExample;

public interface MiniUserMapper {
    long countByExample(MiniUserExample example);

    int deleteByExample(MiniUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MiniUser record);

    int insertSelective(MiniUser record);

    List<MiniUser> selectByExample(MiniUserExample example);

    MiniUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MiniUser record, @Param("example") MiniUserExample example);

    int updateByExample(@Param("record") MiniUser record, @Param("example") MiniUserExample example);

    int updateByPrimaryKeySelective(MiniUser record);

    int updateByPrimaryKey(MiniUser record);

    int upsert(MiniUser miniUser);
}