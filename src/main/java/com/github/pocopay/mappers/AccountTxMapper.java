package com.github.pocopay.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.github.pocopay.model.AccountTx;

public interface AccountTxMapper {

    @Select("SELECT * FROM ACCOUNT_TX WHERE FROM_ACCOUNT_ID = #{fromId}")
    @Results({
            @Result(property = "fromAccountId", column = "FROM_ACCOUNT_ID"),
            @Result(property = "toAccountId", column = "TO_ACCOUNT_ID")
    })
    public List<AccountTx> findOutgoingTxs(@Param("fromId") long fromId);

    @Insert("INSERT INTO ACCOUNT_TX(FROM_ACCOUNT_ID, TO_ACCOUNT_ID, DATE, AMOUNT) "
            + "VALUES (#{tx.fromAccountId}, #{tx.toAccountId}, #{tx.date}, #{tx.amount})")
    @SelectKey(statement = "call identity()", keyProperty = "tx.id", before = false, resultType = Long.class)
    public void save(@Param("tx") AccountTx tx);
}
