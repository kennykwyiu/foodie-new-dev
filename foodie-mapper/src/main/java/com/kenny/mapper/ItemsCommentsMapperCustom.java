package com.kenny.mapper;

import com.kenny.my.mapper.MyMapper;
import com.kenny.pojo.ItemsComments;
import com.kenny.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {
    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);


}