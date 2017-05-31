package com.example.demo.dao;

import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public class UserMapper extends  DynamicQueryRepository {

	private static final long serialVersionUID = 1L;
	
	public List<User>  findAll(){
		return this.sqlQuery("SELECT * FROM USER", null);
	}
}
