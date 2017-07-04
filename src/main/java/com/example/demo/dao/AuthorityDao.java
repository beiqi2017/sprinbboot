package com.example.demo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.Authority;

@Mapper
public interface AuthorityDao {

	  List<Authority> getAll();
}

	
