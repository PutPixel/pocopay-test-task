package com.github.pocopay.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.github.pocopay.model.Account;

public interface AccountMapper {

    @Select("SELECT * FROM ACCOUNT")
    public List<Account> findAll();

    @Select("SELECT * FROM ACCOUNT WHERE ID = #{id}")
    public Account findAccountById(@Param("id") long id);

    @Select("SELECT * FROM ACCOUNT WHERE NAME = #{name}")
    public Account findAccountByName(@Param("name") String name);

    @Insert("INSERT INTO ACCOUNT(NAME, AMOUNT) VALUES (#{account.name}, #{account.amount})")
    @SelectKey(statement = "call identity()", keyProperty = "account.id", before = false, resultType = Long.class)
    public void save(@Param("account") Account account);

    @Update("UPDATE ACCOUNT SET NAME = #{account.name}, AMOUNT = #{account.amount} WHERE ID = #{account.id}")
    public void update(@Param("account") Account account);

}
