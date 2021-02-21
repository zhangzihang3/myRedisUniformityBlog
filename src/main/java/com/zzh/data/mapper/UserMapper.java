package com.zzh.data.mapper;

import com.zzh.data.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 张子行
 * @since 2021-02-17
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where id = #{id} for update")
    User pessimismSelectUserById(@Param("id") int id);

    /**
     *
     * @param user
     * @return int 1：更新成功，0：更新失败
     */
    @Update("update user set age = #{user.age},version = #{user.version}+1 where id = #{user.id} and version = #{user.version}")
    int optimisticUpdateById(@Param("user") User user);
}
